package com.darja.inbdemo.domain.model

/**
 * Bank client profile
 */
class ClientProfile(
    val personalCode: String,
    val status: ClientStatus
)

/**
 * Status that should be checked for the score calculation
 */
sealed class ClientStatus

/**
 * Status indicating that the client is in debt
 * @param amount is added just for sake of Kotlin sealed class
 */
data class DebtClientStatus(val amount: Int): ClientStatus()

/**
 * Status indicating that the client belongs to a particular segment
 */
data class ClientSegment(val segment: ClientStatus): ClientStatus()