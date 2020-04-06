package com.darja.inbdemo.domain.usecase

import com.darja.inbdemo.domain.model.CreditStatus
import com.darja.inbdemo.domain.model.LoanDecision
import com.darja.inbdemo.domain.model.RejectionReason
import com.darja.inbdemo.domain.repo.ClientNotFoundException
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository

class GetLoanDecisionUseCase(private val clientRepository: ClientRepository,
                             private val creditRules: CreditRulesRepository) {
    suspend fun execute(request: Request): LoanDecision {
        lateinit var decision: LoanDecision

        try {
            val client = clientRepository.getClientByPersonalCode(request.personalCode)

            when (client.creditStatus) {
                // client has debt
                is CreditStatus.Debt -> decision = LoanDecision.Rejected(RejectionReason.DEBT)

                // client belongs to a segment
                is CreditStatus.Segment -> {
                    val creditModifier = creditRules.getCreditModifier(client.creditStatus.segment)

                    // Score = Period * CreditModifier / Amount
                    val score = request.periodMonths * 1f * creditModifier / request.amount

                    // Amount = Period * CreditModifier / Score
                    // as amount and score are inversely proportional, the biggest amount is reached when score is minimum.
                    // minimum accepted score is 1, so MaxAmount = Period * CreditModifier
                    val maxAmount = request.periodMonths * creditModifier

                    decision = if (score > 1) {
                        LoanDecision.Approved(maxAmount)
                    } else {
                        LoanDecision.RejectedWithOption(maxAmount)
                    }
                }
            }


        } catch (e: ClientNotFoundException) {
            decision = LoanDecision.Rejected(RejectionReason.UNKNOWN_CLIENT)
        }

        return decision
    }

    data class Request(
        val personalCode: String,
        val amount: Int,
        val periodMonths: Int
    )
}