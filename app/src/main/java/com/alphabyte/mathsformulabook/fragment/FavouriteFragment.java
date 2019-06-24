package com.alphabyte.mathsformulabook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabyte.mathsformulabook.R;
import com.alphabyte.mathsformulabook.activity.DetailsActivity;
import com.alphabyte.mathsformulabook.adapter.HomeAdapter;
import com.alphabyte.mathsformulabook.helper.ClickListener;
import com.alphabyte.mathsformulabook.helper.DividerItemDecoration;
import com.alphabyte.mathsformulabook.helper.PreferenceHelper;
import com.alphabyte.mathsformulabook.models.Favourite;
import com.alphabyte.mathsformulabook.models.TopicList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteFragment extends Fragment {

    PreferenceHelper helper;
    String favourites;
    List<TopicList.TopicDetails> topicList;
    HomeAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    Gson gson;

    @BindView(R.id.recyclerViewFavourite)
    RecyclerView recyclerView;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();
        helper = new PreferenceHelper(getContext());
        favourites = helper.getFavouriteList();
        Favourite fav = gson.fromJson(favourites,Favourite.class);
        if(fav != null) {
            topicList = fav.getTopicList();
        }else{
            topicList = new ArrayList<>();
        }
        adapter = new HomeAdapter(topicList, getContext(), new ClickListener() {
            @Override
            public void onClicked(int position) {
                TopicList.TopicDetails topic = topicList.get(position);

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("topic_file_name",topic.getTopic_file_name());
                intent.putExtra("topic_selected", topic.getTopic_name());

                startActivity(intent);

            }

            @Override
            public void onLongClicked(int position) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);
        updateScreen();

    }

    @Override
    public void onResume() {
        super.onResume();
        favourites = helper.getFavouriteList();
        Favourite fav = gson.fromJson(favourites,Favourite.class);
        if(fav != null) {
            topicList = fav.getTopicList();
        }else{
            topicList = new ArrayList<>();
        }
        adapter.setFilteredList(topicList);
        adapter.notifyDataSetChanged();
        updateScreen();
    }

    private void updateScreen() {
        if(topicList.size() == 0){
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else{
            textView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
}