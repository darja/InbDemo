package com.darja.inbdemo.domain.usecase

import com.darja.inbdemo.domain.model.LoanDecision
import com.darja.inbdemo.domain.model.RejectedLoanDecision
import com.darja.inbdemo.domain.model.RejectionReason

class GetLoanDecisionUseCase {
    fun execute(request: Request): LoanDecision {
        return RejectedLoanDecision(RejectionReason.UNKNOWN_CLIENT)
    }

    data class Request(
        val personalCode: String,
        val amount: Int,
        val periodMonths: Int
    )
}