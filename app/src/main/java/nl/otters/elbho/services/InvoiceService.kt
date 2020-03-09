package nl.otters.elbho.services

import nl.otters.elbho.models.Invoice
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface InvoiceService {

    @Headers("Content-type: application/json")
    @GET("auth/invoice/me")
    fun getAllInvoices(@Header("Authorization") auth: String): Call<ArrayList<Invoice.File>>

    @Multipart
    @POST("/auth/invoice")
    fun createInvoice(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("date") date: String
    ): Call<ResponseBody>
}