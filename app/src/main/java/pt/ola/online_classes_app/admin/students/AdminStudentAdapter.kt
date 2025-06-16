package pt.ola.online_classes_app.admin.students

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.admin.students.ClassInfoPresences

class AdminStudentAdapter(
    private val context: Context,
    private val studentList: List<ClassInfoPresences>,
    private val onEditClick: (ClassInfoPresences) -> Unit,
    private val onRemoveClick: (ClassInfoPresences) -> Unit
) : RecyclerView.Adapter<AdminStudentAdapter.AdminStudentViewHolder>() {


    class AdminStudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentEmail: TextView = itemView.findViewById(R.id.student_email)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnRemove: Button = itemView.findViewById(R.id.btn_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminStudentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.admin_item_class_info_presences, parent, false)
        return AdminStudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminStudentViewHolder, position: Int) {
        val classInfoPresences = studentList[position]

        holder.studentName.text = classInfoPresences.studentName
        holder.studentEmail.text = classInfoPresences.studentEmail

        holder.btnEdit.setOnClickListener {
            onEditClick(classInfoPresences)
        }

        holder.btnRemove.setOnClickListener {
            onRemoveClick(classInfoPresences)
        }
    }

    // Total number of items
    override fun getItemCount(): Int = studentList.size
}
