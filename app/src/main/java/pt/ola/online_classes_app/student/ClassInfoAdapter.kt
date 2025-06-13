package pt.ola.online_classes_app.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class ClassInfoAdapter(private val classList: List<ClassInfo>) :
    RecyclerView.Adapter<ClassInfoAdapter.ClassInfoViewHolder>() {

    class ClassInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val classTime: TextView = itemView.findViewById(R.id.class_time)
        val classRoom: TextView = itemView.findViewById(R.id.class_room)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item_class_info, parent, false)
        return ClassInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassInfoViewHolder, position: Int) {
        val classInfo = classList[position]
        holder.courseName.text = classInfo.courseName
        holder.classTime.text = classInfo.classTime
        holder.classRoom.text = classInfo.classRoom
    }

    override fun getItemCount(): Int = classList.size
}