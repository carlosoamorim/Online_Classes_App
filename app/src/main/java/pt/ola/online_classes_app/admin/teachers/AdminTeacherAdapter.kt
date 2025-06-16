package pt.ola.online_classes_app.admin.teachers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class AdminTeacherAdapter(
    private val context: Context,
    private val teacherList: List<TeacherInfo>,
    //private val onEditClick: (TeacherInfo) -> Unit,
    private val onRemoveClick: (TeacherInfo) -> Unit
) : RecyclerView.Adapter<AdminTeacherAdapter.TeacherViewHolder>() {

    class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teacherName: TextView = itemView.findViewById(R.id.teacher_name)
        val teacherEmail: TextView = itemView.findViewById(R.id.teacher_email)
        //val btnEdit: Button = itemView.findViewById(R.id.btn_edit_teacher)
        val btnRemove: Button = itemView.findViewById(R.id.btn_remove_teacher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.admin_item_teacher, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = teacherList[position]

        holder.teacherName.text = "Name: ${teacher.name}"
        holder.teacherEmail.text = "Email: ${teacher.email}"

        //holder.btnEdit.setOnClickListener { onEditClick(teacher) }
        holder.btnRemove.setOnClickListener { onRemoveClick(teacher) }
    }

    override fun getItemCount(): Int = teacherList.size
}
