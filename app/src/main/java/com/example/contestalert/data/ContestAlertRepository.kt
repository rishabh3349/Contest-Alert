package com.example.contestalert.data

import com.example.contestalert.model.ContestAlert
import com.example.contestalert.network.ContestApiService

interface ContestAlertRepository {
    suspend fun getAlert(): List<ContestAlert>
}

class DefaultContestAlertRepository(
    private val contestApiService: ContestApiService
) : ContestAlertRepository {
    override suspend fun getAlert(): List<ContestAlert> = contestApiService.getAlert().result
}