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

    @GET("record_app/{user_id}")
    fun getRecord(
        @Path(value = "user_id", encoded = true) user_id: String
    ): Call<List<Record>>

    @GET("text_app/{record_id}")
    fun getText(
        @Path(value = "record_id", encoded = true) record_id: Int
    ): Call<List<Text>>
}