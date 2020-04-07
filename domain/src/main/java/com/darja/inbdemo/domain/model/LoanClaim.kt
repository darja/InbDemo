package com.darja.inbdemo.domain.model

import java.io.Serializable

data class LoanClaim(
    val personalCode: String,
    val amount: Int,
    val periodMonths: Int
): Serializable