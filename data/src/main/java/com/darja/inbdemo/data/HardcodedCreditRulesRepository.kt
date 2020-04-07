package com.darja.inbdemo.data

import com.darja.inbdemo.domain.repo.CreditRulesRepository

class HardcodedCreditRulesRepository: CreditRulesRepository {
    override fun getCreditModifier(segment: Int): Int {
        return when (segment) {
            1 -> 100
            2 -> 300
            3 -> 1000
            else -> throw IllegalArgumentException("Unexpected segment: $segment")
        }
    }

    override fun getMinLoan() = 2000

    override fun getMaxLoan() = 10000

    override fun getMinPeriod() = 12

    override fun getMaxPeriod() = 60
}