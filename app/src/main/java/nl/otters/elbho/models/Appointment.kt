package nl.otters.elbho.models
import com.google.gson.annotations.SerializedName

object Appointment {

    data class Properties(
        @SerializedName("_id")
        val id: String,
        val startTime: String,
        val endTime: String,
        val comment: String,
        val address: String,
        val contactPersonName: String,
        val contactPersonPhoneNumber: String,
        val contactPersonFunction: String,
        val contactPersonEmail: String,
        val active: String,
        val website: String,
        val logo: String,
        val cocNumber: String,
        val cocName: String,
        @SerializedName("advisor")
        val adviser: String,
        val createdAt: String,
        val updatedAt: String
    )

// TODO: Could somejuan please come up with a better name?
    data class Options(
    val page: Number?,
    val limit: Number?,
    val before: String?,
    val after: String?
    )
}
