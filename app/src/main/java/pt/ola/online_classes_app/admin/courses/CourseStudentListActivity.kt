package pt.ola.online_classes_app.admin.courses

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.admin.students.StudentInfo
import pt.ola.online_classes_app.admin.students.StudentListAdapter

class CourseStudentListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentListAdapter
    private val studentList = mutableListOf<StudentInfo>()
    private val enrollmentMap = mutableMapOf<Int, Int>() // studentId -> enrollmentId

    private var subjectId: Int = 0 // Should be passed via intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_student_list)

        subjectId = intent.getIntExtra("subjectId", 0)


        recyclerView = findViewById(R.id.studentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = StudentListAdapter(
            studentList,
            onEnrollClick = { student -> enrollStudentAndUpdateUI(student, subjectId) },
            onUnenrollClick = { student ->
                val enrollmentId = enrollmentMap[student.id] ?: return@StudentListAdapter
                unenrollStudentAndUpdateUI(student, enrollmentId)
            }
        )

        recyclerView.adapter = adapter

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fetchAllStudents()
    }

    private fun fetchAllStudents() {
        val url = "http://10.0.2.2:8000/users/"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                parseStudentList(response)
                fetchAndMarkEnrolledStudents(subjectId)
            },
            { error ->
                Log.e("API", "Error fetching students: ${error.message}")
            })

        Volley.newRequestQueue(this).add(request)
    }

    private fun parseStudentList(response: JSONArray) {
        studentList.clear()
        for (i in 0 until response.length()) {
            val obj = response.getJSONObject(i)
            val role = obj.getString("role")
            if (role == "student") {
                val student = StudentInfo(
                    name = obj.getString("name"),
                    email = obj.getString("email"),
                    password = obj.getString("password"),
                    role = role,
                    id = obj.getInt("id"),
                    isEnrolled = false
                )
                studentList.add(student)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun fetchAndMarkEnrolledStudents(subjectId: Int) {
        val url = "http://10.0.2.2:8000/enrollments/"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val studentObj = obj.getJSONObject("student")
                    val subjectObj = obj.getJSONObject("subject")

                    val studentIdFromResponse = studentObj.getInt("id")
                    val subjectIdFromResponse = subjectObj.getInt("id")

                    if (subjectIdFromResponse == subjectId) {
                        val enrollmentId = obj.getInt("id")
                        enrollmentMap[studentIdFromResponse] = enrollmentId
                        studentList.find { it.id == studentIdFromResponse }?.isEnrolled = true
                    }
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("API", "Error fetching enrollments: ${error.message}")
            })

        Volley.newRequestQueue(this).add(request)
    }


    private fun enrollStudentAndUpdateUI(student: StudentInfo, subjectId: Int) {
        val url = "http://10.0.2.2:8000/enrollments/"
        val json = org.json.JSONObject().apply {
            put("student_id", student.id)
            put("subject_id", subjectId)
        }
        Log.d("API", "Enroll JSON: $json")

        val request = object : com.android.volley.toolbox.JsonObjectRequest(
            Method.POST, url, json,
            { response ->
                student.isEnrolled = true
                val enrollmentId = response.getInt("id")
                enrollmentMap[student.id] = enrollmentId
                adapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("API", "Enroll failed: ${error.message}")
            }) {}

        Volley.newRequestQueue(this).add(request)
    }

    private fun unenrollStudentAndUpdateUI(student: StudentInfo, enrollmentId: Int) {
        val url = "http://10.0.2.2:8000/enrollments/?enrollment_id=$enrollmentId"

        val request = object : com.android.volley.toolbox.StringRequest(
            Method.DELETE, url,
            {
                student.isEnrolled = false
                enrollmentMap.remove(student.id)
                adapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("API", "Unenroll failed: ${error.message}")
            }) {}

        Volley.newRequestQueue(this).add(request)
    }
}
