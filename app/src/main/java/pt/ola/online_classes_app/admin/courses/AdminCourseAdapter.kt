package pt.ola.online_classes_app.admin.courses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class AdminCourseAdapter(
    private val context: Context,
    private val courseList: MutableList<CourseInfo>,
    private val onViewStudentsClick: (CourseInfo) -> Unit,
    //private val onEditClick: (CourseInfo) -> Unit,
    private val onRemoveClick: (CourseInfo) -> Unit
) : RecyclerView.Adapter<AdminCourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       // var courseId: TextView = itemView.findViewById(R.id.course_id)
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val teacherName: TextView = itemView.findViewById(R.id.teacher_name)
        val btnViewStudents: Button = itemView.findViewById(R.id.btn_view_students)
        //val btnEdit: Button = itemView.findViewById(R.id.btn_edit_course)
        val btnRemove: Button = itemView.findViewById(R.id.btn_remove_course)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.admin_item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.courseName.text = course.courseName
        holder.teacherName.text = "Prof.: ${course.teacherName}"

        holder.btnViewStudents.setOnClickListener {
            onViewStudentsClick(course)
        }

        //holder.btnEdit.setOnClickListener { onEditClick(course) }
        holder.btnRemove.setOnClickListener { onRemoveClick(course) }
    }

    override fun getItemCount(): Int = courseList.size
}