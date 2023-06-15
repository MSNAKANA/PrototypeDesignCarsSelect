package com.example.prototypedesigncarsselect


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.prototypedesigncarsselect.databinding.ActivityYourChoiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.storage.FirebaseStorage


class YourChoice : AppCompatActivity() {

    //view binding for activity
    private lateinit var binding: ActivityYourChoiceBinding

    //firebase setup
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog


    //arrayList to hold pdf categories
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    //tag
    private val TAG = "CARS_ADD_TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityYourChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase initialization
        firebaseAuth = FirebaseAuth.getInstance()
        loadCars()
        //set up progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait.")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click for show recipe category pick dialog
        binding.setgoalsbtn.setOnClickListener {

            val intent = Intent(this, SetGoals::class.java)
            startActivity(intent)
        }
        binding.pickCat.setOnClickListener {
            categoryPickDialog()
        }

        //handle click for upload btn

        binding.submitBtn.setOnClickListener {


            validateData()

        }

        //handle click for selecting image
        binding.captureBtn.setOnClickListener {
            selectImage()
        }


    }

    private fun selectImage() {
        // Creating AlertDialog
        val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery")
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        myAlertDialog.setTitle("Select Image")
        myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
            when {
                choice[item] == "Choose from Gallery" -> {
                    val pickFromGallery = Intent(
                        Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    pickFromGallery.type = "image/*"

                    startActivityForResult(pickFromGallery, 1)


                }
                choice[item] == "Take Photo" -> {
                    val cameraPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraPicture, 0)

                }

            }
        })
        myAlertDialog.show()
    }


    //variables to store title, year and category
    private var name = ""
    private var year = ""
    private var category = ""

    private fun validateData() {
        //validating data
        Log.d(TAG, "validateData: validating data")

        //retrieving data
        name = binding.recipeName.text.toString().trim()
        year = binding.recipeDescription.text.toString().trim()
        category = binding.pickCat.text.toString().trim()

        //validate data
        if (name.isEmpty()) {

            Toast.makeText(this, "Enter cars name:...", Toast.LENGTH_SHORT).show()
        } else if (year.isEmpty()) {

            Toast.makeText(this, "Enter cars description:...", Toast.LENGTH_SHORT).show()
        } else if (category.isEmpty()) {

            Toast.makeText(this, "Pick cars category...", Toast.LENGTH_SHORT).show()
        } else {
            uploadCarsToStorage()
        }
    }

    private fun uploadCarsToStorage() {
        //upload data to the firebase
        Log.d(TAG, "uploadCarsToStorage: uploading cars to storage...")

        //timestamp
        val timeStamp = System.currentTimeMillis()

        //user id of current user
        val uid = firebaseAuth.uid

        //setup data to store in database
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timeStamp"
        hashMap["name"] = "$name"
        hashMap["year"] = "$year"
        hashMap["categoryId"] = "$category"
        hashMap["timestamp"] = timeStamp

        //database reference
        val ref = FirebaseDatabase.getInstance().getReference("Cars")
        ref.child("$timeStamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadCarsToStorage: uploaded to database")
                progressDialog.dismiss()
                Toast.makeText(this, "Uploaded.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "uploadCarsToStorage: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "failed to upload due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()

            }


    }

    private fun loadCars() {
        Log.d(TAG, "loadCarsCategories: Loading Cars Categories")
        //
        categoryArrayList = ArrayList()

        //database reference to load recipes
        val ref = FirebaseDatabase.getInstance().getReference("Cars Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before adding data
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    //get data
                    val model = ds.getValue(ModelCategory::class.java)

                    //add to arraylist
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //variable for selected recipe category id and title
    private var selectedRecipeCatId = ""
    private var selectedRecipeCatTitle = ""

    private fun categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: Showing cars category pick dialog")


        //get string array of CAR from arraylist
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for (rec in categoryArrayList.indices) {
            categoriesArray[rec] = categoryArrayList[rec].category

        }

        //alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Cars Category")
            .setItems(categoriesArray) { dialog, which ->
                //deals with item clicks and retrieves clicked items
                selectedRecipeCatTitle = categoryArrayList[which].category
                selectedRecipeCatId = categoryArrayList[which].id

                //set recipe category to textview
                binding.pickCat.text = selectedRecipeCatTitle

                Log.d(TAG, "categoryPickDialog: Selected Cars Category ID: $selectedRecipeCatId")
                Log.d(
                    TAG,
                    "categoryPickDialog: Selected Cars Category Title: $selectedRecipeCatTitle"
                )

            }
            .show()
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // For loading Image
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val imageSelected = data.extras!!["data"] as Bitmap?
                    binding.seeImage.setImageBitmap(imageSelected)
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val imageSelected = data.data
                    val pathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (imageSelected != null) {
                        val myCursor = contentResolver.query(
                            imageSelected,
                            pathColumn, null, null, null
                        )
                        if (myCursor != null) {
                            myCursor.moveToFirst()
                            val columnIndex = myCursor.getColumnIndex(pathColumn[0])
                            val picturePath = myCursor.getString(columnIndex)
                            binding.seeImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                            myCursor.close()
                        }
                    }
                }
            }
        }
    }
}
