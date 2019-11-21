/*
 * Copyright (C) 2019 Heidelpay GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heidelpay.android.types.paymenttypes

import android.os.Parcel
import android.os.Parcelable
import com.heidelpay.android.types.PaymentMethod
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

/// Hire Purchase Installment Rate
/// Holds the values of an individual rate and belongs to Hire Purchase Plan
@Parcelize
class HirePurchaseInstallmentRate(var amountOfRepayment: Float = 0f, var rate: Float = 0f, var totalRemainingAmount: Float = 0f, var type: String = "", var rateIndex: Int = 0, var ultimo: Boolean = false) : Parcelable {

    internal constructor(jsonObject: JSONObject) : this() {
        amountOfRepayment = jsonObject.getDouble("amountOfRepayment").toFloat()
        rate = jsonObject.getDouble("rate").toFloat()
        totalRemainingAmount = jsonObject.getDouble("totalRemainingAmount").toFloat()
        type = jsonObject.getString("type")
        rateIndex = jsonObject.getInt("rateIndex")
        ultimo = jsonObject.getBoolean("ultimo")
    }
}

/// Hire Purchase Installment Plan
@Parcelize
class HirePurchasePlan(var numberOfRates: Int = 0, var dayOfPurchase: String = "", var totalPurchaseAmount: Double = 0.0, var totalInterestAmount: Double = 0.0, var totalAmount: Double = 0.0, var effectiveInterestRate: Double = 0.0, var nominalInterestRate: Double = 0.0, var feeFirstRate: Double = 0.0, var feePerRate: Double = 0.0, var monthlyRate: Double = 0.0, var lastRate: Double = 0.0, var installmentRates: ArrayList<HirePurchaseInstallmentRate> = arrayListOf()) : Parcelable {

    internal constructor(jsonObject: JSONObject) : this() {
        numberOfRates = jsonObject.getInt("numberOfRates")
        dayOfPurchase = jsonObject.getString("dayOfPurchase")
        totalPurchaseAmount = jsonObject.getDouble("totalPurchaseAmount")
        totalInterestAmount = jsonObject.getDouble("totalInterestAmount")
        totalAmount = jsonObject.getDouble("totalAmount")
        effectiveInterestRate = jsonObject.getDouble("effectiveInterestRate")
        nominalInterestRate = jsonObject.getDouble("nominalInterestRate")
        feeFirstRate = jsonObject.getDouble("feeFirstRate")
        feePerRate = jsonObject.getDouble("feePerRate")
        monthlyRate = jsonObject.getDouble("monthlyRate")
        lastRate = jsonObject.getDouble("lastRate")

        val installmentRatesJson = jsonObject.getJSONArray("installmentRates")

        installmentRates = arrayListOf()
        if (installmentRatesJson != null) {
            for (i in 0 until installmentRatesJson.length()) {
                installmentRates.add(HirePurchaseInstallmentRate(installmentRatesJson.getJSONObject(i)))
            }
        }
    }
}

/**
 * Hire Purchase Payment Type
 *
 */
internal class HirePurchasePayment
/**
 * Init HirePurchase Payment
 * @param iban: International bank account number (IBAN)
 * @param bic: Bank identification number
 * @param holder: Name of the bank account
 * @param plan: Hire purchase plan
 */(
        val iban: String,
        val bic: String,
        val holder: String, plan: HirePurchasePlan) : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.HirePurchase

    // Other properties are mapped from the provided plan
    val numberOfRates: Int
    val effectiveInterestRate: Double
    val nominalInterestRate: Double
    val totalPurchaseAmount: Double
    val totalInterestAmount: Double
    val totalAmount: Double
    val feeFirstRate: Double
    val feePerRate: Double
    val monthlyRate: Double
    val lastRate: Double
    val dayOfPurchase: String

    init {
        this.numberOfRates = plan.numberOfRates
        this.effectiveInterestRate = roundedTo2Commas(plan.effectiveInterestRate)
        this.nominalInterestRate = roundedTo2Commas(plan.nominalInterestRate)
        this.totalPurchaseAmount = roundedTo2Commas(plan.totalPurchaseAmount)
        this.totalInterestAmount = roundedTo2Commas(plan.totalInterestAmount)
        this.totalAmount = roundedTo2Commas(plan.totalAmount)
        this.feeFirstRate = roundedTo2Commas(plan.feeFirstRate)
        this.feePerRate = roundedTo2Commas(plan.feePerRate)
        this.monthlyRate = roundedTo2Commas(plan.monthlyRate)
        this.lastRate = roundedTo2Commas(plan.lastRate)
        this.dayOfPurchase = plan.dayOfPurchase
    }

    override fun encodeAsJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("iban", iban)
        jsonObject.put("bic", bic)
        jsonObject.put("holder", holder)

        jsonObject.put("numberOfRates", numberOfRates)
        jsonObject.put("effectiveInterestRate", effectiveInterestRate)
        jsonObject.put("nominalInterestRate", nominalInterestRate)
        jsonObject.put("totalPurchaseAmount", totalPurchaseAmount)
        jsonObject.put("totalInterestAmount", totalInterestAmount)
        jsonObject.put("totalAmount", totalAmount)
        jsonObject.put("feeFirstRate", feeFirstRate)
        jsonObject.put("feePerRate", feePerRate)
        jsonObject.put("monthlyRate", monthlyRate)
        jsonObject.put("lastRate", lastRate)
        jsonObject.put("dayOfPurchase", dayOfPurchase)
        return jsonObject
    }

    companion object {
        private fun roundedTo2Commas(value: Double): Double {
            var bd = BigDecimal(value)
            bd = bd.setScale(2, RoundingMode.HALF_UP)
            return bd.toDouble()
        }
    }

}
