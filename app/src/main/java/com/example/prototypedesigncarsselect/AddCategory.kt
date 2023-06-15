package com.example.prototypedesigncarsselect

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prototypedesigncarsselect.databinding.ActivityAddCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddCategory : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityAddCategoryBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click for submit button
        binding.submitBtn.setOnClickListener {
            validateCategory()
            val intent = Intent(this, ViewCars::class.java)
            startActivity(intent)
        }

    }

    //variable to store category
    private var category = " "

    private fun validateCategory() {
        //get category
        category = binding.addCat.text.toString().trim()

        //validate category
        if (category.isEmpty()) {
            Toast.makeText(this, "Please enter category.", Toast.LENGTH_SHORT).show()
        } else {
            addCategoryFirebase()

        }
    }

    private fun addCategoryFirebase() {
        //show progress dialog
        progressDialog.show()

        //getting timestamp
        val timeStamp = System.currentTimeMillis()

        //data setup to add in database
        val hashMap = HashMap<String, Any?>()
        hashMap["id"] = "$timeStamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timeStamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        //add data to firebase
        val reference = FirebaseDatabase.getInstance().getReference("Cars Categories")
        reference.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //added successfully
                progressDialog.dismiss()
                Toast.makeText(this, "Category Added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Failed to add category due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
}
