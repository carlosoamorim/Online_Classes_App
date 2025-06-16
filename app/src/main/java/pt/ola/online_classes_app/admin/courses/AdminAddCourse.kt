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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import pt.ola.online_classes_app.R

class AdminAddCourse : AppCompatActivity() {

    private lateinit var btnAddCourse: Button
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var courseAdapter: AdminCourseAdapter

    private val courseList = mutableListOf<CourseInfo>()

    private val addCourseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleActivityResult(it, isEdit = false) }
        }
    }

//    private val editCourseLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let { handleActivityResult(it, isEdit = true) }
//        }
//    }

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
            //onEditClick = { openEditCourseActivity(it) },
            onRemoveClick = { removeCourse(it) }
//            onViewStudentsClick = { course ->
//                val intent = Intent(this, CourseStudentListActivity::class.java).apply {
//                    putStringArrayListExtra("studentList", ArrayList(course.studentList))
//                }
//                startActivity(intent)
//            }
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
        }
        startActivity(intent)
    }


//    private fun openEditCourseActivity(course: CourseInfo) {
//        val intent = Intent(this, AddOrEditCourse::class.java).apply {
//            putExtra("courseId", course.courseId)
//
//            putExtra("courseName", course.courseName)
//            putExtra("teacherName", course.teacherName)
//        }
//        editCourseLauncher.launch(intent)
//    }


        private fun removeCourse(course: CourseInfo) {
            val position = courseList.indexOf(course)
            if (position != -1) {
                courseList.removeAt(position)
                courseAdapter.notifyItemRemoved(position)
            }
            val url = "http://10.0.2.2:8000:8000/subjects/${course.courseId}"
            val queue = Volley.newRequestQueue(this)

            val request = StringRequest(Request.Method.DELETE, url,
                {
                    val position = courseList.indexOf(course)
                    if (position != -1) {
                        courseList.removeAt(position)
                        courseAdapter.notifyItemRemoved(position)
                    }
                    Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                },
                {
                    it.printStackTrace()
                    Toast.makeText(this, "Failed to delete course", Toast.LENGTH_SHORT).show()
                })

            queue.add(request)
        }



    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
        val courseId = data.getIntExtra("courseId", -1)
        val name = data.getStringExtra("courseName") ?: return
        val teacher = data.getStringExtra("teacherName") ?: return
        val newCourse = CourseInfo(courseId, name, teacher)

        if (isEdit) {
            val index = courseList.indexOfFirst { it.courseId == courseId }
            if (index != -1) {
                courseList[index] = newCourse
                courseAdapter.notifyItemChanged(index)
            }
        } else {
            courseList.add(newCourse)
            courseAdapter.notifyItemInserted(courseList.size - 1)
        }
    }
}
