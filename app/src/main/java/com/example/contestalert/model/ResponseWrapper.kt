package com.example.contestalert.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper(
    val status:String,
    val result:List<ContestAlert>
)