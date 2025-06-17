package pt.ola.online_classes_app.professor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import pt.ola.online_classes_app.R

class ClassInfoAdapterPresences(
    private val context: Context,
    private val classList: List<ClassInfoPresences>
) : RecyclerView.Adapter<ClassInfoAdapterPresences.ClassInfoPresencesViewHolder>() {

    class ClassInfoPresencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentEmail: TextView = itemView.findViewById(R.id.student_email)
        val presence: TextView = itemView.findViewById(R.id.student_presence)
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
        val attendance_id = classInfo.id
        holder.studentName.text = classInfo.studentName // Replace with student name
        holder.studentEmail.text = classInfo.studentEmail // Replace with student email
        if (classInfo.mark == true){
            holder.presence.text = "Present"}
        else {
            holder.presence.text = "Not present"
        }
        holder.itemButton.setOnClickListener {
            val requestQueue = Volley.newRequestQueue(context)
            val request = JsonArrayRequest(
                Request.Method.PUT,
                "http://10.0.2.2:8000/attendances/true/?attendance=$attendance_id",
                null,
                Response.Listener { response ->
                    var classList = ArrayList<ClassInfoPresences>()
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val id = item.getInt("id");
                        val cur_class_id = item.getInt("class_id")
                        val mark = item.getBoolean("mark")
                        val student_id = item.getInt("student_id")
                        val student_name= item.getJSONObject("student").getString("name")
                        val student_email= item.getJSONObject("student").getString("email")
                        if (classInfo.class_id == cur_class_id){
                            val cur_class = ClassInfoPresences(
                                id = id,
                                class_id = cur_class_id,
                                student_id = student_id,
                                studentName = student_name,
                                studentEmail = student_email,
                                mark = mark
                            )
                            classList.add(cur_class)
                        }
                    }

                },
                Response.ErrorListener { error ->
                    Toast.makeText(context, "Error: ${error.message}",
                        Toast.LENGTH_SHORT).show()
                }
            )

            requestQueue.add(request)
            if (context is Activity) {
                val intent = context.intent
                context.finish()
                context.startActivity(intent)
            }
        }
        holder.itemButtonAbsent.setOnClickListener {
            val requestQueue = Volley.newRequestQueue(context)
            val request = JsonArrayRequest(
                Request.Method.PUT,
                "http://10.0.2.2:8000/attendances/false/?attendance=$attendance_id",
                null,
                Response.Listener { response ->
                    var classList = ArrayList<ClassInfoPresences>()
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val id = item.getInt("id");
                        val cur_class_id = item.getInt("class_id")
                        val mark = item.getBoolean("mark")
                        val student_id = item.getInt("student_id")
                        val student_name= item.getJSONObject("student").getString("name")
                        val student_email= item.getJSONObject("student").getString("email")
                        if (classInfo.class_id == cur_class_id){
                            val cur_class = ClassInfoPresences(
                                id = id,
                                class_id = cur_class_id,
                                student_id = student_id,
                                studentName = student_name,
                                studentEmail = student_email,
                                mark = mark
                            )
                            classList.add(cur_class)
                        }
                    }

                },
                Response.ErrorListener { error ->
                    Toast.makeText(context, "Error: ${error.message}",
                        Toast.LENGTH_SHORT).show()
                }
            )

            requestQueue.add(request)
            if (context is Activity) {
                val intent = context.intent
                context.finish()
                context.startActivity(intent)
            }
        }
    }


    override fun getItemCount(): Int = classList.size
}
