package com.darja.inbdemo.domain.usecase

import com.darja.inbdemo.domain.model.ClientProfile
import com.darja.inbdemo.domain.model.CreditStatus
import com.darja.inbdemo.domain.model.LoanDecision
import com.darja.inbdemo.domain.model.RejectionReason
import com.darja.inbdemo.domain.repo.ClientNotFoundException
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLoanDecisionUseCaseTest {
    private lateinit var clientRepository: ClientRepository
    private lateinit var creditRules: CreditRulesRepository

    @Before
    fun setUp() {
        clientRepository = mock {
            onBlocking { getClientByPersonalCode(CLIENT_UNKNOWN) } doThrow ClientNotFoundException()
            onBlocking { getClientByPersonalCode(CLIENT_IN_DEBT) } doReturn ClientProfile(
                CLIENT_IN_DEBT,
                CreditStatus.Debt(100)
            )
            onBlocking { getClientByPersonalCode(CLIENT_SEGMENT_1) } doReturn ClientProfile(
                CLIENT_SEGMENT_1,
                CreditStatus.Segment(SEGMENT_1)
            )
            onBlocking { getClientByPersonalCode(CLIENT_SEGMENT_2) } doReturn ClientProfile(
                CLIENT_SEGMENT_2,
                CreditStatus.Segment(SEGMENT_2)
            )
        }

        creditRules = mock {
            on { getCreditModifier(SEGMENT_1) } doReturn MODIFIER_1
            on { getCreditModifier(SEGMENT_2) } doReturn MODIFIER_2
        }
    }

    /**
     * Test case: Unknown client tries to request a loan
     */
    @Test
    fun testUnknownClient() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = GetLoanDecisionUseCase.Request(CLIENT_UNKNOWN, 122, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertTrue("Loan is rejected for an unknown user", decision is LoanDecision.Rejected)
        assertEquals("Rejection reason", RejectionReason.UNKNOWN_CLIENT, (decision as LoanDecision.Rejected).reason)
    }

    /**
     * Test case: A client with a debt tries to request a loan
     */
    @Test
    fun testClientInDebt() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = GetLoanDecisionUseCase.Request(CLIENT_IN_DEBT, 122, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertTrue("Loan is rejected for a user in debt", decision is LoanDecision.Rejected)
        assertEquals("Rejection reason", RejectionReason.DEBT, (decision as LoanDecision.Rejected).reason)
    }

    /**
     * Test case: Loan is approved
     */
    @Test
    fun testApproveHigherAmount() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = GetLoanDecisionUseCase.Request(CLIENT_SEGMENT_1, 1000, 12)
        val decision = runBlocking { useCase.execute(request) }
        assertTrue("Loan is approved", decision is LoanDecision.Approved)
        assertEquals("Max sum", 1200, (decision as LoanDecision.Approved).maxApprovedAmount)
    }

    /**
     * Test case: Loan is rejected, lower loan is suggested
     */
    @Test
    fun testApproveLowerAmount() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = GetLoanDecisionUseCase.Request(CLIENT_SEGMENT_2, 4000, 12)
        val decision = runBlocking { useCase.execute(request) }

        assertTrue("Loan is rejected, lower amount suggested", decision is LoanDecision.RejectedWithOption)
        assertEquals("Max sum", 3600, (decision as LoanDecision.RejectedWithOption).maxApprovedAmount)
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