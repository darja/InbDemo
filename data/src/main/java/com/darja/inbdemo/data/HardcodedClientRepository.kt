package com.darja.inbdemo.data

import com.darja.inbdemo.domain.model.ClientProfile
import com.darja.inbdemo.domain.model.CreditStatus
import com.darja.inbdemo.domain.repo.ClientNotFoundException
import com.darja.inbdemo.domain.repo.ClientRepository

/**
 * [ClientRepository] implementation that uses hardcoded data
 */
class HardcodedClientRepository: ClientRepository {

    private val registeredClients: List<ClientProfile> = listOf(
        ClientProfile("49002010965", CreditStatus.Debt),
        ClientProfile("49002010976", CreditStatus.Segment(1)),
        ClientProfile("49002010987", CreditStatus.Segment(2)),
        ClientProfile("39012210987", CreditStatus.Segment(2)),
        ClientProfile("35610140435", CreditStatus.Segment(2)),
        ClientProfile("35610140437", CreditStatus.Segment(2)),
        ClientProfile("47812240132", CreditStatus.Segment(2)),
        ClientProfile("35610140437", CreditStatus.Segment(3)),
        ClientProfile("49002010998", CreditStatus.Segment(3))
    )

    override suspend fun getClientByPersonalCode(personalCode: String): ClientProfile {
        val client = registeredClients.firstOrNull { it.personalCode == personalCode }

        if (client != null) {
            return client
        } else {
            throw ClientNotFoundException()
        }
    }

}