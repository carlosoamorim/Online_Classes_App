package pt.ola.online_classes_app.admin.classes

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

data class ClassInfo(
    val id: Int,
    val subjectId: Int,
    val courseName: String,
    val classDate: String,
    val classStartTime: String,
    val classEndTime: String,
    val classRoom: String,
    val teacherId: Int
){
    val formattedTime: String
        get() {
            return try {
                val start = ZonedDateTime.parse(classStartTime)
                val end = ZonedDateTime.parse(classEndTime)
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                "${formatter.format(start)} - ${formatter.format(end)}"
            } catch (e: Exception) {
                "$classStartTime - $classEndTime" // fallback
            }
        }

    val formattedDate: String
        get() {
            return try {
                val date = ZonedDateTime.parse(classStartTime)
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                formatter.format(date)
            } catch (e: Exception) {
                classDate
            }
        }
}