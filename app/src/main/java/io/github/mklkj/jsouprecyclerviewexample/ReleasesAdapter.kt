package io.github.mklkj.jsouprecyclerviewexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReleasesAdapter : RecyclerView.Adapter<ReleasesAdapter.ViewHolder>() {

    var items: List<ReleaseItem> = emptyList()

    var onReleaseItemClick: (url: String) -> Unit = {}

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_release, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.view.findViewById<TextView>(R.id.item_release)) {
            text = item.name
            setOnClickListener { onReleaseItemClick(item.url) }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
