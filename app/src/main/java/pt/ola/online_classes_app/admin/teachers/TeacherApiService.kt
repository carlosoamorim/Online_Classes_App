import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import pt.ola.online_classes_app.admin.teachers.TeacherInfo


class TeacherApiService(private val context: Context) {

    private val baseUrl = "http://10.0.2.2:8000/"  // Emulator access to localhost
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun getTeachers(onSuccess: (List<TeacherInfo>) -> Unit, onError: (Throwable) -> Unit) {
        val url = baseUrl + "users/"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response: JSONArray ->
                val teacherList = mutableListOf<TeacherInfo>()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    if (obj.optString("role") == "teacher") {
                        val teacher = TeacherInfo(
                            name = obj.optString("name"),
                            email = obj.optString("email"),
                            password = obj.optString("password"),
                            role = obj.optString("role")
                        )
                        teacherList.add(teacher)
                    }

                }
                onSuccess(teacherList)
            },
            { error ->
                onError(error)
            }
        )
        queue.add(request)
    }

    fun addTeacher(teacher: TeacherInfo, onComplete: (TeacherInfo) -> Unit, onError: (Throwable) -> Unit){
        val url = baseUrl + "users/"

        val jsonBody = JSONObject()
        jsonBody.put("name", teacher.name)
        jsonBody.put("email", teacher.email)
        jsonBody.put("password", teacher.password)
        jsonBody.put("role", teacher.role)

        Log.d("API_REQUEST", jsonBody.toString())

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                val id = response.optInt("id", -1)
                val name = response.optString("name")
                val email = response.optString("email")
                val password = response.optString("password")
                val role = response.optString("role")

                val newTeacher = TeacherInfo(id, name, email, password, role)
                onComplete(newTeacher)
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

    fun deleteTeacher(email: String, onComplete: () -> Unit, onError: (Throwable) -> Unit) {
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
