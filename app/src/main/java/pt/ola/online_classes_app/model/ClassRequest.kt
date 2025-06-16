package pt.ola.online_classes_app.model

data class ClassRequest(
    val subject_id: Int,
    val start_time: String, // ISO 8601 format: "2025-06-17T09:00:00"
    val end_time: String
)