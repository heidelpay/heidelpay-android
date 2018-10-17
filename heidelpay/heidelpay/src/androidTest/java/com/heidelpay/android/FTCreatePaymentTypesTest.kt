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
import com.heidelpay.android.types.PublicKey
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FTCreatePaymentTypesTest {

    fun setupHeidelpay(): Heidelpay {
        val key = PublicKey("s-pub-2a10ifVINFAjpQJ9qW8jBe5OJPBx6Gxa")

        val lock = CountDownLatch(1)

        var heidelpay: Heidelpay? = null

        Heidelpay.setup(key) { heidelPayInstance, error ->
            assertNull(error)
            assertNotNull(heidelPayInstance)

            heidelpay = heidelPayInstance

            lock.countDown()
        }
        lock.await(5, TimeUnit.SECONDS)

        assertNotNull(heidelpay)

        return heidelpay!!
    }

    @Test
    fun testSetup() {
        setupHeidelpay()
    }

    @Test
    fun testCreateCardPayment() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentCard("4444333322221111", "123", "04/25") { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertNotNull(paymentType?.data?.get("number"))
            assertNotNull(paymentType?.data?.get("expiryDate"))
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateSepaDirectDebit() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentSepaDirect("DE89370400440532013000", null, null) { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertNotNull(paymentType?.data?.get("iban"))
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateSepaDirectDebitGuaranteed() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentSepaDirect("DE89370400440532013000", null, null, true) { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertNotNull(paymentType?.data?.get("iban"))
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateInvoice() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentInvoice() { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateInvoiceGuaranteed() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentInvoice(true) { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateSofort() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentSofort() { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateGiropay() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentGiropay() { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreatePrepayment() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentPrepayment() { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreatePrzelewy24() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentPrzelewy24() { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreatePaypal() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentPayPal() { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(0, paymentType?.data?.count())
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateIdeal() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentIdeal("RABONL2U") { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertEquals(1, paymentType?.data?.count())
            assertNotNull(paymentType?.data?.get("bic"))
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }
}
