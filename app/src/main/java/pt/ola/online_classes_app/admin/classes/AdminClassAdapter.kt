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
        holder.classDate.text = classItem.formattedDate
        holder.classTime.text = classItem.formattedTime
        holder.classRoom.text = classItem.classRoom

        holder.btnRemove.setOnClickListener {
            onRemoveClick(classItem)
        }
    }

    override fun getItemCount(): Int = classList.size
}
