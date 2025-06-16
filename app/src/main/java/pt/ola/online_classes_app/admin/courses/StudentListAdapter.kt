package pt.ola.online_classes_app.admin.students

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class StudentListAdapter(
    private val studentList: MutableList<ClassInfoPresences>,
    private val onEnrollClick: (ClassInfoPresences) -> Unit,
    private val onUnenrollClick: (ClassInfoPresences) -> Unit
) : RecyclerView.Adapter<StudentListAdapter.PresenceViewHolder>() {

    inner class PresenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.studentNameTextView)
        val student_email: TextView = itemView.findViewById(R.id.student_email)
        val enrollButton: Button = itemView.findViewById(R.id.enrollButton)
        val unenrollButton: Button = itemView.findViewById(R.id.unenrollButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresenceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_course, parent, false)
        return PresenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresenceViewHolder, position: Int) {
        val student = studentList[position]

        holder.nameTextView.text = student.studentName
        holder.student_email.text = student.studentEmail

        if (student.isEnrolled) {
            holder.enrollButton.visibility = View.GONE
            holder.unenrollButton.visibility = View.VISIBLE
        } else {
            holder.enrollButton.visibility = View.VISIBLE
            holder.unenrollButton.visibility = View.GONE
        }

        holder.enrollButton.setOnClickListener {
            onEnrollClick(student)
            notifyItemChanged(position)
        }

        holder.unenrollButton.setOnClickListener {
            onUnenrollClick(student)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = studentList.size
}
