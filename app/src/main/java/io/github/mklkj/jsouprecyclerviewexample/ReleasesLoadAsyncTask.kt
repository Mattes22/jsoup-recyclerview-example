package io.github.mklkj.jsouprecyclerviewexample

import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Jsoup
import java.lang.ref.WeakReference

class ReleasesLoadAsyncTask(activity: MainActivity, adapter: ReleasesAdapter) :
    AsyncTask<String, Void, List<ReleaseItem>>() {

    private val activity: WeakReference<MainActivity> = WeakReference(activity)

    private val adapter: WeakReference<ReleasesAdapter> = WeakReference(adapter)

    private var lastError: Throwable? = null

    override fun onPreExecute() {
        activity.get()?.run {
            findViewById<RecyclerView>(R.id.recycler_releases)?.visibility = INVISIBLE
            findViewById<TextView>(R.id.message_releases)?.visibility = INVISIBLE
            findViewById<ProgressBar>(R.id.progress_releases)?.visibility = VISIBLE
        }
    }

    override fun doInBackground(vararg params: String?): List<ReleaseItem> {
        return try {
            Jsoup.connect(params[0]).get().select(".toc ol a").map {
                ReleaseItem(it.text(), it.attr("abs:href"))
            }.also {
                lastError = null
            }
        } catch (e: Throwable) {
            Log.e("ReleasesLoadAsyncTask", "Error: ${e.message}", e)
            lastError = e
            emptyList()
        }
    }

    override fun onPostExecute(result: List<ReleaseItem>) {
        if (result.isNotEmpty()) {
            adapter.get()?.run {
                items = result
                notifyDataSetChanged()
            }
            activity.get()?.findViewById<RecyclerView>(R.id.recycler_releases)?.visibility = VISIBLE
        } else {
            activity.get()?.findViewById<TextView>(R.id.message_releases)?.run {
                visibility = VISIBLE
                text = if (lastError == null) "No items found" else lastError?.message
            }
        }

        activity.get()?.findViewById<ProgressBar>(R.id.progress_releases)?.visibility = View.GONE
    }
}
