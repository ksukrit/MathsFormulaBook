package com.alphabyte.maths.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabyte.maths.R;
import com.alphabyte.maths.activity.DetailsActivity;
import com.alphabyte.maths.adapter.HomeAdapter;
import com.alphabyte.maths.helper.ClickListener;
import com.alphabyte.maths.helper.DividerItemDecoration;
import com.alphabyte.maths.helper.PreferenceHelper;
import com.alphabyte.maths.models.TopicList;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private List<TopicList.TopicDetails> topicDetailsList;
    private List<TopicList.TopicDetails> filterList;
    private static String PACKAGE_NAME;
    private HomeAdapter adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private PreferenceHelper preferenceHelper;
    private int fontSize[] = {8,10,14,16,20};


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        filterList = new ArrayList<>();
        Gson gson = new Gson();
        String rawData;

        preferenceHelper = new PreferenceHelper(getContext());


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        try {
            rawData = AssetJSONFile("topics.json",getContext());
            TopicList topicList = gson.fromJson(rawData,TopicList.class);
            //Maths maths = gson.fromJson(rawData,Maths.class);
            topicDetailsList = topicList.getTopicDetails();
            filterList.addAll(topicDetailsList);
            //Log.d("Result", "onCreate() returned: " + topicList.get(0).getTopic_name() );
            adapter = new HomeAdapter(topicDetailsList, getContext(), new ClickListener() {
                @Override
                public void onClicked(int position) {
                    TopicList.TopicDetails topic = filterList.get(position);

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    /*
                    Bundle bundle = new Bundle();
                    bundle.putString("topic_file_name",topic.getTopic_file_name());
                    */
                    intent.putExtra("topic_file_name",topic.getTopic_file_name());
                    intent.putExtra("topic_selected", topic.getTopic_name());
                    //intent.putExtras(bundle);

                    Bundle params = new Bundle();
                    params.putString("topic_name",topic.getTopic_name());
                    mFirebaseAnalytics.logEvent("view_topic", params);

                    startActivity(intent);
                    //getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                @Override
                public void onLongClicked(int position) {

                }
            });
        } catch (IOException | NullPointerException e) {
            Toast.makeText(getContext(),"Error Loading Data",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.i("Error", "onViewCreated: " + e.getMessage());
        }
    }

    void setupWindowAnimations(){
        android.transition.Slide slide = new Slide();
        slide.setDuration(1000);
        getActivity().getWindow().setExitTransition(slide);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PACKAGE_NAME = getContext().getPackageName();
        setupWindowAnimations();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    private static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        return new String(formArray);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        /*
        MenuItem checkBox = menu.findItem(R.id.darktheme);
        if(preferenceHelper.isDarkTheme()){
            checkBox.setChecked(true);
        }**/
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.fontSize:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.font_layout,null);
                SeekBar seekBar = dialogView.findViewById(R.id.seekBar);
                int cFontSize = preferenceHelper.getFontSize();
                seekBar.setProgress(getProgess(cFontSize));
                builder.setView(dialogView)
                        .setPositiveButton("Set Font Size", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int size = seekBar.getProgress();
                                preferenceHelper.setFontSize(fontSize[size]);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case R.id.darktheme:
                if(menuItem.isChecked()){
                    menuItem.setChecked(false);
                    preferenceHelper.setDarkTheme(false);
                }else{
                    menuItem.setChecked(true);
                    preferenceHelper.setDarkTheme(true);
                }
                Intent launchIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(launchIntent);
                //getActivity().overridePendingTransition(R.anim.fade_in, R.anim.noanim);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }*/

    private int getProgess(int cFontSize) {
        switch (cFontSize) {
            case 8:
                return 0;
            case 10:
                return 1;
            case 14:
                return 2;
            case 16:
                return 3;
            case 20:
                return 4;
            default:
                return 2;
        }

    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchQuery = newText.toLowerCase();

                if (searchQuery.isEmpty()){
                    adapter.setFilteredList(topicDetailsList);
                    adapter.notifyDataSetChanged();
                    setData();
                    return  true;
                } else {
                    List<TopicList.TopicDetails> filteredList = new ArrayList<>();
                    for (TopicList.TopicDetails t : topicDetailsList){
                        if ( t.getTopic_name().toLowerCase().contains(searchQuery)){
                            filteredList.add(t);
                        }
                    }
                    adapter.setFilteredList(filteredList);
                    adapter.notifyDataSetChanged();
                    setData();
                    return  true;
                }
            }
        });
    }

    private void setData() {
        filterList.clear();
        filterList.addAll(adapter.getFilteredList());
    }

}

