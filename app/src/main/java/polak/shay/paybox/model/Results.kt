package polak.shay.paybox.model

data class Results(
    var hits: MutableList<Hit>,
    val total: Int?,
    val totalHits: Int?
)