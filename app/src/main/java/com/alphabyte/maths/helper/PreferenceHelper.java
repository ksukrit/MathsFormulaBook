package com.alphabyte.maths.helper;

import android.content.Context;
import android.util.Log;

import com.alphabyte.maths.models.Favourite;
import com.alphabyte.maths.models.Maths;
import com.alphabyte.maths.models.TopicList;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class PreferenceHelper {
    Context mContext;
    private String FAVOURITE_TOPIC = "favourite";
    private String DARK_THEME = "darkTheme";
    private String FONT_SIZE = "fontSize";
    Gson gson;
    boolean darkTheme;
    int fontSize;


    String favourite;


    public PreferenceHelper(Context mContext) {
        this.mContext = mContext;
        Hawk.init(mContext).build();
        gson = new Gson();
        if(Hawk.count() != 0){
            favourite = Hawk.get(FAVOURITE_TOPIC,"");
        }else{
            favourite = "";
        }
        darkTheme = false;
        fontSize = 12;
    }

    public boolean isDarkTheme() {
        darkTheme = Hawk.get(DARK_THEME,false);
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        Hawk.put(DARK_THEME,darkTheme);
        this.darkTheme = darkTheme;
    }

    public int getFontSize() {
        fontSize = Hawk.get(FONT_SIZE,12);
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        Hawk.put(FONT_SIZE,fontSize);
        this.fontSize = fontSize;
    }

    public String getFavouriteList() {
        favourite = Hawk.get(FAVOURITE_TOPIC,"");
        return favourite;
    }

    public void addTopic(Maths.Topic topic,String fileName) {
        List<TopicList.TopicDetails> topicList;
        TopicList.TopicDetails topicDetails = new TopicList.TopicDetails();
        topicDetails.setTopic_name(topic.getTopic_name());
        topicDetails.setTopic_file_name(fileName);
        int flag = 0;
        if(favourite != null && favourite.length() != 0){
            Favourite fav = gson.fromJson(favourite, Favourite.class);
            topicList = fav.getTopicList();
            for(TopicList.TopicDetails t : topicList){
                if(t.getTopic_name().equals(topicDetails.getTopic_name())){
                    flag = 1;
                }
            }
            if(flag == 0){
                topicList.add(topicDetails);
            }
            fav.setTopicList(topicList);
            favourite = gson.toJson(fav);
            Hawk.put(FAVOURITE_TOPIC,favourite);
        }else {
            Favourite fav = new Favourite();
            topicList = new ArrayList<>();
            topicList.add(topicDetails);
            fav.setTopicList(topicList);
            favourite = gson.toJson(fav);
            Hawk.put(FAVOURITE_TOPIC,favourite);
        }
    }

    public void removeTopic(Maths.Topic topic,String filename){
        List<TopicList.TopicDetails> topicList;
        List<TopicList.TopicDetails> newList;
        newList = new ArrayList<>();
        String output;
        int index = -1;
        String favString = getFavouriteList();
        Favourite fav = gson.fromJson(favString,Favourite.class);
        topicList = fav.getTopicList();
        for(TopicList.TopicDetails t: topicList) {
            if (!t.getTopic_name().equals(topic.getTopic_name())) {
                newList.add(t);
            }
        }

        fav.setTopicList(newList);
        output = gson.toJson(fav);
        Log.i("removeTopic",topic.getTopic_name() + "  " + output );
        favourite = output;
        Hawk.put(FAVOURITE_TOPIC,favourite);
    }


}
