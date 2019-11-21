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
import com.heidelpay.android.types.Customer
import com.heidelpay.android.types.CustomerAddress
import com.heidelpay.android.util.FTBaseTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FTCreateCustomerTest : FTBaseTest() {

    @Test
    fun testCreateMinimalCustomer() {

        val heidelPay = setupHeidelpay()

        val customer = Customer.createCustomer("John", "Doe")

        val lock = CountDownLatch(1)
        var finishedCall = false

        heidelPay.createCustomer(customer) { customerId, error ->
            Assert.assertNotNull(customerId)
            Assert.assertNull(error)

            finishedCall = true
            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)
        Assert.assertTrue(finishedCall)
    }

    @Test
    fun testCreateCustomerWithAllDetails() {

        val heidelPay = setupHeidelpay()

        val billingAddress = CustomerAddress("John Doe", "Doe Street 1", "VI", "1010", "Vienna", "AT")
        val shippingAddress = CustomerAddress("John Doe Home", "John Street 2", "VI", "1190", "Vienna", "AT")
        val uniqueCustomerId = "test-${Date().time}"
        val customer = Customer.createCustomer("John", "Doe", uniqueCustomerId, "mr", "Doe's", "01.01.1970", "john.doe@does.com", "012345", "123456", billingAddress, shippingAddress)

        val lock = CountDownLatch(1)
        var finishedCall = false

        heidelPay.createCustomer(customer) { customerId, error ->
            Assert.assertNotNull(customerId)
            Assert.assertNull(error)

            finishedCall = true
            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)
        Assert.assertTrue(finishedCall)
    }

    @Test
    fun testCreateRegisteredCompanyCustomer() {

        val heidelPay = setupHeidelpay()

        val billingAddress = CustomerAddress("John Doe", "Doe Street 1", "VI", "1010", "Vienna", "AT")
        val customer = Customer.createRegisteredCompanyCustomer("Sample Company", "FN 123456", billingAddress)

        val lock = CountDownLatch(1)
        var finishedCall = false

        heidelPay.createCustomer(customer) { customerId, error ->
            Assert.assertNotNull(customerId)
            Assert.assertNull(error)

            finishedCall = true
            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)
        Assert.assertTrue(finishedCall)
    }

    @Test
    fun testCreateNonRegisteredCompanyCustomer() {

        val heidelPay = setupHeidelpay()

        val billingAddress = CustomerAddress("John Doe", "Doe Street 1", "VI", "1010", "Vienna", "AT")
        val customer = Customer.createNonRegisteredCompanyCustomer("John", "Doe", "Doe Company", "01.01.1970", "john@doe.company.com", billingAddress, "OWNER", "AIRPORT")

        val lock = CountDownLatch(1)
        var finishedCall = false

        heidelPay.createCustomer(customer) { customerId, error ->
            Assert.assertNotNull(customerId)
            Assert.assertNull(error)

            finishedCall = true
            lock.countDown()
        }

        lock.await(10, TimeUnit.SECONDS)
        Assert.assertTrue(finishedCall)
    }

}
