package polak.shay.paybox.view_model

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import polak.shay.paybox.R
import polak.shay.paybox.data.Data
import polak.shay.paybox.model.Hit
import polak.shay.paybox.model.Results
import java.util.*

interface MainViewModelAPI {

    fun calcPreviewItemSize(height: Int, width: Int)

    fun getImageResults(): LiveData<List<Hit>>

    fun getItemSize(): LiveData<ViewGroup.LayoutParams>

    fun onImageClick(index: Int)

    fun search(find: String) : Boolean

    fun isLoading() : Boolean

    fun addPage()
}


class MainViewModel : ViewModel(), MainViewModelAPI, Observer {

    private var mSearchWord : String = ""
    private var mLoading : Boolean = false
    private var mItemSize: MutableLiveData<ViewGroup.LayoutParams> = MutableLiveData()
    private var mImageResults: Results? = null
    private var mImageHits: MutableLiveData<List<Hit>> = MutableLiveData()

    override fun getImageResults(): LiveData<List<Hit>> {
        return mImageHits
    }

    override fun getItemSize(): LiveData<ViewGroup.LayoutParams> {
        return mItemSize
    }

    override fun calcPreviewItemSize(height: Int, width: Int) {
        mItemSize.postValue(ViewGroup.LayoutParams(width / 2, height / 2))
    }

    override fun search(find: String) : Boolean{
        if(!mSearchWord.equals(find))
        {
            mSearchWord = find
            mImageResults = null
            mImageHits.postValue(emptyList())
            Data.getInstance().search(find, 1,this)
            return true
        }
        return false
    }

    override fun isLoading(): Boolean { return  mLoading }

    override fun addPage() {
        if(mImageResults != null)
        {
            if(mImageResults!!.hits.size < mImageResults!!.total!!) {
                mLoading = true
                var page = mImageResults!!.hits.size.div(R.integer.items_in_page)
                page++
                Data.getInstance().search(mSearchWord, page, this)
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        o?.deleteObservers()
        mLoading = false

        val data = arg as Results
        if( mImageResults == null){
            mImageResults = data
        }
        else{
            var a = mImageResults?.hits?.toMutableList()
            if(a?.addAll(data?.hits)!!)
            {
                mImageResults?.hits = a?.toList()
                Data.getInstance().updateCurrentData(mImageResults!!)
            }
        }

        mImageHits.postValue(mImageResults?.hits)
    }

    override fun onImageClick(index: Int) {

    }

}