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

import com.heidelpay.android.types.PaymentType
import org.junit.Assert
import org.junit.Test

class PaymentTypeTest {

    @Test
    fun testPaymentTypesFromMapList() {
        val list = listOf<Any>(
            mapOf<String, Any>("method" to "paypal", "id" to "A123456"),
            mapOf<String, Any>("method" to "sofort", "id" to "B123456"),
            mapOf<String, Any>("method" to "card", "id" to "B123456", "brand" to "Mastercard", "number" to "****1233")
        )
        val paymentTypes = PaymentType.map(list)
        Assert.assertEquals(3, paymentTypes.count())
        Assert.assertNotNull(paymentTypes.get(2).title)
        Assert.assertEquals("Mastercard", paymentTypes.get(2).title)
        Assert.assertEquals(2, paymentTypes.get(2).data.count())
    }
}
