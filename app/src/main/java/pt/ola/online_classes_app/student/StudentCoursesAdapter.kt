package pt.ola.online_classes_app.student

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class StudentCoursesAdapter(
    private val context: Context,
    private val courseNames: List<String>
) : RecyclerView.Adapter<StudentCoursesAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTextView: TextView = itemView.findViewById(R.id.courseNameTextView)
        val enrollButton: Button = itemView.findViewById(R.id.enrollButton)
        val unsubscribeButton: Button = itemView.findViewById(R.id.unsubscribeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val courseName = courseNames[position]
        holder.courseNameTextView.text = courseName

        holder.enrollButton.setOnClickListener {
            holder.enrollButton.visibility = View.GONE
            holder.unsubscribeButton.visibility = View.VISIBLE
            Toast.makeText(context, "Enrolled in $courseName", Toast.LENGTH_SHORT).show()
        }

        holder.unsubscribeButton.setOnClickListener {
            holder.unsubscribeButton.visibility = View.GONE
            holder.enrollButton.visibility = View.VISIBLE
            Toast.makeText(context, "Unsubscribed from $courseName", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = courseNames.size
}