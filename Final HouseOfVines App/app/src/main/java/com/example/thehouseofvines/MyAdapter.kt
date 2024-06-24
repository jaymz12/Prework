package com.example.thehouseofvines.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.thehouseofvines.R
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.thehouseofvines.Signup
import com.example.thehouseofvines.dataclass.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var databaseReference: DatabaseReference
private val auth: FirebaseAuth = FirebaseAuth.getInstance()


class MyAdapter(val c:Context,val userList:ArrayList<Category>):RecyclerView.Adapter<MyAdapter.UserViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(categoryName: String, categoryGoal: String)
    }
    private var onCategoryClickListener: OnCategoryClickListener? = null

    inner class UserViewHolder(val v: View):RecyclerView.ViewHolder(v){
        var name: TextView
        var mbNum:TextView
        var mMenus: ImageView
        var category: CardView

        init {
            name = v.findViewById<TextView>(R.id.mTitle)
            mbNum = v.findViewById<TextView>(R.id.mSubTitle)
            mMenus = v.findViewById(R.id.mMenus)
            category = v.findViewById<androidx.cardview.widget.CardView>(R.id.Category)
            mMenus.setOnClickListener { popupMenus(it) }

            // Set click listener for the view
            itemView.setOnClickListener(){

            }
        }



        private fun popupMenus(v:View) {
            val position = userList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val v = LayoutInflater.from(c).inflate(R.layout.activity_add_category_format,null)
                        val name = v.findViewById<EditText>(R.id.CategoryName)
                        val number = v.findViewById<EditText>(R.id.CategoryGoal)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                    dialog,_->
                                position.categoryName = name.text.toString()
                                position.categoryGoal = number.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(c,"Category Information is Edited",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()

                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c,"Deleted this Information",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.list_category,parent,false)
        var progressBar = v.findViewById<ProgressBar>(R.id.progressBar)
        var progressPercentage: Int = 0
        // Getter
        fun getMyNumber(): Int {
            return progressPercentage
        }

        // Setter
        fun setMyNumber(value: Int) {
            progressPercentage = value
        }

        val usernameInFirebase = auth.currentUser
        val userID = usernameInFirebase?.email?.replace(".", "")
        val username = userID.toString()
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wineapplication-a9530-default-rtdb.firebaseio.com/")
        val jayUserRef = databaseReference.child("users").child(username)
        val categoriesRef = jayUserRef.child("wine categories")

        categoriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnapshot in snapshot.children) {
                    val categoryGoal = categorySnapshot.child("categoryGoal").value.toString()
                    val goal = categoryGoal.toInt()

                    for (items in categorySnapshot.children){
                        val itemsCount = items.childrenCount.toInt()
                        val progressPercentage = (itemsCount.toFloat() / goal.toFloat() * 100).toInt()
                        setMyNumber(progressPercentage)
                    }
                    progressBar.progress = getMyNumber()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newList = userList[position]
        holder.name.text = newList.categoryName
        holder.mbNum.text = newList.categoryGoal
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    fun setOnCategoryClickListener(listener: OnCategoryClickListener) {
        this.onCategoryClickListener = listener
    }

}