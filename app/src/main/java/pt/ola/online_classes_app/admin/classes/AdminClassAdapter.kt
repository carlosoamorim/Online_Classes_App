package pt.ola.online_classes_app.admin.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import android.content.Context
import android.widget.Button

//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import pt.ola.online_classes_app.R
//import pt.ola.online_classes_app.admin.classes.ClassInfo
//
//class AdminClassAdapter(
//    private val context: Context,
//    private val classList: List<ClassInfo>,
//    private val onEditClick: (ClassInfo) -> Unit,
//    private val onRemoveClick: (ClassInfo) -> Unit
//) : RecyclerView.Adapter<AdminClassAdapter.AdminClassViewHolder>() {
//
//    // ViewHolder to hold references to each item's views
//    class AdminClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val courseName: TextView = itemView.findViewById(R.id.course_name)
//        val classTime: TextView = itemView.findViewById(R.id.class_time)
//        val classRoom: TextView = itemView.findViewById(R.id.class_room)
//        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
//        val btnRemove: Button = itemView.findViewById(R.id.btn_remove)
//    }
//
//    // Inflate the item layout and create a ViewHolder
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminClassViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.admin_item_class_info, parent, false)
//        return AdminClassViewHolder(view)
//    }
//
//    // Bind data to the item view
//    override fun onBindViewHolder(holder: AdminClassViewHolder, position: Int) {
//        val classInfo = classList[position]
//
//        holder.courseName.text = classInfo.courseName
//        holder.classTime.text = "Time: ${classInfo.fullTime}"
//        holder.classRoom.text = "Room: ${classInfo.classRoom}"
//
//        holder.btnEdit.setOnClickListener {
//            onEditClick(classInfo)
//        }
//
//        holder.btnRemove.setOnClickListener {
//            onRemoveClick(classInfo)
//        }
//    }
//
//    // Total number of items
//    override fun getItemCount(): Int = classList.size
//}

class AdminClassAdapter(
    private val context: Context,
    private val classList: List<ClassInfo>,
    private val onRemoveClick: (ClassInfo) -> Unit
) : RecyclerView.Adapter<AdminClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.course_name)
        val classDate: TextView = view.findViewById(R.id.class_date)
        val classTime: TextView = view.findViewById(R.id.class_time)
        val classRoom: TextView = view.findViewById(R.id.class_room)
        val btnRemove: Button = view.findViewById(R.id.btn_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.admin_item_class_info, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classItem = classList[position]
        holder.courseName.text = classItem.courseName
        holder.classDate.text = classItem.formattedDate  // assuming your ClassInfo has formattedDate property
        holder.classTime.text = classItem.formattedTime  // assuming ClassInfo.formattedTime
        holder.classRoom.text = classItem.classRoom

        holder.btnRemove.setOnClickListener {
            onRemoveClick(classItem)
        }
    }

    override fun getItemCount(): Int = classList.size
}
