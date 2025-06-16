package pt.ola.online_classes_app.admin.courses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ola.online_classes_app.R

class AddOrEditCourse : AppCompatActivity() {

    private lateinit var editCourseName: EditText
    private lateinit var editTeacherName: EditText
    private var courseId = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
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
            val course = editCourseName.text.toString().trim()
            val teacher = editTeacherName.text.toString().trim()

            if (course.isEmpty() || teacher.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("courseId", courseId)
                putExtra("courseName", course)
                putExtra("teacherName", teacher)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}
