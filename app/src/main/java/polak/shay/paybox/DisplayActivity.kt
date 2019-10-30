package polak.shay.paybox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_display.*
import kotlinx.android.synthetic.main.activity_main.*
import polak.shay.paybox.adapters.ImageAdapter
import polak.shay.paybox.view_model.DisplayViewModel
import polak.shay.paybox.view_model.DisplayViewModelAPI
import polak.shay.paybox.view_model.MainViewModel

class DisplayActivity : AppCompatActivity() {
    private val mGridLayoutManager by lazy { GridLayoutManager(applicationContext, 1, RecyclerView.HORIZONTAL, false) }
    private val mAdapter by lazy { ImageAdapter(null) }
    private val mViewModel: DisplayViewModelAPI by lazy {
        ViewModelProvider.NewInstanceFactory().create(DisplayViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        init(intent.getIntExtra(getString(R.string.index_key), 0))
    }

    private fun init(index: Int) {
        val data = mViewModel.getImageResults()

        display.layoutManager = mGridLayoutManager
        display.adapter = mAdapter
        mAdapter.setData(data)
        display.scrollToPosition(index)
    }
}