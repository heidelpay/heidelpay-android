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

import com.heidelpay.android.backend.HeidelpayDataRequest
import com.heidelpay.android.types.paymenttypes.HirePurchasePlan
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/// heidelpay backend request for retrieving Hire Purchase Plans
internal class RetrieveHirePurchasePlansRequest : HeidelpayDataRequest {
    override val requestPath: String

    constructor(amount: Float, currencyCode: String, effectiveInterest: Float, orderDate: Date? = null) {
        var path = "types/hire-purchase-direct-debit/plans?amount=${amount}&currency=${currencyCode}"
        path = path + "&effectiveInterest=${effectiveInterest}"
        if (orderDate != null) {
            val simpleFormatter = SimpleDateFormat("yyyy-MM-dd")
            path = path + "&orderDate=${simpleFormatter.format(orderDate)}"
        }
        requestPath = path
    }

    override var httpMethod: String = "GET"

    override fun encodeToJSON(): JSONObject? {
        return null
    }

    override fun createResponseFromDataString(dataString: String): Any {
        val json = JSONObject(dataString)
        val response = RetrieveHirePurchasePlansResponse(jsonObject = json)

        return response
    }
}

/**
 * heidelpay backend response for Hire Purchase Plans
 */
internal class RetrieveHirePurchasePlansResponse(jsonObject: JSONObject) {
    val code: String
    val entity: ArrayList<HirePurchasePlan>

    init {
        this.code = jsonObject.optString("code")
        this.entity = ArrayList()
        val entities = jsonObject.optJSONArray("entity")
        if (entities != null) {
            for (i in 0 until entities.length()) {
                entity.add(HirePurchasePlan(entities.getJSONObject(i)))
            }
        }
    }
}
