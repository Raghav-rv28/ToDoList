package Database

import androidx.room.TypeConverter
import java.util.*

class TaskTypeConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let{
            Date(it)
        }
    }
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}