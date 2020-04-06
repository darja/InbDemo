package com.darja.inbdemo.domain.model

sealed class LoanDecision

data class ApprovedHigherLoanDecision(val approvedAmount: Int): LoanDecision()

data class ApprovedLowerLoanDecision(val approvedAmount: Int): LoanDecision()

data class RejectedLoanDecision(val reason: RejectionReason): LoanDecision()