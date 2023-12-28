package com.example.contestalert.model

import kotlinx.serialization.Serializable


@Serializable
data class ContestAlert (
    val id:Long,
    val name:String,
    val type:String,
    val phase:String,
    val frozen:Boolean,
    val durationSeconds:Long,
    val startTimeSeconds:Long,
    val relativeTimeSeconds:Long
)