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

import com.heidelpay.android.types.PaymentMethod
import com.heidelpay.android.types.PaymentType
import org.json.JSONObject

/**
 * Ideal Payment Type
 *
 * @property bic: bic
 */
internal data class IdealPayment(val bic: String) : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.Ideal


    override fun paymentType(paymentId: String, paymentMethod: PaymentMethod, json: Map<String, Any>): PaymentType {
        return PaymentType(paymentId = paymentId, method = paymentMethod, title = paymentMethod.rawValue, data = mapOf("bic" to bic))
    }

    override fun encodeAsJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("bic", bic)
        return jsonObject
    }
}
