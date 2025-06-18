package pt.ola.online_classes_app.admin.students

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ola.online_classes_app.R

class AddOrEditStudent : AppCompatActivity() {

    private lateinit var editStudentName: EditText
    private lateinit var editStudentEmail: EditText
    private lateinit var editStudentPassword: EditText

    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_student)

        editStudentName = findViewById(R.id.editStudentName)
        editStudentEmail = findViewById(R.id.editStudentEmail)
        editStudentPassword = findViewById(R.id.editStudentPassword)

        val btnSaveStudent = findViewById<Button>(R.id.btnSaveStudent)


        if (intent.hasExtra("studentName")) {
            isEditMode = true
            editStudentName.setText(intent.getStringExtra("studentName"))
            editStudentEmail.setText(intent.getStringExtra("studentEmail"))
            editStudentPassword.setText(intent.getStringExtra("studentPassword"))
        }

        btnSaveStudent.setOnClickListener {
            val name = editStudentName.text.toString().trim()
            val email = editStudentEmail.text.toString().trim()
            val password = editStudentPassword.text.toString().trim()


            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                //putExtra("studentId", studentId)
                putExtra("studentName", name)
                putExtra("studentEmail", email)
                putExtra("studentPassword", password)

            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

}
