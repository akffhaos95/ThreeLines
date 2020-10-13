package com.example.threelines

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UploadFileService {
    @Multipart
    @POST("/file_upload/")
    fun uploadFile(
        @Part("file") file: MultipartBody.Part
    ) : Call<resp>
}