package com.darja.inbdemo.domain.model

sealed class LoanDecision {

    /**
     * The loan is approved, higher amount is available
     */
    data class Approved(val maxApprovedAmount: Int) : LoanDecision()

    /**
     * The loan is rejected, lower amount can be suggested
     */
    data class Suggested(val maxApprovedAmount: Int) : LoanDecision()

    /**
     * The loan is rejected without options
     */
    data class Rejected(val reason: RejectionReason) : LoanDecision()
}