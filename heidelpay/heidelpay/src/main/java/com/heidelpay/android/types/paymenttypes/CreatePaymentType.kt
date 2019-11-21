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

import com.heidelpay.android.types.JsonType
import com.heidelpay.android.types.PaymentMethod
import com.heidelpay.android.types.PaymentType
import org.json.JSONObject

/**
 * interface implemented by all concrete payment types like CardPayment for creation
 */
interface CreatePaymentType: JsonType {

    /// payment method of that payment type
    val method: PaymentMethod

    override fun encodeAsJSON(): JSONObject {
        return JSONObject()
    }

    /**
     * create a PaymentType
     *
     * @param paymentId: id of the Payment Type created
     * @param paymentMethod: method of the Payment Type created
     * @param json: additional data parameters provided by the backend
     * @return a PaymentType element
     */
    fun paymentType(paymentId: String, paymentMethod: PaymentMethod, json: Map<String, Any>): PaymentType {
        return PaymentType(paymentId = paymentId, method = paymentMethod, title = paymentMethod.rawValue, data = json)
    }
}
