package pt.ola.online_classes_app.admin.students

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import pt.ola.online_classes_app.admin.students.StudentInfo

class StudentApiService(private val context: Context) {
    private val queue: RequestQueue = Volley.newRequestQueue(context)
    private val baseUrl = "http://10.0.2.2:8000/"

    fun getStudents(onSuccess: (List<StudentInfo>) -> Unit, onError: (Throwable) -> Unit) {
        val url = baseUrl + "users/"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response: JSONArray ->
                val studentList = mutableListOf<StudentInfo>()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    if (obj.getString("role") == "student") {
                        val student = StudentInfo(
                            name = obj.getString("name"),
                            email = obj.getString("email"),
                            password = obj.optString("password"),
                            role = obj.getString("role")
                        )
                        studentList.add(student)
                    }
                }
                onSuccess(studentList)
            },
            { error ->
                Log.e("API_ERROR", "Failed to fetch students", error)
                onError(error)
            }
        )

        queue.add(request)
    }

    fun addStudent(student: StudentInfo, onComplete: () -> Unit, onError: (Throwable) -> Unit) {
        val url = baseUrl + "users/"
        val jsonBody = JSONObject()
        jsonBody.put("name", student.name)
        jsonBody.put("email", student.email)
        jsonBody.put("password", student.password)
        jsonBody.put("role", student.role)

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                onComplete()
            },
            { error ->
                error.networkResponse?.let { networkResponse ->
                    val statusCode = networkResponse.statusCode
                    val responseData = String(networkResponse.data)
                    Log.e("API_ERROR", "Error $statusCode: $responseData")
                }
                Log.e("API_ERROR", "Volley error", error)
                onError(error)
            }
        )

        queue.add(request)
    }

    fun deleteStudent(email: String, onComplete: () -> Unit, onError: (Throwable) -> Unit) {
        val url = baseUrl + "users/?email=$email"

        val request = StringRequest(
            Request.Method.DELETE, url,
            {
                onComplete()
            },
            { error ->
                onError(error)
            }
        )

        queue.add(request)
    }

}

