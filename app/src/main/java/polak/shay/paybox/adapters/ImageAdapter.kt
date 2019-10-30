package polak.shay.paybox.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.preview_item.view.*
import polak.shay.paybox.R
import polak.shay.paybox.model.Hit
import java.lang.ref.WeakReference

class ImageAdapter(clickListener: View.OnClickListener?) : RecyclerView.Adapter<ImageAdapter.Holder>() {

    private var mItemSize : ViewGroup.LayoutParams? = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    private val mHits by lazy { mutableListOf<Hit>() }
    private lateinit var mClickListener : WeakReference<View.OnClickListener?>

    init {
            mClickListener = WeakReference(clickListener)
    }

    fun setData(newData: List<Hit>) {
        mHits.clear()
        mHits.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_item, parent, false)
        view.layoutParams = mItemSize
        return Holder(mClickListener.get(), view)
    }

    override fun getItemCount(): Int { return if (mHits.isNullOrEmpty()) { 0 } else mHits!!.size }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setup(mHits?.get(position), position)
    }

    fun setItemSize(data: ViewGroup.LayoutParams?) {
        mItemSize = data
    }

    class Holder(clickListener: View.OnClickListener?, val mView: View) : RecyclerView.ViewHolder(mView) {

        init { mView.setOnClickListener(clickListener) }

        fun setup(data: Hit?, index : Int) {
            val url = data?.userImageURL
            mView.tag = index
            Picasso.get().load(url).into(mView?.hit_image)
        }
    }
}