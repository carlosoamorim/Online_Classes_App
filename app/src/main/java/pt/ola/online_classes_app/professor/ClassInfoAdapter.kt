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
import pt.ola.online_classes_app.professor.ClassInfo

class ClassInfoAdapter(
    private val context: Context,
    private val classList: List<ClassInfo>
) : RecyclerView.Adapter<ClassInfoAdapter.ClassInfoViewHolder>() {

    class ClassInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject_name: TextView = itemView.findViewById(R.id.course_name)
        val start_time: TextView = itemView.findViewById(R.id.class_time)
        val end_time: TextView = itemView.findViewById(R.id.class_time)
        val auditorium: TextView = itemView.findViewById(R.id.class_room)
        val itemButton: Button = itemView.findViewById(R.id.item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.professor_item_class_info, parent, false)
        return ClassInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassInfoViewHolder, position: Int) {
        val classInfo = classList[position]
        holder.subject_name.text = classInfo.subject_name
        holder.start_time.text = classInfo.start_time
        holder.end_time.text = classInfo.end_time
        holder.auditorium.text = classInfo.auditorium
        holder.itemButton.setOnClickListener {
            val intent = Intent(context, professor_check_presences::class.java).apply {
                putExtra("courseName", classInfo.subject_name)
                putExtra("classTime", classInfo.start_time)
                putExtra("auditorium", classInfo.auditorium)
                putExtra("class_id", classInfo.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = classList.size
}
