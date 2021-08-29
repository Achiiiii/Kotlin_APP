package com.apmic.kolin_app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.apmic.kolin_app.R
import com.apmic.kolin_app.adapter.DataAdapter
import com.apmic.kolin_app.app.AppController
import com.apmic.kolin_app.dataModel.Data

class FragmentB: Fragment() {

    lateinit var gridLayoutManager: GridLayoutManager
    val jsonURL: String = "https://raw.githubusercontent.com/cmmobile/NasaDataSet/main/apod.json"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var listData = ArrayList<Data>() //json data

        gridLayoutManager = GridLayoutManager(activity,4)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL

        val dataList = view.findViewById<RecyclerView>(R.id.data_list)
        dataList?.layoutManager = gridLayoutManager //recyclerview use gridLayoutManager
        var jsonRequest =
            JsonArrayRequest(
                Request.Method.GET, jsonURL, null,
                Response.Listener{ response ->
                    if (response != null) {
                        try {
                            val usersArray = response
                            for (i in 0 until usersArray.length()) {
                                val jsonObject = usersArray.getJSONObject(i)
                                val description = jsonObject.getString("description")
                                val copyright = jsonObject.getString("copyright")
                                val title = jsonObject.getString("title")
                                val url = jsonObject.getString("url")
                                val apod_site = jsonObject.getString("apod_site")
                                val date = jsonObject.getString("date")
                                val media_type = jsonObject.getString("media_type")
                                val hdurl = jsonObject.getString("hdurl")
                                listData.add(
                                    Data(
                                        description,
                                        copyright,
                                        title,
                                        url,
                                        apod_site,
                                        date,
                                        media_type,
                                        hdurl
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        dataList?.adapter = DataAdapter(listData)
                    }
                },
                Response.ErrorListener { error: VolleyError? -> }
            )
        AppController.getInstance()?.addToRequestQueue(jsonRequest)
    }
}