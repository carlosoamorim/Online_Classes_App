package pt.ola.online_classes_app.admin.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.professor.ClassInfo

class AdminClassAdapter(
    private val context: Context,
    private val classList: List<ClassInfo>,
    private val onEditClick: (ClassInfo) -> Unit,
    private val onRemoveClick: (ClassInfo) -> Unit
) : RecyclerView.Adapter<AdminClassAdapter.AdminClassViewHolder>() {

    // ViewHolder to hold references to each item's views
    class AdminClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val classTime: TextView = itemView.findViewById(R.id.class_time)
        val classRoom: TextView = itemView.findViewById(R.id.class_room)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnRemove: Button = itemView.findViewById(R.id.btn_remove)
    }

    // Inflate the item layout and create a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminClassViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.admin_item_class_info, parent, false)
        return AdminClassViewHolder(view)
    }

    // Bind data to the item view
    override fun onBindViewHolder(holder: AdminClassViewHolder, position: Int) {
        val classInfo = classList[position]

        holder.courseName.text = classInfo.courseName
        holder.classTime.text = classInfo.classTime
        holder.classRoom.text = classInfo.classRoom

        holder.btnEdit.setOnClickListener {
            onEditClick(classInfo)
        }

        holder.btnRemove.setOnClickListener {
            onRemoveClick(classInfo)
        }
    }

    // Total number of items
    override fun getItemCount(): Int = classList.size
}
