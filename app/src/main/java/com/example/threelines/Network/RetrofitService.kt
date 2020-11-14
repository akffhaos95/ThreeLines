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

    @GET("record_app/{user_id}")
    fun getRecord(
        @Path(value = "user_id", encoded = true) user_id: String
    ): Call<List<Record>>

    @GET("text_app/{record_id}")
    fun getText(
        @Path(value = "record_id", encoded = true) record_id: Int
    ): Call<List<Text>>

    @Multipart
    @POST("mic_app/")
    fun mic(
        @Part("title") title: String,
        @Part("user_id") user_id: String,
        @Part("people") people : Int,
        @Part("location") location : String,
        @Part file: MultipartBody.Part
    ): Call<Result>

    // 안됨
    @FormUrlEncoded
    @Multipart
    @POST("edit_text_app/")
    fun edit_text(
        @Part("idx") idx : Int,
        @Part("content") content : String
    ): Call<Result>

    // 안됨
    @FormUrlEncoded
    @Multipart
    @POST("edit_speaker_app/")
    fun edit_speaker(
        @Part("speaker_id") speaker_id : Int,
        @Part("speaker_name") speaker_name : String
    ): Call<Result>

}