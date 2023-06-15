package com.example.prototypedesigncarsselect

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypedesigncarsselect.databinding.RowCategoryBinding
import com.google.firebase.database.FirebaseDatabase

class AdapterCategory : RecyclerView.Adapter<AdapterCategory.HolderCategory>, Filterable{


    private val context : Context
    public var categoryArrayList:ArrayList<ModelCategory>
    private var filterList : ArrayList<ModelCategory>


    private var filter : FilterCategory? = null
    //variable for binding
    private lateinit var binding : RowCategoryBinding


    //constructor
    constructor(context: Context, categoryArrayList: ArrayList<ModelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        //inflate/bind row_category.xml
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        //returns number of array size
        return categoryArrayList.size

    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        //getting data
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timeStamp = model.timeStamp

        //setting the data
        holder.categoryTv.text = category

        //handle click for the delete category button
        holder.deleteButton.setOnClickListener{
            //confirmation of deletion
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete the category?")
                .setPositiveButton("Confirm"){a, d->
                    Toast.makeText(context,"Category is being deleted.", Toast.LENGTH_SHORT).show()
                    deleteCategory(model, holder)

                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()

                }
                .show()

        }


    }

    private fun deleteCategory(model: ModelCategory, holder: HolderCategory) {
        //get id of category to delete
        val id = model.id
        //accessing firebase to get category ID
        val ref = FirebaseDatabase.getInstance().getReference("Recipe Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Category has been deleted.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,"Unable to delete category due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }


    //view holder class to initialise UI Views for row_category.xml
    inner class HolderCategory(itemView : View) : RecyclerView.ViewHolder(itemView){

        //initialise ui views
        var categoryTv:TextView =binding.categoryTV
        var deleteButton : ImageButton = binding.deleteBtn


    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterCategory(filterList, this)

        }
        return filter as FilterCategory
    }


}