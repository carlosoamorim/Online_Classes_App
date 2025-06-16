package pt.ola.online_classes_app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.professor.professor_dashboard
import pt.ola.online_classes_app.student.student_dashboard

class login : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button


    private val BASE_URL = "http://10.0.2.2:8000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.email_input)
        editTextPassword = findViewById(R.id.password_input)
        buttonLogin = findViewById(R.id.login_btn)

        buttonLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please, fill all the information", Toast.LENGTH_SHORT).show()
            return
        }
    // Endpoint do mike
        val requestQueue = Volley.newRequestQueue(this)
        val url = "$BASE_URL/users"

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                // login success
                val id = response.getInt("id")
                val name = response.getString("name")
                val userEmail = response.getString("email")
                val role = response.getString("role")

                // Store user info
                val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("user_id", id)
                    putString("user_name", name)
                    putString("user_email", userEmail)
                    putString("user_role", role)
                    apply()
                }

                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()

                // Redirect according to user role
                when (role) {
                    "student" -> {
                        val intent = Intent(this, student_dashboard::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "teacher" -> {
                        val intent = Intent(this, professor_dashboard::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, "Papel de usuário desconhecido: $role", Toast.LENGTH_LONG).show()
                    }
                }
            },
            { error ->
                // login error
                val errorMessage = error.message ?: "Unknown error"
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()

                error.networkResponse?.data?.let { data ->
                    try {
                        val errorJson = JSONObject(String(data))
                        if (errorJson.has("detail")) {
                            Toast.makeText(this, "Erro: ${errorJson.getString("detail")}", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}