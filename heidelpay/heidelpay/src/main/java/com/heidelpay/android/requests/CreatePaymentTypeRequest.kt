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

package com.heidelpay.android.requests

import com.heidelpay.android.backend.BackendError
import com.heidelpay.android.backend.HeidelpayDataRequest
import com.heidelpay.android.types.PaymentMethod
import com.heidelpay.android.types.paymenttypes.CreatePaymentType
import org.json.JSONObject

/**
 * Heidelpay backend request for creating Payment Types
 *
 * As the Backend scheme for creating payment types is the same for all different
 * kind of payment types this class is capable to create payment types for
 * all supported payment methods.
 *
 * @property type: payment type that shall be created
 */
internal class CreatePaymentTypeRequest(val type: CreatePaymentType) : HeidelpayDataRequest {

    /// request path which depends on the payment method to be created
    override val requestPath: String = "types/${type.method.rawValue}"

    override fun encodeToJSON(): JSONObject {
        return type.encodeAsJSON()
    }

    override fun createResponseFromDataString(dataString: String): Any {
        val json = JSONObject(dataString)
        val response = CreatePaymentTypeResponse(jsonObject = json)

        PaymentMethod.fromString(response.method)?.let {
            return type.paymentType(paymentId = response.id, paymentMethod = it, json = mapOf())
        }
        throw BackendError.InvalidRequest()
    }
}

/**
 * helper type to map the server side JSON to the SDK
 * element PaymentTypeId
 */
internal class CreatePaymentTypeResponse(jsonObject: JSONObject) {
    val id: String
    val method: String

    init {
        this.id = jsonObject.optString("id")
        this.method = jsonObject.optString("method")

    }
}
