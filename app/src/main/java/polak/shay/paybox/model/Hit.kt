package polak.shay.paybox.model

import polak.shay.paybox.R


data class Hit(
    val comments: Int?,
    val downloads: Int?,
    val favorites: Int?,
    val id: Int?,
    val imageHeight: Int?,
    val imageSize: Int?,
    val imageWidth: Int?,
    val largeImageURL: String?,
    val likes: Int?,
    val pageURL: String?,
    val previewHeight: Int?,
    val previewURL: String?,
    val previewWidth: Int?,
    val tags: String?,
    val type: String?,
    val user: String?,
    val userImageURL: String?,
    val user_id: Int?,
    val views: Int?,
    val webformatHeight: Int?,
    val webformatURL: String?,
    val webformatWidth: Int?
) {

    var smallUrl : Any? = null
        get() {
            if (field == null) {
                initSmall()
            }
            return field
        }
        private set
/*
    var largeUrl : Any? = null
        get() {
            if (field == null) {
                initLarge()
            }
            return field
        }
        private set

    private fun initLarge() {
        if(largeImageURL.isNullOrEmpty())
        {
            if(webformatURL.isNullOrEmpty())
            {
                if(userImageURL.isNullOrEmpty())
                {
                    largeUrl = R.drawable.no_image
                }
                else
                {
                    largeUrl = userImageURL
                }
            }
            else
            {
                largeUrl = webformatURL
            }
        }
        else
        {
            largeUrl = largeImageURL
        }
    }
*/

    private fun initSmall() {
        if(userImageURL.isNullOrEmpty())
        {
            if(webformatURL.isNullOrEmpty())
            {
               if(largeImageURL.isNullOrEmpty())
               {
                   smallUrl = R.drawable.no_image
               }
               else
               {
                   smallUrl = largeImageURL
               }
            }
            else
            {
                smallUrl = webformatURL
            }
        }
        else
        {
            smallUrl = userImageURL
        }
    }
}