package com.darja.inbdemo.domain.repo

interface CreditRulesRepository {
    fun getCreditModifier(segment: Int): Int
}