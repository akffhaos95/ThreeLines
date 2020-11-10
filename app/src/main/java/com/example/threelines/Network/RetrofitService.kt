package com.example.threelines.Network

import com.example.threelines.Data.Record
import com.example.threelines.Data.Text
import com.example.threelines.Data.Result
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @FormUrlEncoded
    @POST("login_app/")
    fun login(
        @Field("user_id") user_id : String,
        @Field("passwd") passwd : String
    ): Call<Result>

    @FormUrlEncoded
    @POST("register_app/")
    fun register(
        @Field("user_id") user_id : String,
        @Field("passwd") passwd : String
    ): Call<Result>

    @FormUrlEncoded
    @Multipart
    @POST("/mic_app/")
    fun mic(
        @Part("user_id") user_id : String,
        @Part ("file") file : MultipartBody
    ): Call<Result>

    @GET("record_app")
    fun getRecord(
        @Query("user_id") user_id: String
    ): Call<List<Record>>

    @FormUrlEncoded
    @GET("/text_app")
    fun getText(
        @Field("user_id") user_id: String,
        @Field("record_id") record_id: Int
    ): Call<List<Text>>
}