package polak.shay.paybox.view_model

import androidx.lifecycle.ViewModel
import polak.shay.paybox.data.Data
import polak.shay.paybox.model.Hit

interface DisplayViewModelAPI
{
    fun getImageResults() : List<Hit>
}

class DisplayViewModel : ViewModel(),DisplayViewModelAPI
{

    override fun getImageResults() : List<Hit>
    {
      return Data.getInstance().getCurrentData()!!.hits
    }
}