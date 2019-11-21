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
import com.heidelpay.android.types.PaymentType
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class PaymentTypeTestInstrumented {

    @Test
    @Throws(Exception::class)
    fun paymentTypesFromJSONArray() {
        val jsonArray = JSONArray()
        val obj1 = JSONObject()
        obj1.put("method", "paypal")
        obj1.put("id", "A123456")
        jsonArray.put(obj1)

        val obj2 = JSONObject()
        obj2.put("method", "sofort")
        obj2.put("id", "B123456")
        jsonArray.put(obj2)

        val obj3 = JSONObject()
        obj3.put("method", "card")
        obj3.put("id", "B123456")
        obj3.put("brand", "Mastercard")
        obj3.put("number", "****1233")
        jsonArray.put(obj3)

        val paymentTypes = PaymentType.map(jsonArray)
        assertEquals(3, paymentTypes.size.toLong())
        assertNotNull(paymentTypes[2].title)
        assertEquals("Mastercard", paymentTypes[2].title)
        assertEquals(2, paymentTypes[2].data.size.toLong())
    }
}
