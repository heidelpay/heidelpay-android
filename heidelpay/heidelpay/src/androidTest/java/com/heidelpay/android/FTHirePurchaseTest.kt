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
import com.heidelpay.android.types.paymenttypes.HirePurchasePlan
import com.heidelpay.android.util.FTBaseTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/// Functional Tests of the heidelpay SDK with real backend!
///
/// **Note**: This tests may fail in case of changes in the backend. Nothing is mocked.
///
@RunWith(AndroidJUnit4::class)
class FTHirePurchaseTest: FTBaseTest() {

    @Test
    fun testRetrieveHirePurchasePlansAndCreatePaymentType() {
        val heidelPay = setupHeidelpay()

        val lock = CountDownLatch(1)

        var finishedCall = false

        var firstPlan: HirePurchasePlan? = null
        heidelPay.retrieveHirePurchasePlans(amount = 100.0f, currencyCode = "EUR", effectiveInterest = 5.99f) { plans, error ->

            Assert.assertNotNull(plans)
            Assert.assertTrue(plans!!.size > 0)

            firstPlan = plans.firstOrNull()

            finishedCall = true

            lock.countDown()
        }
        lock.await(10, TimeUnit.SECONDS)

        Assert.assertTrue(finishedCall)

        Assert.assertNotNull(firstPlan)

        val lockCreate = CountDownLatch(1)

        var finishedCreateCall = false

        heidelPay.createPaymentHirePurchase(iban = "DE46940594210000012345", bic = "COBADEFFXXX", holder = "Manuel Weißmann", plan = firstPlan!!) { paymentType, error ->

            Assert.assertNotNull(paymentType)
            Assert.assertNull(error)
            Assert.assertNotNull(paymentType?.data)
            Assert.assertNull(paymentType?.data?.get("id"))
            Assert.assertNull(paymentType?.data?.get("method"))

            finishedCreateCall = true

            lockCreate.countDown()
        }
        lockCreate.await(10, TimeUnit.SECONDS)
        Assert.assertTrue(finishedCreateCall)
    }

}
