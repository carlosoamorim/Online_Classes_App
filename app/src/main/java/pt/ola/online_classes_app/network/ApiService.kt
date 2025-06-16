package pt.ola.online_classes_app.network

import pt.ola.online_classes_app.model.ClassRequest
import pt.ola.online_classes_app.model.ClassResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("classes/")
    fun getClasses(): Call<List<ClassResponse>>

    @POST("classes/")
    fun createClass(@Body newClass: ClassRequest): Call<ClassResponse>
}
