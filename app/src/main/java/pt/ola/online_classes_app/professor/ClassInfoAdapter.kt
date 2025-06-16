package pt.ola.online_classes_app.professor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.student.ClassInfo

class ClassInfoAdapter(
    private val context: Context,
    private val classList: List<ClassInfo>
) : RecyclerView.Adapter<ClassInfoAdapter.ClassInfoViewHolder>() {

    class ClassInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val classTime: TextView = itemView.findViewById(R.id.class_time)
        val classRoom: TextView = itemView.findViewById(R.id.class_room)
        val itemButton: Button = itemView.findViewById(R.id.item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.professor_item_class_info, parent, false)
        return ClassInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassInfoViewHolder, position: Int) {
        val classInfo = classList[position]
        holder.courseName.text = classInfo.courseName
        holder.classTime.text = classInfo.classTime
        holder.classRoom.text = classInfo.classRoom
        holder.itemButton.setOnClickListener {
            // Pass parameters to professor_check_presences
            val intent = Intent(context, professor_check_presences::class.java).apply {
                putExtra("courseName", classInfo.courseName)
                putExtra("classTime", classInfo.classTime)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = classList.size
}
