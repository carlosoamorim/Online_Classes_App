package pt.ola.online_classes_app.admin.courses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pt.ola.online_classes_app.R

class AddOrEditCourse : AppCompatActivity() {

    private lateinit var editCourseName: EditText
    private lateinit var editTeacherName: EditText
    private var courseId = -1
    private var isEditMode = false

    private fun fetchTeachersAndCreateCourse() {
        val url = "http://10.0.2.2:8000/users/"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            for (i in 0 until response.length()) {
                val user = response.getJSONObject(i)
                if (user.getString("role") == "teacher") {
                    val teacherId = user.getInt("id")
                    val teacherName = user.getString("name")
                    createCourse(teacherId, teacherName)
                    return@JsonArrayRequest
                }
            }
            Toast.makeText(this, "No teachers found", Toast.LENGTH_SHORT).show()
        },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to fetch teachers", Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }

    private fun createCourse(teacherId: Int, teacherName: String) {
        val course = editCourseName.text.toString().trim()

        val url = "http://10.0.2.2:8000/subjects/"
        val jsonBody = JSONObject().apply {
            put("name", course)
            put("teacher_id", teacherId)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(this, "Course added!", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent().apply {
                    putExtra("courseName", course)
                    putExtra("teacherName", teacherName)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to add course: ${error.message}", Toast.LENGTH_SHORT).show()
            })


        Volley.newRequestQueue(this).add(request)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "AddOrEditCourse launched")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_course)

        editCourseName = findViewById(R.id.editCourseName)
        editTeacherName = findViewById(R.id.editTeacherName)
        val btnSave = findViewById<Button>(R.id.btnSaveCourse)

        if (intent.hasExtra("courseName")) {
            isEditMode = true
            courseId = intent.getIntExtra("courseId", -1)
            editCourseName.setText(intent.getStringExtra("courseName"))
            editTeacherName.setText(intent.getStringExtra("teacherName"))
        }

        btnSave.setOnClickListener {
            val enteredTeacherName = editTeacherName.text.toString().trim()
            if (enteredTeacherName.isEmpty()) {
                Toast.makeText(this, "Please enter a teacher name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val url = "http://10.0.2.2:8000/users/"
            val queue = Volley.newRequestQueue(this)

            val request = JsonArrayRequest(Request.Method.GET, url, null,
                { response ->
                    var matchedTeacherId: Int? = null

                    for (i in 0 until response.length()) {
                        val user = response.getJSONObject(i)
                        val role = user.getString("role")
                        val name = user.getString("name")
                        if (role == "teacher" && name.equals(enteredTeacherName, ignoreCase = true)) {
                            matchedTeacherId = user.getInt("id")
                            break
                        }
                    }

                    if (matchedTeacherId != null) {
                        createCourse(matchedTeacherId, enteredTeacherName)
                    } else {
                        Toast.makeText(this, "Teacher not found: $enteredTeacherName", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    error.printStackTrace()
                    Toast.makeText(this, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                })

            queue.add(request)
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}
