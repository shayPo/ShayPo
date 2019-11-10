package polak.shay.paybox

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import polak.shay.paybox.adapters.ImageAdapter
import polak.shay.paybox.view_model.MainViewModel
import polak.shay.paybox.view_model.MainViewModelAPI


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mGridLayoutManager by lazy { GridLayoutManager(applicationContext, 2, RecyclerView.VERTICAL, false) }
    private val mViewModel: MainViewModelAPI by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }
    private val mAdapter by lazy { ImageAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.search_icon5)

        resultList.layoutManager = mGridLayoutManager
        resultList.adapter = mAdapter
        resultList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visiblePosition = mGridLayoutManager.findLastVisibleItemPosition() - mAdapter.itemCount + 1
                if (visiblePosition == 0) {
                    if (!mViewModel.isLoading()) {
                        mViewModel.addPage()
                    }
                }
            }
        })

        mViewModel.getItemSize().observe(this, Observer { data -> mAdapter.setItemSize(data) })
        resultList.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                resultList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mViewModel.calcPreviewItemSize(resultList.height, resultList.width)
            }
        })
        mViewModel.getImageResults().observe(this, Observer { data ->
            if(mAdapter.itemCount > 0) { Toast.makeText(applicationContext, R.string.data_updated,Toast.LENGTH_LONG).show() }
            mAdapter.setData(data)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val find = to_search.text.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(!mViewModel.search(find))
        return super.onOptionsItemSelected(item)
    }

    //on image click
    override fun onClick(v: View?) {
        val intent = Intent(this, DisplayActivity::class.java)
        intent.putExtra(getString(R.string.index_key), v?.tag as Int)
        startActivity(intent)
    }

}
