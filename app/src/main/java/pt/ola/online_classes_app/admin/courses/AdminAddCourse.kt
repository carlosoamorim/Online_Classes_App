//package pt.ola.online_classes_app.admin.courses
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageButton
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.android.volley.Request
//import com.android.volley.toolbox.JsonArrayRequest
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.google.gson.Gson
//import pt.ola.online_classes_app.R
//import pt.ola.online_classes_app.admin.classes.AddOrEditClassActivity
//import java.net.URLEncoder
//
//class AdminAddCourse : AppCompatActivity() {
//
//    private lateinit var btnAddCourse: Button
//    private lateinit var courseRecyclerView: RecyclerView
//    private lateinit var courseAdapter: AdminCourseAdapter
//
//    private val courseList = mutableListOf<CourseInfo>()
//
//    private val addCourseLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let { handleActivityResult(it) }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_admin_add_course)
//
//        btnAddCourse = findViewById(R.id.btnAddCourse)
//        courseRecyclerView = findViewById(R.id.courseRecyclerView)
//
//        loadCourses()
//
//        courseAdapter = AdminCourseAdapter(
//            context = this,
//            courseList = courseList,
//            onViewStudentsClick = { openStudentListActivity(it) },
//            onRemoveClick = { removeCourse(it) }
//
//            // onEditClick = { openEditCourseActivity(it) }
//        )
//
//        courseRecyclerView.layoutManager = LinearLayoutManager(this)
//        courseRecyclerView.adapter = courseAdapter
//
//        btnAddCourse.setOnClickListener {
//            openAddCourseActivity()
//        }
//
////        val gson = Gson()
////        val jsonString = gson.toJson(courseList)
////        val intent = Intent(this, AddOrEditClassActivity::class.java)
////        intent.putExtra("courseListJson", jsonString)
////        startActivity(intent)
//
//
//        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
//    }
//
//    private fun loadCourses() {
//        val url = "http://10.0.2.2:8000/subjects/"
//        val queue = Volley.newRequestQueue(this)
//
//        val request = JsonArrayRequest(
//            Request.Method.GET, url, null,
//            { response ->
//                courseList.clear()
//                for (i in 0 until response.length()) {
//                    val obj = response.getJSONObject(i)
//                    val id = obj.getInt("id")
//                    val name = obj.getString("name")
//                    val teacherObj = obj.getJSONObject("teacher")
//                    val teacherName = teacherObj.getString("name")
//
//                    courseList.add(CourseInfo(id, name, teacherName))
//                }
//                courseAdapter.notifyDataSetChanged()
//            },
//            { error ->
//                error.printStackTrace()
//                Toast.makeText(this, "Failed to load courses", Toast.LENGTH_SHORT).show()
//            })
//
//        queue.add(request)
//    }
//
//    private fun openAddCourseActivity() {
//        val intent = Intent(this, AddOrEditCourse::class.java)
//        // If needed: intent.putExtra("teacherId", yourTeacherId)
//        addCourseLauncher.launch(intent)
//    }
//
//    private fun openStudentListActivity(course: CourseInfo) {
//        val intent = Intent(this, CourseStudentListActivity::class.java).apply {
//            putStringArrayListExtra("studentList", ArrayList(course.studentList))
//            putExtra("subjectId", course.courseId)
//        }
//        startActivity(intent)
//    }
//
//    private fun removeCourse(course: CourseInfo) {
//        val encodedName = URLEncoder.encode(course.courseName, "UTF-8")
//        val url = "http://10.0.2.2:8000/subjects/?name=$encodedName"
//        val queue = Volley.newRequestQueue(this)
//
//        val request = StringRequest(Request.Method.DELETE, url,
//            {
//                val index = courseList.indexOf(course)
//                if (index != -1) {
//                    courseList.removeAt(index)
//                    courseAdapter.notifyItemRemoved(index)
//                }
//                Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
//            },
//            {
//                it.printStackTrace()
//                Toast.makeText(this, "Failed to delete course", Toast.LENGTH_SHORT).show()
//            })
//
//        queue.add(request)
//    }
//
//    private fun handleActivityResult(data: Intent) {
//        val courseId = data.getIntExtra("courseId", -1)
//        val name = data.getStringExtra("courseName") ?: return
//        val teacher = data.getStringExtra("teacherName") ?: return
//        val newCourse = CourseInfo( courseId, name, teacher)
//
//        courseList.add(newCourse)
//        courseAdapter.notifyItemInserted(courseList.size - 1)
//    }
//}
package pt.ola.online_classes_app.admin.courses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pt.ola.online_classes_app.R
import java.net.URLEncoder

