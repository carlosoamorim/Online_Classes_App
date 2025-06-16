package pt.ola.online_classes_app.model

data class ClassResponse(
    val id: Int,
    val subject_id: Int,
    val start_time: String,
    val end_time: String,
    val subject: Subject
)