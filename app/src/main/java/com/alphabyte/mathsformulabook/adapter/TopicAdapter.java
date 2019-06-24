package com.alphabyte.mathsformulabook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabyte.mathsformulabook.R;
import com.alphabyte.mathsformulabook.helper.ClickListener;
import com.alphabyte.mathsformulabook.models.Maths;
import com.alphabyte.mathview.MathView;
import com.bumptech.glide.Glide;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder>{

    List<Maths.Topic.Subtopic> subtopicList;
    Context mContext;
    ClickListener listener;
    boolean darkTheme;
    int fontSize;
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public TopicAdapter(List<Maths.Topic.Subtopic> subtopicList, Context mContext, ClickListener listener) {
        this.subtopicList = subtopicList;
        this.mContext = mContext;
        this.listener = listener;
        darkTheme = false;
    }

    public TopicAdapter(List<Maths.Topic.Subtopic> subtopicList, Context mContext, ClickListener listener, boolean darkTheme, int fontSize) {
        this.subtopicList = subtopicList;
        this.mContext = mContext;
        this.listener = listener;
        this.darkTheme = darkTheme;
        this.fontSize = fontSize;
        expansionsCollection.openOnlyOne(true);
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subtopic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, final int position) {
        Maths.Topic.Subtopic subtopic = subtopicList.get(position);
        holder.subtopicName.setText(subtopic.getName());
        if (subtopic.getImages().equals("No") || subtopic.getImages() == null || subtopic.getImages().length() == 0){
            holder.imageContainer.setVisibility(View.GONE);
        }else {
            Glide.with(mContext)
                    .load(Uri.parse(subtopic.getImages()))
                    .fitCenter()
                    .into(holder.imageView);
            if (darkTheme) {
                holder.imageView.setColorFilter(Color.WHITE);
            }
        }



        holder.formula.setTextSize(fontSize);

        if (subtopic.getFormula().equals("No")){
            holder.formulaContainer.setVisibility(View.GONE);
        } else {
            holder.formula.setDisplayText(subtopic.getFormula());
            if(darkTheme){
                //holder.formula.setViewBackgroundColor(Color.BLACK);
                holder.formula.setTextColor(Color.WHITE);
            }
        }

        String information = getInformation(subtopic,subtopic.isDisplay_information());
        if(subtopic.isDisplay_information()){
            holder.expansionHeader.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 24) {
                holder.information.setText(Html.fromHtml(information,Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.information.setText(Html.fromHtml(information));
            }
        }else{
            holder.information.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 24) {
                holder.expandedInformation.setText(Html.fromHtml(information,Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.expandedInformation.setText(Html.fromHtml(information));
            }
            expansionsCollection.add(holder.expansionLayout);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClicked(position);
                if(holder.expansionHeader.getVisibility() == View.VISIBLE){
                    holder.expansionLayout.toggle(true);
                }
            }
        });

    }

    private String getInformation(Maths.Topic.Subtopic subtopic, boolean display_information) {
        StringBuilder builder = new StringBuilder();
        if(display_information){
            builder.append("Information : <br>");
        }
        List<String> informationList = subtopic.getInformation();

        for( int i = 0 ; i < informationList.size(); i++ ){
            builder.append("• " + informationList.get(i) + " <br>");
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return subtopicList.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        @BindView(R.id.subtopicName)
        TextView subtopicName;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.image_container)
        RelativeLayout imageContainer;
        @BindView(R.id.formula)
        MathView formula;
        @BindView(R.id.formula_container)
        RelativeLayout formulaContainer;
        @BindView(R.id.information)
        TextView information;
        @BindView(R.id.informationContainer)
        RelativeLayout informationContainer;
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.expansionLayout)
        ExpansionLayout expansionLayout;
        @BindView(R.id.expansionHeader)
        ExpansionHeader expansionHeader;
        @BindView(R.id.information_expanded)
        TextView expandedInformation;

        TopicViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }
}
