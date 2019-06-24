package com.alphabyte.mathsformulabook.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicList {
    private int topic_count;
    private int data_version;
    private String last_updated;
    @SerializedName("topic_list")
    private List<TopicDetails> topicDetails;

    public int getTopic_count() {
        return topic_count;
    }

    public void setTopic_count(int topic_count) {
        this.topic_count = topic_count;
    }

    public int getData_version() {
        return data_version;
    }

    public void setData_version(int data_version) {
        this.data_version = data_version;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public List<TopicDetails> getTopicDetails() {
        return topicDetails;
    }

    public void setTopicDetails(List<TopicDetails> topicDetails) {
        this.topicDetails = topicDetails;
    }

    public static class TopicDetails {
        private String topic_name;
        private String topic_file_name;
        private int logo_color;



        public int getLogo_color() {
            return logo_color;
        }

        public void setLogo_color(int logo_color) {
            this.logo_color = logo_color;
        }


        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        public String getTopic_file_name() {
            return topic_file_name;
        }

        public void setTopic_file_name(String topic_file_name) {
            this.topic_file_name = topic_file_name;
        }
    }
}
