package pt.ola.online_classes_app.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.model.Subject
import pt.ola.online_classes_app.professor.ClassInfo
import pt.ola.online_classes_app.professor.ClassInfoAdapter

class student_courses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_courses)

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/subjects/",
            null,
            Response.Listener { response ->
                val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val user_id = sharedPref.getInt("user_id", -1)
                var subjectList = ArrayList<Subject>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val id = item.getInt("id");
                    val subject_name= item.getString("name")
                    val teacher_id= item.getInt("teacher_id")
                    val cur_class = Subject(
                        id = id,
                        name = subject_name,
                        teacher_id = teacher_id
                    )
                    subjectList.add(cur_class)


                }
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = StudentCoursesAdapter(this, subjectList)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)




        val homeButton = findViewById<Button>(R.id.home_btn)
        val classesButton = findViewById<Button>(R.id.classes_btn)

        homeButton.setOnClickListener {
            val intent = Intent(this, student_dashboard::class.java)
            startActivity(intent)
        }

        classesButton.setOnClickListener {
            val intent = Intent(this, student_class_schedule::class.java)
            startActivity(intent)
        }
    }
}