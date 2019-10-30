package polak.shay.paybox.model

data class Results(
    var hits: List<Hit>,
    val total: Int?,
    val totalHits: Int?
)