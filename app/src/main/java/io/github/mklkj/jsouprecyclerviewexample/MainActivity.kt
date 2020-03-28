package io.github.mklkj.jsouprecyclerviewexample

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        with(recycler_releases) {
            layoutManager = LinearLayoutManager(context)
            adapter = releasesAdapter
        }
    }

    private fun startLoading() {
        loadAsyncTask = LoadAsyncTask(this, releasesAdapter)
            .execute("https://jsoup.org/news/")
    }

    override fun onDestroy() {
        super.onDestroy()
        loadAsyncTask?.cancel(true)
    }
}
