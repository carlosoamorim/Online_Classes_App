package pt.ola.online_classes_app.admin.courses


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val editCourseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleActivityResult(it, isEdit = true) }
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
            onEditClick = { openEditCourseActivity(it) },
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
        courseList.add(CourseInfo(1,"Databases", "Ana"))
        courseList.add(CourseInfo(2,"Networking", "Luís"))
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


    private fun openEditCourseActivity(course: CourseInfo) {
        val intent = Intent(this, AddOrEditCourse::class.java).apply {
            putExtra("courseId", courseList.indexOf(course))
            putExtra("courseName", course.courseName)
            putExtra("teacherName", course.teacherName)
        }
        editCourseLauncher.launch(intent)
    }

    private fun removeCourse(course: CourseInfo) {
        val position = courseList.indexOf(course)
        if (position != -1) {
            courseList.removeAt(position)
            courseAdapter.notifyItemRemoved(position)
        }
    }

    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
        val courseId = data.getIntExtra("courseId", -1)
        val name = data.getStringExtra("courseName") ?: return
        val teacher = data.getStringExtra("teacherName") ?: return
        val newCourse = CourseInfo(courseId, name, teacher)

        if (isEdit) {
            val position = data.getIntExtra("courseId", -1)
            if (position in courseList.indices) {
                courseList[position] = newCourse
                courseAdapter.notifyItemChanged(position)
            }
        } else {
            courseList.add(newCourse)
            courseAdapter.notifyItemInserted(courseList.size - 1)
        }
    }
}
