package pt.ola.online_classes_app.admin.courses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.admin.students.ClassInfoPresences
import pt.ola.online_classes_app.admin.students.StudentListAdapter

class CourseStudentListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_student_list)

        // Replace with real data or intent data if available
        val studentList = arrayListOf(
            ClassInfoPresences("1", "Alice Smith", "alice@example.com", false),
            ClassInfoPresences("2", "Bob Johnson", "bob@example.com", true)
        )

        recyclerView = findViewById(R.id.studentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = StudentListAdapter(
            studentList,
            onEnrollClick = { student ->
                student.isEnrolled = true
                // TODO: update database if needed
            },
            onUnenrollClick = { student ->
                student.isEnrolled = false
                // TODO: update database if needed
            }
        )

        recyclerView.adapter = adapter
    }
}
