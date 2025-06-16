package pt.ola.online_classes_app.admin

//import AdminAddClass
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.admin.classes.AdminAddClass
import pt.ola.online_classes_app.admin.courses.AdminAddCourse
import pt.ola.online_classes_app.admin.students.AdminAddStudent
import pt.ola.online_classes_app.admin.teachers.AdminAddTeacher

class admin_dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        findViewById<Button>(R.id.edit_classes_btn).setOnClickListener {
            startActivity(Intent(this, AdminAddClass::class.java))
        }

        findViewById<Button>(R.id.edit_students_btn).setOnClickListener {
            startActivity(Intent(this, AdminAddStudent::class.java))
        }

        findViewById<Button>(R.id.edit_teachers_btn).setOnClickListener {
            startActivity(Intent(this, AdminAddTeacher::class.java))
        }

        findViewById<Button>(R.id.edit_courses_btn).setOnClickListener {
            startActivity(Intent(this, AdminAddCourse::class.java))
        }
    }
}
