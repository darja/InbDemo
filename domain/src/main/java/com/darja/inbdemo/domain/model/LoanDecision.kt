package com.darja.inbdemo.domain.model

sealed class LoanDecision {

    data class Approved(val maxApprovedAmount: Int) : LoanDecision()

    data class ApprovedLower(val approvedAmount: Int) : LoanDecision()

    data class Rejected(val reason: RejectionReason) : LoanDecision()
}