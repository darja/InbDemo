package com.darja.inbdemo.domain.usecase

import com.darja.inbdemo.domain.model.CreditStatus
import com.darja.inbdemo.domain.model.LoanClaim
import com.darja.inbdemo.domain.model.LoanDecision
import com.darja.inbdemo.domain.model.RejectionReason
import com.darja.inbdemo.domain.repo.ClientNotFoundException
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import kotlin.math.min

class GetLoanDecisionUseCase(private val clientRepository: ClientRepository,
                             private val creditRules: CreditRulesRepository) {
    suspend fun execute(claim: LoanClaim): LoanDecision {
        lateinit var decision: LoanDecision

        try {
            val client = clientRepository.getClientByPersonalCode(claim.personalCode)

            when (client.creditStatus) {
                // client has debt
                is CreditStatus.Debt -> decision = LoanDecision.Rejected(RejectionReason.DEBT)

                // client belongs to a segment
                is CreditStatus.Segment -> {
                    val creditModifier = creditRules.getCreditModifier(client.creditStatus.segment)

                    // Score = Period * CreditModifier / Amount
                    val score = claim.periodMonths * 1f * creditModifier / claim.amount

                    // Amount = Period * CreditModifier / Score
                    // as amount and score are inversely proportional, the biggest amount is reached when score is minimum.
                    // minimum accepted score is 1, so MaxAmount = Period * CreditModifier
                    // Also, amount shouldn't exceed the maximum defined in credit rules
                    val maxAmount = min(claim.periodMonths * creditModifier, creditRules.getMaxLoan())

                    decision = when {
                        score > 1 -> LoanDecision.Approved(maxAmount)
                        maxAmount > creditRules.getMinLoan() -> LoanDecision.RejectedWithOption(maxAmount)
                        else -> LoanDecision.Rejected(RejectionReason.INSUFFICIENT_SCORE)
                    }
                }
            }


        } catch (e: ClientNotFoundException) {
            decision = LoanDecision.Rejected(RejectionReason.UNKNOWN_CLIENT)
        }

        return decision
    }
}