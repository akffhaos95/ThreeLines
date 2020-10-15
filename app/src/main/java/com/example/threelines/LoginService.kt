package com.example.threelines

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{
    @FormUrlEncoded
    @POST("/app_login/")
    fun requestLogin(
        @Field("userid") id: String?,
        @Field("userpw") pw:String?
    ) : Call<Login>
}