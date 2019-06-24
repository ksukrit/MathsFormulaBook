package com.alphabyte.mathsformulabook.adapter;

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

import androidx.recyclerview.widget.RecyclerView;

import com.alphabyte.mathsformulabook.R;
import com.alphabyte.mathsformulabook.helper.ClickListener;
import com.alphabyte.mathsformulabook.models.TopicList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    List<TopicList.TopicDetails> topicList;
    List<TopicList.TopicDetails> mFilteredList;
    Context mContext;
    ClickListener listener;


    public HomeAdapter(List<TopicList.TopicDetails> topicList, Context mContext, ClickListener listener) {
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
        TopicList.TopicDetails topic = mFilteredList.get(position);
        holder.topicName.setText(topic.getTopic_name());
        holder.subtopicName.setText(getSubtopic(topic.getTopic_name()));
        holder.iconText.setText(topic.getTopic_name().substring(0,1));

        holder.iconImage.setImageResource(R.drawable.bg_circle);
        int randomColor = topic.getLogo_color();
        //int randomColor = topic.getLogo_color( ?:ThreadLocalRandom.current().nextInt(0,  11);
        holder.iconImage.setColorFilter(getIconColor(randomColor));

        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClicked(position);
            }
        });
    }

    public void setFilteredList(List<TopicList.TopicDetails> mFilteredList) {
        this.mFilteredList = mFilteredList;
    }

    public List<TopicList.TopicDetails> getFilteredList() {
        return mFilteredList;
    }

    private String getSubtopic(String topicName){
        StringBuilder builder = new StringBuilder();
        builder.append("Learn About ");
        builder.append(topicName);
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
