package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Invoice {

    data class File(
        @SerializedName("_id")
        val id: String,
        @SerializedName("advisor")
        val adviserId: String,
        val fileName: String,
        val date: String,
        val filePath: String,
        val invoiceMonth: String,
        val createdAt: String,
        val updatedAt: String
    )

    data class Upload(
        val date: String,
        val file: String
    )
}