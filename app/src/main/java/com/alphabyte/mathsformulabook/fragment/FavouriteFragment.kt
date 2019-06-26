package com.alphabyte.mathsformulabook.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.alphabyte.mathsformulabook.R
import com.alphabyte.mathsformulabook.activity.DetailsActivity
import com.alphabyte.mathsformulabook.adapter.HomeAdapter
import com.alphabyte.mathsformulabook.helper.ClickListener
import com.alphabyte.mathsformulabook.helper.DividerItemDecoration
import com.alphabyte.mathsformulabook.helper.PreferenceHelper
import com.alphabyte.mathsformulabook.models.Favourite
import com.alphabyte.mathsformulabook.models.TopicList
import com.google.gson.Gson
import java.util.*

class FavouriteFragment : Fragment() {

    internal lateinit var helper: PreferenceHelper
    internal var favourites: String? = null
    internal lateinit var topicList: List<TopicList.TopicDetails>
    internal var adapter: HomeAdapter? = null
    internal var mLayoutManager: RecyclerView.LayoutManager? = null
    internal lateinit var gson: Gson

    @BindView(R.id.recyclerViewFavourite)
    internal lateinit var recyclerView: RecyclerView
    @BindView(R.id.textView)
    internal lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gson = Gson()
        helper = PreferenceHelper(context!!)
        favourites = helper.favouriteList
        val fav = gson.fromJson(favourites, Favourite::class.java)
        if (fav != null) {
            topicList = fav.topicList
        } else {
            topicList = ArrayList<TopicList.TopicDetails>()
        }

        adapter = HomeAdapter(topicList, context, object : ClickListener {
            override fun onClicked(position: Int) {
                val topic = topicList[position]

                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("topic_file_name", topic.topic_file_name)
                intent.putExtra("topic_selected", topic.topic_name)
                intent.putExtra("topic_color", topic.logo_color)

                startActivity(intent)

            }

            override fun onLongClicked(position: Int) {

            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutManager = LinearLayoutManager(activity)
        (mLayoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(activity!!, RecyclerView.VERTICAL))
        recyclerView.adapter = adapter
        if (adapter != null) {
            recyclerView.adapter = adapter
        } else {
            Log.v("TAG", "adapter is null")
        }
        updateScreen()

    }

    override fun onResume() {
        super.onResume()
        favourites = helper.favouriteList
        val fav = gson.fromJson(favourites, Favourite::class.java)
        if (fav != null) {
            topicList = fav.topicList
        } else {
            topicList = ArrayList<TopicList.TopicDetails>()
        }
        adapter?.filteredList = topicList
        adapter?.notifyDataSetChanged()
        updateScreen()
    }

    private fun updateScreen() {
        if (topicList.isEmpty()) {
            textView?.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
        } else {
            textView?.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        ButterKnife.bind(this, view)
        return view
    }
}