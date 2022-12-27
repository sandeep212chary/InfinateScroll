package com.infinatescroll.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var recylerViewAdapter: RecylerViewAdapter? = null
    var rowsArrayList: ArrayList<String> = arrayListOf()

    var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initAdapter()
        initScrollListener()
    }

    private fun initData() {
        var i = 0
        while (i < 20) {
            rowsArrayList.add("ITEM $i")
            i++
        }
    }

    private fun initAdapter() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
//        recyclerView?.itemAnimator = null
        recylerViewAdapter = RecylerViewAdapter(rowsArrayList)
        recyclerView?.adapter = recylerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) {
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add("-1")
        recylerViewAdapter?.notifyItemInserted(rowsArrayList.size - 1)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recylerViewAdapter?.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                rowsArrayList.add("Item $currentSize")
                currentSize++
            }
            recylerViewAdapter?.notifyDataSetChanged()
            isLoading = false
        }, 1000)
    }
}