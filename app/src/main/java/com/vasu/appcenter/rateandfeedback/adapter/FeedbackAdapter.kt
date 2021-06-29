package com.vasu.appcenter.rateandfeedback.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vasu.appcenter.R
import com.vasu.appcenter.rateandfeedback.adapter.FeedbackAdapter.PointHolder
import java.util.*

class FeedbackAdapter(var context: Context, var mImageList: ArrayList<String>) : RecyclerView.Adapter<PointHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PointHolder {
        return PointHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_feedback, viewGroup, false))
    }

    override fun onBindViewHolder(holder: PointHolder, position: Int) {
        Log.e("TAG_1", mImageList[position])
        Glide.with(context).load(mImageList[position]).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                Log.e("TAG_1", e.toString())
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                Log.e("TAG_1", "onResourceReady")
                return false
            }
        }).into(holder.ivAdd)
        holder.ivClose.setOnClickListener { v: View? ->
            mImageList.removeAt(position)
            notifyDataSetChanged()
            val intent = Intent("FeedbackActivity")
            context.sendBroadcast(intent)
        }
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }

    class PointHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivAdd: ImageView = view.findViewById(R.id.iv_add)
        var ivClose: ImageView = view.findViewById(R.id.close_1)

    }
}