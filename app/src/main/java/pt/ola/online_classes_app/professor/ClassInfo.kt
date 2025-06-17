package pt.ola.online_classes_app.professor
import pt.ola.online_classes_app.model.Subject


data class ClassInfo(
    val id: Int,
    val subject_id: Int,
    val auditorium: String,
    val start_time: String,
    val end_time: String,
    val subject_name: String
)