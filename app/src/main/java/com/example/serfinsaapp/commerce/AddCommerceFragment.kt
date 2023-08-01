package com.example.serfinsaapp.commerce

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.serfinsaapp.data.Commerce
import com.example.serfinsaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddCommerceFragment: Fragment() {
    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth
    private lateinit var etCommerceName: EditText
    private lateinit var etDepartment: EditText
    private lateinit var etDocument: EditText
    private lateinit var affiliateButton: Button
    private lateinit var imageView: ImageView
    private lateinit var btnImageSelector: Button

    private var PICK_IMAGE_REQUEST = 1
    private lateinit var mImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_commerce, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etCommerceName = view.findViewById(R.id.etCommerceName)
        etDepartment = view.findViewById(R.id.etDepartment)
        etDocument = view.findViewById(R.id.etDocument)
        affiliateButton = view.findViewById(R.id.btnAffiliateCommerce)
        imageView = view.findViewById(R.id.imageView)
        btnImageSelector = view.findViewById(R.id.btnImageSelector)

        auth = FirebaseAuth.getInstance()

        affiliateButton.setOnClickListener {
            addCommerce(
                commerceName = etCommerceName.text.toString(),
                department = etDepartment.text.toString(),
                document = etDocument.text.toString()
            )
        }

        btnImageSelector.setOnClickListener {
            val intent = Intent()
            // on below line setting type of files which we want to pick in our case we are picking images.
            intent.type = "image/*"
            // on below line we are setting action to get content
            intent.action = Intent.ACTION_GET_CONTENT
            // on below line calling start activity for result to choose image.
            startActivityForResult(
                // on below line creating chooser to choose image.
                Intent.createChooser(
                    intent,
                    "Pick your image to upload"
                ),
                PICK_IMAGE_REQUEST
            )
        }
    }

    private fun addCommerce(commerceName: String, department: String, document: String) {
        val commerce = Commerce(
            id = auth.currentUser?.uid,
            name = commerceName,
            department = department,
            document = document
        )
        db.collection("Commerce").add(commerce).addOnSuccessListener {
            clearEditText()
            Toast.makeText(
                activity,
                getString(R.string.success_aggregate_commerce),
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            clearEditText()
            Toast.makeText(
                activity,
                getString(R.string.something_went_wrong) + it.localizedMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearEditText() {
        etCommerceName.text.clear()
        etDepartment.text.clear()
        etDocument.text.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
             mImageUri = data.data!!
             imageView.setImageURI(mImageUri)
         }
    }
}