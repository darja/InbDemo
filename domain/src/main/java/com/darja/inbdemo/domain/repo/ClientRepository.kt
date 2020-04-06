package com.darja.inbdemo.domain.repo

import com.darja.inbdemo.domain.model.ClientProfile

/**
 * Data source that provides clients details
 */
interface ClientRepository {
    @Throws(ClientNotFoundException::class)
    suspend fun getClientByPersonalCode(personalCode: String): ClientProfile
}

