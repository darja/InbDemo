package com.darja.inbdemo.domain.model

/**
 * Bank client profile
 */
class ClientProfile(
    val personalCode: String,
    val creditStatus: CreditStatus
)

/**
 * Status that should be checked for the score calculation
 */
// todo think about naming
sealed class CreditStatus {
    /**
     * Status indicating that the client is in debt
     */
    object Debt: CreditStatus()

    /**
     * Status indicating that the client belongs to a particular segment
     */
    data class Segment(val segment: Int): CreditStatus()
}

