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

package com.heidelpay.android

import android.os.Handler
import android.os.Looper
import com.heidelpay.android.backend.BackendService
import com.heidelpay.android.backend.Environment
import com.heidelpay.android.backend.HttpBackendService
import com.heidelpay.android.backend.mapFromBackendError
import com.heidelpay.android.requests.CreatePaymentTypeRequest
import com.heidelpay.android.requests.SetupRequest
import com.heidelpay.android.requests.SetupResponse
import com.heidelpay.android.types.PaymentMethod
import com.heidelpay.android.types.PaymentType
import com.heidelpay.android.types.PublicKey
import com.heidelpay.android.types.paymenttypes.CreatePaymentType

/**
 * Definition of the completion lambda expression which is called after a payment type create request completed
 *
 * When creation succeeded the first parameter holds a `PaymentTypeId` element which references
 * the newly created payment type. The information of this payment type id can be used on your
 * server part to trigger a charge.
 *
 * In case of an error the first parameter is nil and the second parameter holds an error object
 * with more details about the problem.
 *
 * It is guaranteed that only one of the two parameters is nil
 */
typealias HeidelpayCreatePaymentCompletion = (paymentType: PaymentType?, error: Any?) -> Unit

/**
 * Definition for the completion lambda expression which is called after setup is completed
 *
 * When setup succeeded the completion handler is called with a Heidelpay
 * instance as the first parameter and nil for the second parameter.
 * In case of an error the first parameter is nil and the second parameter
 * holds an error object of type {@link HeidelpayError}.
 */
typealias HeidelpaySetupCompletion = (heidelpay: Heidelpay?, error: HeidelpayError?) -> Unit

/**
 * Entry class of the Heidelpay SDK
 *
 * The Heidelpay element allows you to create payment types in a secure way.
 *
 * In order to use the Heidelpay system you have to setup an instance with the
 * PublicKey that is provided to you bei Heidelpay.
 *
 * An initialized instance is stored and available through the sharedInstance property of
 * the Heidelpay class.
 *
 * ## Note:
 * For security reasons the private key must not be used on the client side.
 * The private key must kept private on your server side.
 */
class Heidelpay {

    /// backend service used by this instance
    private val backendService: BackendService

    /// available payment methods supported by the SDK and provided by the Heidelpay backend
    val paymentMethods: List<PaymentMethod>

    internal constructor(backendService: BackendService, paymentMethods: List<PaymentMethod>) {
        this.backendService = backendService
        this.paymentMethods = paymentMethods
    }

    companion object {

        /// helper method for unit testing to mock the backend service
        internal var backendServiceCreator: (publicKey: PublicKey, environment: Environment) -> BackendService = { publicKey, environment ->
            HttpBackendService(publicKey = publicKey, environment = environment)
        }

        /**
         * setup a Heidelpay instance
         *
         * The completion lambda expression is called with a Heidelpay instance if setup succeeded. The
         * second parameter is nil.
         * In case of an error the first parameter is nil and the second parameter holds an
         * element of type HeidelpayError. It's guaranteed that only one of the two parameter is nil.
         *
         * @param publicKey: the public key of the merchant
         * @param completion: completion lambda expression that will be called in case of failure and success
         *
         */
        fun setup(publicKey: PublicKey, completion: HeidelpaySetupCompletion) {
            val backendService = backendServiceCreator(publicKey, Environment.Production)

            backendService.performRequest(request = SetupRequest(), completion = { response, error ->
                (response as? SetupResponse)?.let {
                    executeOnMain { completion(Heidelpay(backendService, it.availablePaymentMethods), null) }
                    return@performRequest
                }
                error?.let {
                    executeOnMain { completion(null, HeidelpayError.mapFromBackendError(it)) }
                    return@performRequest
                }
                executeOnMain { completion(null, HeidelpayError.GeneralProcessingError()) }
            })
        }

        /// execute the given block on the main thread
        internal fun executeOnMain(expression: () -> Unit) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                expression()
            }
        }
    }

    /**
     * Create a new payment type
     *
     * The completion lambda expression is either called with a PaymentTypeId element which references the created
     * payment type or with an error. It's guaranteed that only one of the two parameters is not nil.
     *
     * @param type: Payment Type to create
     * @param completion: HeidelpayCreatePaymentCompletion that will be called in case of failure and success
     */
    internal fun createPayment(type: CreatePaymentType, completion: HeidelpayCreatePaymentCompletion) {
        backendService.performRequest(CreatePaymentTypeRequest(type), completion = { response, error ->
            (response as? PaymentType)?.let {
                executeOnMain { completion(it, null) }
                return@performRequest
            }
            error?.let {
                executeOnMain { completion(null, HeidelpayError.mapFromBackendError(error)) }
                return@performRequest
            }
            executeOnMain { completion(null, HeidelpayError.GeneralProcessingError()) }
        })
    }
}


