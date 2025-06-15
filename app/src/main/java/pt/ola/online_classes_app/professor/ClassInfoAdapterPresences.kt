package pt.ola.online_classes_app.professor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.student.ClassInfo
import pt.ola.online_classes_app.student.student_dashboard

class ClassInfoAdapterPresences(
    private val context: Context,
    private val classList: List<ClassInfoPresences>
) : RecyclerView.Adapter<ClassInfoAdapterPresences.ClassInfoPresencesViewHolder>() {

    class ClassInfoPresencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentEmail: TextView = itemView.findViewById(R.id.student_email)
        val itemButton: Button = itemView.findViewById(R.id.item_button_present)
        val itemButtonAbsent: Button = itemView.findViewById(R.id.item_button_absent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassInfoPresencesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.professor_students, parent, false)
        return ClassInfoPresencesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassInfoPresencesViewHolder, position: Int) {
        val classInfo = classList[position]
        holder.studentName.text = classInfo.studentName // Replace with student name
        holder.studentEmail.text = classInfo.studentEmail // Replace with student email
        holder.itemButton.setOnClickListener {
            val intent = Intent(context, professor_check_presences::class.java).apply {
                putExtra("studentName", classInfo.studentName) // Replace with student name
                putExtra("studentEmail", classInfo.studentEmail) // Replace with student email
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = classList.size
}
