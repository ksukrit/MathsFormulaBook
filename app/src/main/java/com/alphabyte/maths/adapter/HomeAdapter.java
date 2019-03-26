package com.alphabyte.maths.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alphabyte.maths.R;
import com.alphabyte.maths.helper.ClickListener;
import com.alphabyte.maths.models.Maths;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    List<Maths.Topic> topicList;
    List<Maths.Topic> mFilteredList;
    Context mContext;
    ClickListener listener;


    public HomeAdapter(List<Maths.Topic> topicList, Context mContext, ClickListener listener) {
        this.topicList = topicList;
        mFilteredList = topicList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, final int position) {
        //Maths.Topic topic = topicList.get(position);
        Maths.Topic topic = mFilteredList.get(position);
        holder.topicName.setText(topic.getTopic_name());
        holder.subtopicName.setText(getSubtopic(topic.getSubtopic()));
        holder.iconText.setText(topic.getTopic_name().substring(0,1));

        holder.iconImage.setImageResource(R.drawable.bg_circle);
        holder.iconImage.setColorFilter(getIconColor(topic.getLogo_color()));

        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClicked(position);
            }
        });
    }

    public void setFilteredList(List<Maths.Topic> mFilteredList) {
        this.mFilteredList = mFilteredList;
    }

    public List<Maths.Topic> getFilteredList() {
        return mFilteredList;
    }

    private String getSubtopic(List<Maths.Topic.Subtopic> subtopic){
        StringBuilder builder = new StringBuilder();
        builder.append("Learn About ");
        for(int i = 0; i < subtopic.size(); i++){
            builder.append(subtopic.get(i).getName());
            builder.append(",");
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        //return topicList.size();
        return mFilteredList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.topicName)
        TextView topicName;
        @BindView(R.id.subtopicName)
        TextView subtopicName;
        @BindView(R.id.icon_image)
        ImageView iconImage;
        @BindView(R.id.icon_text)
        TextView iconText;
        @BindView(R.id.icon_container)
        RelativeLayout iconContainer;
        @BindView(R.id.list_row)
        RelativeLayout mainContainer;

        public HomeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }

    private int getIconColor(int colorId){
        int returnColor = Color.RED;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + "500", "array", mContext.getPackageName());
        if( arrayId != 0 ){
            TypedArray color = mContext.getResources().obtainTypedArray(arrayId);
            returnColor = color.getColor(colorId,Color.RED);
            color.recycle();
        }
        return returnColor;
    }

}
