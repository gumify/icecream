package me.gumify.icecream.model

import org.json.JSONObject
import sh.fearless.hiper.Hiper
import me.gumify.icecream.Icecream
import sh.fearless.lib.icecream.RequestCaller

data class Wallpaper(
    val id: String,
    val imageUrl: String,
    val licensed: Boolean,
    val title: String
) {
    private val hiper = Hiper.getInstance().async()
    private val directUrlQuery = "\n    query contentDownloadUrl(\$itemId: ID!) {\n      contentDownloadUrlAsUgc(itemId: \$itemId)\n    }\n  "
    fun directUrl(callback: (url: String, e: String?) -> Unit): RequestCaller? {
        try {
            val json = JSONObject("""
            {"variables":{"itemId":"$id"}}
        """.trimIndent())
            json.put("query", directUrlQuery)
            val queue = hiper.post(Icecream.API_URL, json = json) {
                if (this.isSuccessful) {
                    val obj = JSONObject(this.text!!)
                    if (obj.has("errors")) {
                        callback("", this.text!!)
                    } else {
                        val url = obj.getJSONObject("data").getString("contentDownloadUrlAsUgc")
                        callback(url, null)
                    }
                }
            }
            return RequestCaller(queue)
        } catch (e: Exception) {
            callback("", e.toString())
        }
        return null
    }
}
