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
import com.heidelpay.android.requests.CreatePaymentTypeRequest
import com.heidelpay.android.requests.CreateServerObjectRequest
import com.heidelpay.android.requests.CreateServerObjectResponse
import com.heidelpay.android.types.Customer
import com.heidelpay.android.types.JsonType
import com.heidelpay.android.types.PaymentType
import com.heidelpay.android.types.paymenttypes.*

/**
 * Create a customer instance on the Heidelpay server
 *
 * @param customer: a customer object
 * @param completion: the completion handler
 */
fun Heidelpay.createCustomer(customer: Customer, completion: HeidelpayCreateServerObjectCompletion) {
    performServerSideCreation("customers", customer, completion)
}

private fun <T : JsonType> Heidelpay.performServerSideCreation(requestPath: String, dataObject: T, completion: HeidelpayCreateServerObjectCompletion) {

    val serverObjectRequest = CreateServerObjectRequest(requestPath, dataObject)

    backendService.performRequest(serverObjectRequest, completion = { response, error ->
        (response as? CreateServerObjectResponse)?.let {
            Heidelpay.executeOnMain { completion(it.id, null) }
            return@performRequest
        }
        error?.let {
            Heidelpay.executeOnMain { completion(null, HeidelpayError.mapFromBackendError(error)) }
            return@performRequest
        }
        Heidelpay.executeOnMain { completion(null, HeidelpayError.GeneralProcessingError()) }
    })
}

typealias HeidelpayCreateServerObjectCompletion = (customerId: String?, error: Any?) -> Unit
