package pt.ola.online_classes_app.student

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.model.Subject
import pt.ola.online_classes_app.professor.ClassInfoPresences

class StudentCoursesAdapter(
    private val context: Context,
    private val subjectList: List<Subject>
) : RecyclerView.Adapter<StudentCoursesAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTextView: TextView = itemView.findViewById(R.id.courseNameTextView)
        val enrollButton: Button = itemView.findViewById(R.id.enrollButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val courseName = subjectList[position].name
        holder.courseNameTextView.text = courseName

        holder.enrollButton.setOnClickListener {
            val enrollmentjson = JSONObject().apply {
                put("student_id", context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1));
                put("subject_id", subjectList[position].id)
            }
            val requestQueue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/enrollments/",
                enrollmentjson,
                Response.Listener { response ->
                    var classList = ArrayList<ClassInfoPresences>()
                    val item = response
                    val id = item.getInt("id");
                    val subject_id = item.getInt("subject_id")
                    val student_id = item.getInt("student_id")
                    val subject_name= item.getJSONObject("subject").getString("name")

                    Toast.makeText(context, "You have been enrolled in: ${subject_name}",
                        Toast.LENGTH_SHORT).show()

                },
                Response.ErrorListener { error ->
                    Toast.makeText(context, "Error: ${error.message}",
                        Toast.LENGTH_SHORT).show()
                }
            )

            requestQueue.add(request)
        }


    }

    override fun getItemCount(): Int = subjectList.size
}