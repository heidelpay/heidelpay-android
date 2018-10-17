/*
 * Copyright (C) 2018 Heidelpay GmbH
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

import com.heidelpay.android.types.PaymentMethod
import com.heidelpay.android.types.PaymentType
import org.json.JSONObject

/**
 * Sepa Direct Debit Payment Type
 * There is a guaranteed and a non guaranteed version of the Sepa Direct Debit Payment type
 * which can be set as an additional constructor parameter. Default is non guaranteed.
 *
 * @property iban: International bank account number (IBAN)
 * @property bic: Bank identification number (only needed for some countries)
 * @property holder: Name of the bank account
 * @property guaranteed: guaranteed flag

 */
internal data class SepaDirectDebitPayment(val iban: String, val bic: String?, val holder: String?, val guaranteed: Boolean = false) : CreatePaymentType {

    /// dependent on the flag guaranteed the method is sepa direct debit guaranteed or not guaranteed
    override val method: PaymentMethod
        get() {
            if (guaranteed) {
                return PaymentMethod.SepaDirectDebitGuaranteed
            }
            return PaymentMethod.SepaDirectDebit
        }


    override fun paymentType(paymentId: String, paymentMethod: PaymentMethod, json: Map<String, Any>): PaymentType {
        return PaymentType(paymentId = paymentId, method = paymentMethod, title = paymentMethod.rawValue, data = mapOf("iban" to iban))
    }

    override fun encodeAsJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("iban", iban)
        return jsonObject
    }
}
