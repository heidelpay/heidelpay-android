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

package com.heidelpay.android

import com.heidelpay.android.backend.mapFromBackendError
import com.heidelpay.android.requests.RetrieveHirePurchasePlansRequest
import com.heidelpay.android.requests.RetrieveHirePurchasePlansResponse
import com.heidelpay.android.types.paymenttypes.HirePurchasePlan
import java.util.*
import kotlin.collections.ArrayList

typealias RetrieveHirePurchasePlansCompletion = (hirePurchasePlans: ArrayList<HirePurchasePlan>?, error: HeidelpayError?) -> Unit

/**
 * Retrieve the available plans for hire purchase
 * @param amount: specifies the total amount to be paid in monthly installments
 * @param currencyCode: currency code in which the installments will be paid (e.g. EUR)
 * @param effectiveInterest: effective interest rate of the monthly installment payments.
///                                The range is tied to your merchant configuration.
 * @param orderDate: optional order date
 * @param completion: handler that is called in success and failure case
 */
fun Heidelpay.retrieveHirePurchasePlans(amount: Float, currencyCode: String, effectiveInterest: Float, orderDate: Date? = null, completion: RetrieveHirePurchasePlansCompletion) {

    val request = RetrieveHirePurchasePlansRequest(amount = amount, currencyCode = currencyCode, effectiveInterest = effectiveInterest, orderDate = orderDate)

    backendService.performRequest(request = request, completion = { response, error ->
        (response as? RetrieveHirePurchasePlansResponse)?.let {
            Heidelpay.executeOnMain { completion(response.entity, null) }
            return@performRequest
        }
        error?.let {
            Heidelpay.executeOnMain { completion(null, HeidelpayError.mapFromBackendError(it)) }
            return@performRequest
        }
        Heidelpay.executeOnMain { completion(null, HeidelpayError.GeneralProcessingError()) }
    })

}
