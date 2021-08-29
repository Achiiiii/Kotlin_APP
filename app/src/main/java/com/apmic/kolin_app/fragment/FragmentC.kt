package com.apmic.kolin_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.apmic.kolin_app.R
import com.apmic.kolin_app.app.AppController

class FragmentC: Fragment(){
    private val args: FragmentCArgs by navArgs() //using safe Arg to receive data from Adapter in fragmentB
    var imageLoader: ImageLoader? = AppController.getInstance()?.getImageLoader()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(imageLoader == null) imageLoader = AppController.getInstance()?.getImageLoader()
        val description: TextView = view.findViewById(R.id.description_id)
        val copyright: TextView = view.findViewById(R.id.copyright_id)
        val title: TextView = view.findViewById(R.id.title_id)
        val date: TextView = view.findViewById(R.id.date_id)
        val hdurl: NetworkImageView = view.findViewById(R.id.imghd_id)
        val date_origin: String = args.date.toString()
        val list = date_origin.split("-")
        var month = list[1]
        when (month){
            "01" -> month = "Jan."
            "02" -> month = "Feb."
            "03" -> month = "Mar."
            "04" -> month = "Apr."
            "05" -> month = "May."
            "06" -> month = "Jun."
            "07" -> month = "Jul."
            "08" -> month = "Aug."
            "09" -> month = "Sep."
            "10" -> month = "Oct."
            "11" -> month = "Nov."
            "12" -> month = "Dec."
        }
        description.text = args.description
        copyright.text = "Credit & Copyright: " + args.copyright
        title.text = args.title
        date.text = list[0] + " " + month + " " + list[2]
        hdurl.setImageUrl(args.hdurl, imageLoader)
    }
}