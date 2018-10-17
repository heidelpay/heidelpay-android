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

import android.support.test.runner.AndroidJUnit4
import com.heidelpay.android.backend.BackendError
import com.heidelpay.android.types.PaymentMethod
import com.heidelpay.android.types.PublicKey
import com.heidelpay.android.util.MockedBackendErrorResponse
import com.heidelpay.android.util.TestBackendService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HeidelpayTestInstrumented {

    internal val testPublicKey = PublicKey("s-pub-2a10ifVINFAjpQJ9qW8jBe5OJPBx6Gxa")
    internal var testBackendService = TestBackendService()

    private var lock = CountDownLatch(1)

    @Before
    fun setup() {
        Heidelpay.backendServiceCreator = { publicKey, environment ->
            testBackendService
        }

        testBackendService.mock("keypair", """
            {
                "publicKey" : "s-pub-2a10ifVINFAjpQJ9qW8jBe5OJPBx6Gxa",
                "availablePaymentTypes" : [ "card", "sofort" ]
            }
        """.trimIndent())
    }

    @Test
    fun testCreateInstance() {
        var callSucceeded = false
        Heidelpay.setup(testPublicKey, completion = { heidelPay, error ->
            assertNull(error)
            assertNotNull(heidelPay)

            assertEquals(2, heidelPay!!.paymentMethods.count())
            assertTrue(heidelPay.paymentMethods.contains(PaymentMethod.Card))

            callSucceeded = true

            lock.countDown()
        })

        lock.await(100, TimeUnit.MILLISECONDS)

        assertTrue(callSucceeded)
    }

    @Test
    fun testAuthorizeError() {
        var callSucceeded = false

        testBackendService.mock("keypair", MockedBackendErrorResponse(BackendError.ServerHTTPError(401)))

        Heidelpay.setup(testPublicKey, completion = { heidelPay, error ->
            assertNotNull(error)
            assertTrue(error is HeidelpayError.NotAuthorized)

            callSucceeded = true

            lock.countDown()
        })

        lock.await(100, TimeUnit.MILLISECONDS)

        assertTrue(callSucceeded)
    }

    @Test
    fun testCreateCardPayment() {
        var heidelpayInstance : Heidelpay? = null

        var setupCallSucceeded = false

        Heidelpay.setup(testPublicKey, completion = { heidelPay, error ->
            heidelpayInstance = heidelPay

            setupCallSucceeded = true
            lock.countDown()
        })

        lock.await(100, TimeUnit.MILLISECONDS)

        assertTrue(setupCallSucceeded)

        lock = CountDownLatch(1)

        testBackendService.mock("types/card", """
            {
                "id" : "s-crd-avf2geyehjro",
                "method" : "card"
            }
        """.trimIndent())

        var paymentTypeCallSucceeded = false
        heidelpayInstance?.createPaymentCard("4444333322221111", "123", "04/25") { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)
            assertNotNull(paymentType?.paymentId)
            assertEquals(PaymentMethod.Card, paymentType?.method)

            paymentTypeCallSucceeded = true
            lock.countDown()
        }

        lock.await(100, TimeUnit.MILLISECONDS)
        assertTrue(paymentTypeCallSucceeded)
    }
}
