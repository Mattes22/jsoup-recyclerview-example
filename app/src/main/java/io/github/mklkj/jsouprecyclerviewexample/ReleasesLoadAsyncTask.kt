package io.github.mklkj.jsouprecyclerviewexample

import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Jsoup
import java.lang.ref.WeakReference

class ReleasesLoadAsyncTask(activity: MainActivity, adapter: ReleasesAdapter) :
    AsyncTask<String, Void, List<ReleaseItem>>() {

    private val activity: WeakReference<MainActivity> = WeakReference(activity)

    private val adapter: WeakReference<ReleasesAdapter> = WeakReference(adapter)

    override fun onPreExecute() {
        activity.get()?.run {
            findViewById<RecyclerView>(R.id.recycler_releases)?.visibility = View.INVISIBLE
            findViewById<ProgressBar>(R.id.progress_releases)?.visibility = View.VISIBLE
        }
    }

    override fun doInBackground(vararg params: String?): List<ReleaseItem> {
        return Jsoup.connect(params[0]).get().select(".toc ol a").map {
            ReleaseItem(it.text(), it.attr("abs:href"))
        }
    }

    override fun onPostExecute(result: List<ReleaseItem>) {
        adapter.get()?.run {
            items = result
            notifyDataSetChanged()
        }

        activity.get()?.run {
            findViewById<RecyclerView>(R.id.recycler_releases)?.visibility = View.VISIBLE
            findViewById<ProgressBar>(R.id.progress_releases)?.visibility = View.GONE
        }
    }
}
