package io.github.mklkj.jsouprecyclerviewexample

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var loadAsyncTask: AsyncTask<String, Void, List<ReleaseItem>>? = null

    private val releasesAdapter = ReleasesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        startLoading()
    }

    private fun initRecyclerView() {
        with(releasesAdapter) {
            onReleaseItemClick = { openUrlInBrowser(it) }
        }

        with(recycler_releases) {
            layoutManager = LinearLayoutManager(context)
            adapter = releasesAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun startLoading() {
        loadAsyncTask = ReleasesLoadAsyncTask(this, releasesAdapter)
            .execute("https://jsoup.org/news/")
    }

    private fun openUrlInBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
    }

    override fun onDestroy() {
        super.onDestroy()
        loadAsyncTask?.cancel(true)
    }
}
