package me.gumify.icecream

import org.json.JSONObject
import sh.fearless.hiper.Hiper
import me.gumify.icecream.model.Ringtone
import sh.fearless.lib.icecream.RequestCaller
import me.gumify.icecream.model.Wallpaper
import java.net.URLEncoder

class Icecream {
    companion object {
        const val API_URL = "https://api-gateway.zedge.net/"
        @Volatile private var instance: Icecream? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Icecream()
        }
    }

    private val hiper = Hiper.getInstance().async()
    private val trendingQuery = "\n    query browse(\$input: BrowseAsUgcInput!) {\n      browseAsUgc(input: \$input) {\n        ...browseContentItemsResource\n      }\n    }\n    \n  fragment browseContentItemsResource on BrowseContentItems {\n    page\n    total\n    items {\n      ... on BrowseWallpaper {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n      }\n\n      ... on BrowseRingtone {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n        meta {\n          durationMs\n          previewUrl\n          gradientStart\n          gradientEnd\n        }\n      }\n    }\n  }\n\n  "
    private val directUrlQuery = "\n    query contentDownloadUrl(\$itemId: ID!) {\n      contentDownloadUrlAsUgc(itemId: \$itemId)\n    }\n  "
    private val searchQuery = "\n    query search(\$input: SearchAsUgcInput!) {\n      searchAsUgc(input: \$input) {\n        ...browseContentItemsResource\n      }\n    }\n    \n  fragment browseContentItemsResource on BrowseContentItems {\n    page\n    total\n    items {\n      ... on BrowseWallpaper {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n      }\n\n      ... on BrowseRingtone {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n        meta {\n          durationMs\n          previewUrl\n          gradientStart\n          gradientEnd\n        }\n      }\n    }\n  }\n\n  "


    /* Ringtone */

    private fun extractRingtones(obj: JSONObject): List<Ringtone> {
        val ringtones = ArrayList<Ringtone>()
        val items = obj.getJSONArray("items")
        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            ringtones.add(
                Ringtone(
                id = item.getString("id"),
                imageUrl = item.getString("imageUrl"),
                licensed = item.getBoolean("licensed"),
                title = item.getString("title"),
                audioUrl = item.getJSONObject("meta").getString("previewUrl"),
                gradientStart = item.getJSONObject("meta").getString("gradientStart"),
                gradientEnd = item.getJSONObject("meta").getString("gradientEnd")
            )
            )
        }
        return ringtones
    }


    fun trendingRingtones(page: Int, callback: (List<Ringtone>, error: String?) -> Unit): RequestCaller? {
        try {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"RINGTONE","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", trendingQuery)
            val queue = hiper.post(API_URL, json=json) {
                if (this.isSuccessful) {
                    var obj = JSONObject(this.text!!)
                    if (obj.has("errors")) {
                        callback.invoke(listOf(), this.text!!)
                    } else {
                        obj = obj.getJSONObject("data").getJSONObject("browseAsUgc")
                        callback.invoke(extractRingtones(obj), null)
                    }
                } else {
                    callback.invoke(listOf(), "Request failed")
                }
            }
            return RequestCaller(queue)
        } catch (e: Exception) {
            callback(listOf(), e.toString())
        }
        return null
    }

    fun searchRingtones(query: String, page: Int, callback: (List<Ringtone>, error: String?) -> Unit): RequestCaller? {
        try {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"RINGTONE","keyword":"${URLEncoder.encode(query.replace("\"", ""), "UTF-8")}","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", searchQuery)
            val queue = hiper.post(API_URL, json=json) {
                if (this.isSuccessful) {
                    var obj = JSONObject(this.text!!)
                    if (obj.has("errors")) {
                        callback.invoke(listOf(), this.text!!)
                    } else {
                        obj = obj.getJSONObject("data").getJSONObject("searchAsUgc")
                        callback.invoke(extractRingtones(obj), null)
                    }
                } else {
                    callback.invoke(listOf(), "Request failed")
                }
            }
            return RequestCaller(queue)
        } catch (e: Exception) {
            callback(listOf(), e.toString())
        }
        return null
    }

    /* Wallpaper */

    private fun extractWallpapers(obj: JSONObject): List<Wallpaper> {
        val wallpapers = ArrayList<Wallpaper>()
        val items = obj.getJSONArray("items")
        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            wallpapers.add(
                Wallpaper(
                id = item.getString("id"),
                imageUrl = item.getString("imageUrl"),
                licensed = item.getBoolean("licensed"),
                title = item.getString("title")
            )
            )
        }
        return wallpapers
    }


    fun trendingWallpapers(page: Int, callback: (List<Wallpaper>, error: String?) -> Unit): RequestCaller? {
        try {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"WALLPAPER","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", trendingQuery)
            val queue = hiper.post(API_URL, json=json) {
                if (this.isSuccessful) {
                    var obj = JSONObject(this.text!!)
                    if (obj.has("errors")) {
                        callback.invoke(listOf(), this.text!!)
                    } else {
                        obj = obj.getJSONObject("data").getJSONObject("browseAsUgc")
                        callback.invoke(extractWallpapers(obj), null)
                    }
                } else {
                    callback.invoke(listOf(), "Request failed")
                }
            }
            return RequestCaller(queue)
        } catch (e: Exception) {
            callback(listOf(), e.toString())
        }
        return null
    }

    fun searchWallpapers(query: String, page: Int, callback: (List<Wallpaper>, error: String?) -> Unit): RequestCaller? {
        try {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"WALLPAPER","keyword":"${URLEncoder.encode(query.replace("\"", ""), "UTF-8")}","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", searchQuery)
            val queue = hiper.post(API_URL, json=json) {
                if (this.isSuccessful) {
                    var obj = JSONObject(this.text!!)
                    if (obj.has("errors")) {
                        callback.invoke(listOf(), this.text!!)
                    } else {
                        obj = obj.getJSONObject("data").getJSONObject("searchAsUgc")
                        callback.invoke(extractWallpapers(obj), null)
                    }
                } else {
                    callback.invoke(listOf(), "Request failed")
                }
            }
            return RequestCaller(queue)
        } catch (e: Exception) {
            callback(listOf(), e.toString())
        }
        return null
    }
}