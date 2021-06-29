package com.example.appcenter.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Meet Kachhadiya on 12,April,2021
 */

abstract class BaseAdapter<T>(
    var mContext: FragmentActivity,
    var mList: ArrayList<T>,
    var layoutResId: Int,
) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(layoutResId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        callOnBind(holder.itemView, position)
    }

    abstract fun callOnBind(view: View, item: Int)

    override fun getItemCount(): Int {
        return mList.size
    }

    //TODO adding all item to list
    fun addAllItems(mLists: ArrayList<T>) {
        mList.clear()
        mList.addAll(mLists)
        notifyDataSetChanged()
    }

    //TODO adding single item on list
    fun addItem(mLists: T) {
        mList.add(mLists)
        notifyItemInserted(mList.size)
    }

    //TODO To remove Particular item from list
    open fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    //TODO To clear list
    open fun clearList() {
        mList.clear()
        notifyDataSetChanged()
    }

    //TODO To get size of list
    open fun size() = mList.size

    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    /**
     * Here you just have to override BaseAdapter instead of recyclerview adapter class and
     * pass params like Activity context, list of items, and Layout file of adapter.
     * So from now onward no need to a create class for each recyclerview adapter
     *
     * Example..
     *
     * vBinding.rvList.layoutManager = LinearLayoutManager(this@MainActivity)
     * vBinding.rvList.adapter =
     *      object : BaseAdapter(this@MainActivity, userList!!, R.layout.view_user) {
     *          override fun callOnBind(view: View, item: Int) {
     *              view.findViewById<TextView>(R.id.tvName).text = userList!![item].userName
     *          }
     *      }
     */
}