package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Invoice {

    data class File(
        val id: Int,
        @SerializedName("advisorId")
        val adviserId: String,
        val month: String,
        val fileName: String,
        val filePath: String,
        //TODO: Find out how to parse base64 encoded string to pdf-file
        @SerializedName("base64EncodedPdf")
        val document: String
    )
}