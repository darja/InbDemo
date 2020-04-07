package com.darja.inbdemo.domain.usecase

import com.darja.inbdemo.domain.model.*
import com.darja.inbdemo.domain.repo.ClientNotFoundException
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class GetLoanDecisionUseCaseTest {
    private lateinit var clientRepository: ClientRepository
    private lateinit var creditRules: CreditRulesRepository

    @Before
    fun setUp() {
        clientRepository = mock {
            onBlocking { getClientByPersonalCode(CLIENT_UNKNOWN) } doThrow ClientNotFoundException()
            onBlocking { getClientByPersonalCode(CLIENT_IN_DEBT) } doReturn
                    ClientProfile(CLIENT_IN_DEBT, CreditStatus.Debt)
            onBlocking { getClientByPersonalCode(CLIENT_SEGMENT_1) } doReturn
                    ClientProfile(CLIENT_SEGMENT_1, CreditStatus.Segment(SEGMENT_1))
            onBlocking { getClientByPersonalCode(CLIENT_SEGMENT_2) } doReturn
                    ClientProfile(CLIENT_SEGMENT_2, CreditStatus.Segment(SEGMENT_2))
        }

        creditRules = mock {
            on { getCreditModifier(SEGMENT_1) } doReturn MODIFIER_1
            on { getCreditModifier(SEGMENT_2) } doReturn MODIFIER_2
            on { getMinLoan() } doReturn 2000
            on { getMaxLoan() } doReturn 10_000
        }
    }

    /**
     * Test case: Reject for an unknown client
     */
    @Test
    fun testUnknownClient() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = LoanClaim(CLIENT_UNKNOWN, 122, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertThat("Decision", decision, instanceOf(LoanDecision.Rejected::class.java))
        assertEquals(
            "Rejection reason",
            RejectionReason.UNKNOWN_CLIENT,
            (decision as LoanDecision.Rejected).reason
        )
    }

    /**
     * Test case: Reject for a client with a debt
     */
    @Test
    fun testClientInDebt() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = LoanClaim(CLIENT_IN_DEBT, 122, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertThat("Decision", decision, instanceOf(LoanDecision.Rejected::class.java))
        assertEquals(
            "Rejection reason",
            RejectionReason.DEBT,
            (decision as LoanDecision.Rejected).reason
        )
    }

    /**
     * Test case: Loan is rejected, lower loan is suggested
     */
    @Test
    fun testApproveLowerAmount() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = LoanClaim(CLIENT_SEGMENT_2, 4000, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertThat("Decision", decision, instanceOf(LoanDecision.RejectedWithOption::class.java))
        assertEquals(
            "Max sum",
            3600,
            (decision as LoanDecision.RejectedWithOption).maxApprovedAmount
        )
    }

    /**
     * Test case: Loan is rejected, lower loan cannot be suggested as it is smaller than minimum
     */
    @Test
    fun testRejectInsufficientScore() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = LoanClaim(CLIENT_SEGMENT_1, 2000, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertThat("Decision", decision, instanceOf(LoanDecision.Rejected::class.java))
        assertEquals(
            "Rejection reason",
            RejectionReason.INSUFFICIENT_SCORE,
            (decision as LoanDecision.Rejected).reason
        )
    }

    /**
     * Test case: Loan is approved
     */
    @Test
    fun testApproveHigherAmount() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = LoanClaim(CLIENT_SEGMENT_1, 2000, 30)
        val decision = runBlocking { useCase.execute(request) }
        assertThat("Decision", decision, instanceOf(LoanDecision.Approved::class.java))
        assertEquals("Max sum", 3000, (decision as LoanDecision.Approved).maxApprovedAmount)
    }

    /**
     * Test case: Loan is approved, suggested loan amount exceeds maximum
     */
    @Test
    fun testApproveMaximumAmount() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = LoanClaim(CLIENT_SEGMENT_2, 5000, 40)
        val decision = runBlocking { useCase.execute(request) }
        assertThat("Decision", decision, instanceOf(LoanDecision.Approved::class.java))
        assertEquals("Max sum", 10000, (decision as LoanDecision.Approved).maxApprovedAmount)
    }

    companion object {
        const val CLIENT_UNKNOWN = "Unregistered"
        const val CLIENT_IN_DEBT = "InDebt"
        const val CLIENT_SEGMENT_1 = "Segment1"
        const val CLIENT_SEGMENT_2 = "Segment2"

        const val SEGMENT_1 = 1
        const val SEGMENT_2 = 2

        const val MODIFIER_1 = 100
        const val MODIFIER_2 = 300
    }
}