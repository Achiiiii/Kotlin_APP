package com.apmic.kolin_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.apmic.kolin_app.R
import com.apmic.kolin_app.app.AppController
import com.apmic.kolin_app.dataModel.Data
import com.apmic.kolin_app.fragment.FragmentBDirections


class DataAdapter(private val mData: ArrayList<Data>) : RecyclerView.Adapter<ViewHolder>() {

    var imageLoader: ImageLoader? = AppController.getInstance()
        ?.getImageLoader()
    //declare current view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(v)
    }
    //set recyclerview data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(imageLoader == null) imageLoader = AppController.getInstance()?.getImageLoader()
        holder.titleText.text = mData[position].title
        holder.titleImage.setImageUrl(mData[position].url, imageLoader)
        //use navigation to send data from fragmentB to fragmentC
        holder.itemView.setOnClickListener{view ->
            view.findNavController().navigate(FragmentBDirections.actionFragmentBToFragmentC(
                mData[position].description,
                mData[position].copyright,
                mData[position].title,
                mData[position].date,
                mData[position].hdurl
            ))
        }

    }
    //get data size
    override fun getItemCount(): Int {
        return mData.size
    }
}
//declare element in recyclerview
class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val titleText: TextView = v.findViewById(R.id.info_text)
    val titleImage: NetworkImageView = v.findViewById(R.id.img)
}