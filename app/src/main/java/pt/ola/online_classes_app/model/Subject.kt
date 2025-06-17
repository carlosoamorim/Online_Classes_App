package pt.ola.online_classes_app.model

import pt.ola.online_classes_app.admin.teachers.TeacherInfo

data class Subject(
    val id: Int,
    val name: String,
    val teacher_id: Int,
    val teacher: TeacherInfo
)