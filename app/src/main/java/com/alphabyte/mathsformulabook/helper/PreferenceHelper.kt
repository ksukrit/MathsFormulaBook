package com.alphabyte.mathsformulabook.helper

import android.content.Context
import android.util.Log
import com.alphabyte.mathsformulabook.models.Favourite
import com.alphabyte.mathsformulabook.models.Maths
import com.alphabyte.mathsformulabook.models.TopicList
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import java.util.*

class PreferenceHelper(internal var mContext: Context) {
    private val FAVOURITE_TOPIC = "favourite"
    private val DARK_THEME = "darkTheme"
    private val FONT_SIZE = "fontSize"
    private var gson: Gson
    private var darkTheme: Boolean = false
    private var fontSize: Int = 0


    private var favourite: String? = null

    var isDarkTheme: Boolean
        get() {
            darkTheme = Hawk.get(DARK_THEME, false)
            return darkTheme
        }
        set(darkTheme) {
            Hawk.put(DARK_THEME, darkTheme)
            this.darkTheme = darkTheme
        }

    val favouriteList: String?
        get() {
            favourite = Hawk.get(FAVOURITE_TOPIC, "")
            return favourite
        }


    init {
        Hawk.init(mContext).build()
        gson = Gson()
        if (Hawk.count() != 0L) {
            favourite = Hawk.get(FAVOURITE_TOPIC, "")
        } else {
            favourite = ""
        }
        darkTheme = false
        fontSize = 12
    }

    fun getFontSize(): Int {
        fontSize = Hawk.get(FONT_SIZE, 12)
        return fontSize
    }

    fun setFontSize(fontSize: Int) {
        Hawk.put(FONT_SIZE, fontSize)
        this.fontSize = fontSize
    }

    fun addTopic(topic: Maths.Topic, fileName: String) {
        val topicList: MutableList<TopicList.TopicDetails>
        val topicDetails = TopicList.TopicDetails()
        topicDetails.topic_name = topic.topic_name
        topicDetails.topic_file_name = fileName
        var flag = 0
        if (favourite != null && favourite!!.length != 0) {
            val fav = gson.fromJson(favourite, Favourite::class.java)
            topicList = fav.topicList
            for (t in topicList) {
                if (t.topic_name == topicDetails.topic_name) {
                    flag = 1
                }
            }
            if (flag == 0) {
                topicList.add(topicDetails)
            }
            fav.topicList = topicList
            favourite = gson.toJson(fav)
            Hawk.put<String>(FAVOURITE_TOPIC, favourite)
        } else {
            val fav = Favourite()
            topicList = ArrayList<TopicList.TopicDetails>()
            topicList.add(topicDetails)
            fav.topicList = topicList
            favourite = gson.toJson(fav)
            Hawk.put<String>(FAVOURITE_TOPIC, favourite)
        }
    }

    fun removeTopic(topic: Maths.Topic, filename: String) {
        val topicList: List<TopicList.TopicDetails>
        val newList: MutableList<TopicList.TopicDetails>
        newList = ArrayList<TopicList.TopicDetails>()
        val output: String
        val index = -1
        val favString = favouriteList
        val fav = gson.fromJson(favString, Favourite::class.java)
        topicList = fav.topicList
        for (t in topicList) {
            if (t.topic_name != topic.topic_name) {
                newList.add(t)
            }
        }

        fav.topicList = newList
        output = gson.toJson(fav)
        Log.i("removeTopic", topic.topic_name + "  " + output)
        favourite = output
        Hawk.put<String>(FAVOURITE_TOPIC, favourite)
    }


}
