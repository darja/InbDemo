package com.darja.inbdemo.domain.repo

/**
 * Provides credit rules details
 */
interface CreditRulesRepository {

    /** @return a credit modifier for the requested segment */
    fun getCreditModifier(segment: Int): Int

    /** @return a minimum loan amount */
    fun getMinLoan(): Int

    /** @return a maximum loan amount */
    fun getMaxLoan(): Int

    /** @return a minimum loan period in months */
    fun getMinPeriod(): Int

    /** @return a maximum loan period in months */
    fun getMaxPeriod(): Int
}