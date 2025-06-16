package pt.ola.online_classes_app.admin.students

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ola.online_classes_app.R

class AddOrEditStudent : AppCompatActivity() {

    private lateinit var editStudentName: EditText
    private lateinit var editStudentEmail: EditText

    private var isEditMode = false
    private var studentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_student)

        editStudentName = findViewById(R.id.editStudentName)
        editStudentEmail = findViewById(R.id.editStudentEmail)
        val btnSaveStudent = findViewById<Button>(R.id.btnSaveStudent)


        // Check if editing existing class
        if (intent.hasExtra("studentName")) {
            isEditMode = true
            studentId = intent.getIntExtra("studentId", -1)
            editStudentName.setText(intent.getStringExtra("studentName"))
            editStudentEmail.setText(intent.getStringExtra("studentEmail"))
        }

        btnSaveStudent.setOnClickListener {
            val name = editStudentName.text.toString().trim()
            val email = editStudentEmail.text.toString().trim()


            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("studentId", studentId)
                putExtra("studentName", name)
                putExtra("studentEmail", email)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
