package com.example.prototypedesigncarsselect


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.prototypedesigncarsselect.databinding.ActivityViewCarsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewCars : AppCompatActivity() {
    private lateinit var binding: ActivityViewCarsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    //arraylist to hold categories
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    //adapter
    private lateinit var adapterCategory : AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCarsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()
        binding.categoriesRV.setOnClickListener {

            val intent = Intent(this, Archivements::class.java)
            startActivity(intent)
        }

        //search



        binding.addCatBtn.setOnClickListener {

            val intent = Intent(this, AddCategory::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {

            val intent = Intent(this, YourChoice::class.java)
            startActivity(intent)
        }
    }

    private fun loadCategories() {
        //init arrayList
        categoryArrayList = ArrayList()

        //get all categories from firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Cars Categories")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting to add data into it
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    //get data as model
                    val model = ds.getValue(ModelCategory::class.java)

                    //add to arraylist
                    categoryArrayList.add(model!!)

                }
                //setup adapter
                adapterCategory = AdapterCategory(this@ViewCars, categoryArrayList)
                //set adapter to recyclerview
                binding.categoriesRV.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}