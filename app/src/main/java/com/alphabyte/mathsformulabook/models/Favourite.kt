package com.alphabyte.mathsformulabook.models

class Favourite {

    var topicList: MutableList<TopicList.TopicDetails>
        get() {
            return topicList
        }
        set(t: MutableList<TopicList.TopicDetails>) {
            topicList = t
        }

}
