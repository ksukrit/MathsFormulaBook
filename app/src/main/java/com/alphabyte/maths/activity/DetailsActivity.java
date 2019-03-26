package com.alphabyte.maths.activity;

import android.os.Bundle;
import android.util.Log;

import com.alphabyte.maths.R;
import com.alphabyte.maths.adapter.TopicAdapter;
import com.alphabyte.maths.helper.ClickListener;
import com.alphabyte.maths.helper.PreferenceHelper;
import com.alphabyte.maths.models.Favourite;
import com.alphabyte.maths.models.Maths;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements ClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    TopicAdapter adapter;
    List<Maths.Topic.Subtopic> subtopicList;
    boolean darkTheme;
    int fontSize;
    PreferenceHelper helper;
    Favourite fav;
    String favString;
    boolean favourite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper =  new PreferenceHelper(this);
        darkTheme = helper.isDarkTheme();
        fontSize = helper.getFontSize();

        if(darkTheme){
            setTheme(R.style.AppTheme_DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Gson gson = new Gson();



        final String topicName = getIntent().getStringExtra("topic_selected");
        Bundle bundle = getIntent().getExtras();
        final Maths.Topic topic = (Maths.Topic) bundle.getSerializable("topic_data");

        String favString = helper.getFavouriteList();
        Favourite fav = gson.fromJson(favString,Favourite.class);
        if(fav !=null){
            favourite = isFavourite(topic,fav.getTopicList());
        }else {
            favourite = false;
        }
        if(favourite){
            Log.i("favourite", "Favourite topic changing color");
            fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorite_black_24dp));
        }

        subtopicList = topic.getSubtopic();
        adapter = new TopicAdapter(subtopicList,DetailsActivity.this,this,darkTheme,fontSize);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view -> {
            if(favourite){
                helper.removeTopic(topic);
                Snackbar.make(view,"Removed Topic From Favourites",Snackbar.LENGTH_LONG).show();
                fab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this,R.drawable.ic_favorite_border_black_24dp));
            }else{
                helper.addTopic(topic);
                Snackbar.make(view,"Added Topic To Favourites",Snackbar.LENGTH_LONG).show();
                //String favString = helper.getFavouriteList();
                // Favourite fav = gson.fromJson(favString,Favourite.class);
                fab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this,R.drawable.ic_favorite_black_24dp));
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(topicName);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    boolean isFavourite(Maths.Topic topic, List<Maths.Topic> topicList){
        boolean flag = false;
        for (Maths.Topic t : topicList){
            if(topic.getTopic_name().equals(t.getTopic_name())){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public void onClicked(int position) {
    }

    @Override
    public void onLongClicked(int position) {

    }
}