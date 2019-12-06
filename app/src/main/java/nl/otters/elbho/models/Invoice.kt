package nl.otters.elbho.models

import com.google.gson.annotations.SerializedName

object Invoice {

    data class File(

        //TODO: Find out how to parse base64 encoded string to pdf-file
        val data: String,
        val month: String,
        @SerializedName("advisor-id")
        val adviserId: String
    )
}