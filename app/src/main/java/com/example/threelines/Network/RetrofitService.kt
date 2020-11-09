package com.example.threelines.Network

import com.example.threelines.Data.Record
import com.example.threelines.Data.Text
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @POST("/login_app")
    fun login(
        @Field("user_id") user_id : String,
        @Field("passwd") passwd : String
    ): Call<String>

    @POST("/register_app")
    fun register(
        @Field("user_id") user_id : String,
        @Field("passwd") passwd : String
    ): Call<String>

    @Multipart
    @POST("/file_upload/")
    fun uploadFile(
        @Part("user_id") user_id : String,
        @Part ("file") file : MultipartBody.Part
    ): Call<String>

    @GET("/record_app")
    fun getRecord(
        @Field("user_id") user_id: String
    ): Call<List<Record>>

    @GET("/text_app")
    fun getText(
        @Field("user_id") user_id: String,
        @Field("record_id") record_id: Int
    ): Call<List<Text>>
}