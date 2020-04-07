package com.darja.inbdemo.ui.decision

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darja.inbdemo.R
import com.darja.inbdemo.domain.model.LoanClaim
import com.darja.inbdemo.domain.model.LoanDecision
import com.darja.inbdemo.domain.model.RejectionReason
import com.darja.inbdemo.domain.usecase.GetLoanDecisionUseCase
import com.darja.inbdemo.util.ResourceProvider
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class DecisionActivityViewModel(
    private val moneyFormatter: DecimalFormat,
    private val resourceProvider: ResourceProvider,
    private val getLoanDecisionUseCase: GetLoanDecisionUseCase
) : ViewModel() {

    private val titleMutable = MutableLiveData<String>()
    internal val title = titleMutable

    private val descriptionMutable = MutableLiveData<String>()
    internal val description = descriptionMutable

    internal fun makeLoanDecision(claim: LoanClaim) {
        viewModelScope.launch {
            val decision = getLoanDecisionUseCase.execute(claim)

            // Build title
            val titleResId = when (decision) {
                is LoanDecision.Approved -> R.string.decision_title_approve
                is LoanDecision.Rejected -> R.string.decision_title_reject
                is LoanDecision.RejectedWithOption -> R.string.decision_title_reject_with_options
            }
            titleMutable.postValue(resourceProvider.getString(titleResId))

            // Build description
            val description = when (decision) {
                is LoanDecision.Approved -> buildApprovedDescription(claim, decision)
                is LoanDecision.RejectedWithOption -> buildRejectedWithOptionsDescription(claim, decision)
                is LoanDecision.Rejected -> buildRejectedDescription(claim, decision)
            }
            descriptionMutable.postValue(description)
        }
    }

    private fun buildApprovedDescription(claim: LoanClaim, decision: LoanDecision.Approved) =
        resourceProvider.getString(R.string.decision_description_approve,
            moneyFormatter.format(decision.maxApprovedAmount),
            claim.periodMonths)

    private fun buildRejectedWithOptionsDescription(claim: LoanClaim, decision: LoanDecision.RejectedWithOption) =
        resourceProvider.getString(R.string.decision_description_reject_with_options,
            moneyFormatter.format(decision.maxApprovedAmount),
            claim.periodMonths,
            moneyFormatter.format(claim.amount)
            )

    private fun buildRejectedDescription(claim: LoanClaim, decision: LoanDecision.Rejected): String {
        val reason = when (decision.reason) {
            RejectionReason.DEBT ->
                resourceProvider.getString(R.string.decision_description_reject_debt)

            RejectionReason.UNKNOWN_CLIENT ->
                resourceProvider.getString(R.string.decision_description_reject_unknown_client, claim.personalCode)

            RejectionReason.INSUFFICIENT_SCORE ->
                resourceProvider.getString(R.string.decision_description_reject_insufficient_score)
        }

        return resourceProvider.getString(R.string.decision_description_reject, reason)
    }
}