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

package com.heidelpay.android.requests

import com.heidelpay.android.backend.HeidelpayRequest
import com.heidelpay.android.types.PaymentMethod
import org.json.JSONObject

/**
 * Intial request to verify the public key
 */
internal class SetupRequest : HeidelpayRequest {

    /// request path for the setup request
    override val requestPath: String = "keypair"

    override fun createResponseFromDataString(dataString: String): Any {
        val json = JSONObject(dataString)
        return SetupResponse(jsonObject = json)
    }
}

/**
 * maps the received available payment methods to the ones which
 * are supported by this version of the SDK
 */
internal class SetupResponse(jsonObject: JSONObject) {
    val availablePaymentMethods: List<PaymentMethod>

    init {
        val list = mutableListOf<PaymentMethod>()
        jsonObject.optJSONArray("availablePaymentTypes")?.let { jsonArray ->
            for (index in 0..(jsonArray.length() - 1)) {
                jsonArray.optString(index)?.let { paymentMethodString ->
                    PaymentMethod.fromString(paymentMethodString)?.let {
                        list.add(it)
                    }
                }
            }
        }

        availablePaymentMethods = list
    }
}
