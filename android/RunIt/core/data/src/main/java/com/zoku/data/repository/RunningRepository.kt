package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.data.model.MyTestData
import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.response.PostRunningRecordResponse
import com.zoku.network.model.response.TestSumResponse
import okhttp3.MultipartBody
import java.util.Date

interface RunningRepository {

    suspend fun getRunningRecord(date: Date): Result<List<MyTestData>>

    suspend fun postTestSum(testSumRequest: TestSumRequest): NetworkResult<TestSumResponse>

    suspend fun postRunningRecord(
        dto: MultipartBody.Part,
        images: MultipartBody.Part
    ): NetworkResult<PostRunningRecordResponse>
}