class AdminAddCourse : AppCompatActivity() {

    private lateinit var btnAddCourse: Button
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var courseAdapter: AdminCourseAdapter

    private val courseList = mutableListOf<CourseInfo>()

    private val addCourseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleActivityResult(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_course)

        btnAddCourse = findViewById(R.id.btnAddCourse)
        courseRecyclerView = findViewById(R.id.courseRecyclerView)

        loadCourses()

        courseAdapter = AdminCourseAdapter(
            context = this,
            courseList = courseList,
            onViewStudentsClick = { openStudentListActivity(it) },
            onRemoveClick = { removeCourse(it) }
        )

        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.adapter = courseAdapter

        btnAddCourse.setOnClickListener {
            openAddCourseActivity()
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadCourses() {
        val url = "http://10.0.2.2:8000/subjects/"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                courseList.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val id = obj.getInt("id")
                    val name = obj.getString("name")
                    val teacherObj = obj.getJSONObject("teacher")
                    val teacherName = teacherObj.getString("name")

                    courseList.add(CourseInfo(id, name, teacherName))
                }
                courseAdapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to load courses", Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }

    private fun openAddCourseActivity() {
        val intent = Intent(this, AddOrEditCourse::class.java)
        addCourseLauncher.launch(intent)
    }

    private fun openStudentListActivity(course: CourseInfo) {
        val intent = Intent(this, CourseStudentListActivity::class.java).apply {
            putStringArrayListExtra("studentList", ArrayList(course.studentList))
            putExtra("subjectId", course.courseId)
        }
        startActivity(intent)
    }

    private fun removeCourse(course: CourseInfo) {
        val encodedName = URLEncoder.encode(course.courseName, "UTF-8")
        val url = "http://10.0.2.2:8000/subjects/?name=$encodedName"
        val queue = Volley.newRequestQueue(this)

        val request = StringRequest(Request.Method.DELETE, url,
            {
                val index = courseList.indexOf(course)
                if (index != -1) {
                    courseList.removeAt(index)
                    courseAdapter.notifyItemRemoved(index)
                }
                Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
            },
            {
                it.printStackTrace()
                Toast.makeText(this, "Failed to delete course", Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }

    private fun handleActivityResult(data: Intent) {
        val name = data.getStringExtra("courseName") ?: return
        val teacherId = data.getIntExtra("teacherId", -1)
        if (teacherId == -1) {
            Toast.makeText(this, "Invalid teacher ID", Toast.LENGTH_SHORT).show()
            return
        }

        // POST new course to backend
        postNewCourse(name, teacherId) { createdCourse ->
            courseList.add(createdCourse)
            courseAdapter.notifyItemInserted(courseList.size - 1)
        }
    }

    private fun postNewCourse(courseName: String, teacherId: Int, onSuccess: (CourseInfo) -> Unit) {
        val url = "http://10.0.2.2:8000/subjects/"
        val jsonBody = JSONObject().apply {
            put("name", courseName)
            put("teacher_id", teacherId)
        }

        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                val id = response.getInt("id")
                val name = response.getString("name")
                val teacherObj = response.getJSONObject("teacher")
                val teacherName = teacherObj.getString("name")

                val createdCourse = CourseInfo(id, name, teacherName)
                onSuccess(createdCourse)
                Toast.makeText(this, "Course created: $name", Toast.LENGTH_SHORT).show()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to create course: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }
}
