package Database

import androidx.room.TypeConverter
import java.util.*
import org.joda.time.DateTime
import org.joda.time.LocalDate


class TaskTypeConverter {

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toDate(dateInText: String?): LocalDate {
        return LocalDate(dateInText)
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}