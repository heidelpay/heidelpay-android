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

import androidx.test.runner.AndroidJUnit4
import com.heidelpay.android.types.PublicKey
import com.heidelpay.android.util.FTBaseTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FTCreatePaymentTypesTest: FTBaseTest() {

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
            assertNotNull(paymentType?.data?.get("brand"))
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
    fun testCreateCardPaymentNot3ds() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentCard("4444333322221111", "123", "04/25", false) { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertNotNull(paymentType?.data?.get("number"))
            assertNotNull(paymentType?.data?.get("brand"))
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
    fun testCreateCardPaymentSet3dsToTrue() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentCard("4444333322221111", "123", "04/25", true) { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)

            assertNotNull(paymentType?.data)
            assertNotNull(paymentType?.data?.get("number"))
            assertNotNull(paymentType?.data?.get("brand"))
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

        heidelPay.createPaymentInvoice { paymentType, error ->
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

        heidelPay.createPaymentSofort { paymentType, error ->
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

        heidelPay.createPaymentGiropay { paymentType, error ->
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

        heidelPay.createPaymentPrepayment { paymentType, error ->
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

        heidelPay.createPaymentPrzelewy24 { paymentType, error ->
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

        heidelPay.createPaymentPayPal { paymentType, error ->
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

    @Test
    fun testCreateAlipay() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentAlipay { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)
            assertNotNull(paymentType?.data)
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }
        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreateWeChatPay() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentWeChatPay { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)
            assertNotNull(paymentType?.data)
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }
        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }

    @Test
    fun testCreatePIS() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentPIS { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)
            assertNotNull(paymentType?.data)
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))

            finishedCall = true

            lock.countDown()
        }
        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }


    @Test
    fun testCreateInvoiceFactoring() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        heidelPay.createPaymentInvoiceFactoring { paymentType, error ->
            assertNotNull(paymentType)
            assertNull(error)
            assertNotNull(paymentType?.data)
            assertNull(paymentType?.data?.get("id"))
            assertNull(paymentType?.data?.get("method"))
            finishedCall = true

            lock.countDown()
        }
        lock.await(10, TimeUnit.SECONDS)

        assertTrue(finishedCall)
    }
}
