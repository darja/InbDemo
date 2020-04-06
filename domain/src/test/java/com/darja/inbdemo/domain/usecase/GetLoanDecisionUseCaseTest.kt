package com.darja.inbdemo.domain.usecase

import com.darja.inbdemo.domain.model.ClientProfile
import com.darja.inbdemo.domain.model.CreditStatus
import com.darja.inbdemo.domain.model.LoanDecision
import com.darja.inbdemo.domain.model.RejectionReason
import com.darja.inbdemo.domain.repo.ClientNotFoundException
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetLoanDecisionUseCaseTest {
    private lateinit var clientRepository: ClientRepository
    private lateinit var creditRules: CreditRulesRepository

    @Before
    fun setUp() {
        clientRepository = Mockito.mock(ClientRepository::class.java)
        Mockito.`when`(clientRepository.getClientByPersonalCode(CLIENT_UNKNOWN)).thenThrow(ClientNotFoundException())
        Mockito.`when`(clientRepository.getClientByPersonalCode(CLIENT_IN_DEBT)).thenReturn(
            ClientProfile(CLIENT_IN_DEBT, CreditStatus.Debt(100)))
        Mockito.`when`(clientRepository.getClientByPersonalCode(CLIENT_SEGMENT_1)).thenReturn(
            ClientProfile(CLIENT_IN_DEBT, CreditStatus.Segment(SEGMENT_1)))
        Mockito.`when`(clientRepository.getClientByPersonalCode(CLIENT_SEGMENT_2)).thenReturn(
            ClientProfile(CLIENT_IN_DEBT, CreditStatus.Segment(SEGMENT_2)))
        Mockito.`when`(clientRepository.getClientByPersonalCode(CLIENT_SEGMENT_2)).thenReturn(
            ClientProfile(CLIENT_IN_DEBT, CreditStatus.Segment(SEGMENT_3)))

        creditRules = Mockito.mock(CreditRulesRepository::class.java)
        Mockito.`when`(creditRules.getCreditModifier(1)).thenReturn(MODIFIER_1)
        Mockito.`when`(creditRules.getCreditModifier(2)).thenReturn(MODIFIER_2)
        Mockito.`when`(creditRules.getCreditModifier(3)).thenReturn(MODIFIER_3)
    }

    @Test
    fun testUnknownClient() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = GetLoanDecisionUseCase.Request(CLIENT_UNKNOWN, 122, 12)
        val decision = useCase.execute(request)

        assertTrue("Loan is rejected for an unknown user", decision is LoanDecision.Rejected)
        assertEquals("Rejection reason", RejectionReason.UNKNOWN_CLIENT, (decision as LoanDecision.Rejected).reason)
    }

    @Test
    fun testClientInDebt() {
        val useCase = GetLoanDecisionUseCase(clientRepository, creditRules)

        val request = GetLoanDecisionUseCase.Request(CLIENT_IN_DEBT, 122, 12)
        val decision = useCase.execute(request)

        assertTrue("Loan is rejected for a user in debt", decision is LoanDecision.Rejected)
        assertEquals("Rejection reason", RejectionReason.DEBT, (decision as LoanDecision.Rejected).reason)
    }

    companion object {
        const val CLIENT_UNKNOWN = "Unregistered"
        const val CLIENT_IN_DEBT = "InDebt"
        const val CLIENT_SEGMENT_1 = "Segment1"
        const val CLIENT_SEGMENT_2 = "Segment2"

        const val SEGMENT_1 = 1
        const val SEGMENT_2 = 2
        const val SEGMENT_3 = 3

        const val MODIFIER_1 = 100
        const val MODIFIER_2 = 300
        const val MODIFIER_3 = 1000
    }
}