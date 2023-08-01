package com.example.serfinsaapp.commerce.list

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.serfinsaapp.R
import com.example.serfinsaapp.data.CommerceData
import com.example.serfinsaapp.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A fragment representing a list of Items.
 */
class CommerceListFragment : Fragment() {

    private var columnCount = 1

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private val loadingView = activity?.let { LoadingDialog(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        loadingView?.startLoading()
        var values: MutableList<CommerceData.CommerceItem> = arrayListOf()
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                db.collection("Commerce")
                    .get()
                    .addOnSuccessListener { result ->
                        var i = 1
                        for (document in result) {
                            Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                            if (document.data.values.elementAt(2).toString() == auth.currentUser?.uid.toString()) {
                                values.add(
                                    CommerceData.CommerceItem(
                                        id = i.toString(),
                                        content = document.data.values.elementAt(1).toString(),
                                        details = document.data.values.elementAt(1).toString()
                                    )
                                )
                                i += 1
                            }
                        }
                        adapter = CommerceListRecyclerViewAdapter(values)
                        loadingView?.dismiss()
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    }
            }
        }
        return view
    }
}