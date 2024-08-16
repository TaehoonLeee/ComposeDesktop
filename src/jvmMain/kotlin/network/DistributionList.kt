package network

import kotlinx.serialization.Serializable

@Serializable
data class DistributionList(
    val distributions: List<Distribution>
)

@Serializable
data class Distribution(
    val key: String,
    val url: String
)