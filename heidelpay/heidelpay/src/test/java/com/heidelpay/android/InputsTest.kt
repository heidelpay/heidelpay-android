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

import com.heidelpay.android.ui.model.CardExpiryInput
import com.heidelpay.android.ui.model.CreditCardInput
import com.heidelpay.android.ui.model.CvvInput
import com.heidelpay.android.ui.model.IBANInput
import org.junit.Assert
import org.junit.Test
import java.util.*

class InputsTest {

    @Test
    fun testCreditCardInput() {
        Assert.assertEquals("5389 5012 4765 3501", CreditCardInput("5389501247653501").formattedValue)
        Assert.assertEquals("5389 5012 4765 3501", CreditCardInput("53895012476535015389501247653501").formattedValue)
    }

    @Test
    fun testIBANInput() {
        Assert.assertEquals("FR76 3000 6000 0112 3456 7890 189", IBANInput("FR7630006000011234567890189").formattedValue)

        Assert.assertEquals("F", IBANInput("F1").formattedValue)
        Assert.assertEquals("", IBANInput("1").formattedValue)
        Assert.assertEquals("FR", IBANInput("FRR").formattedValue)
        Assert.assertEquals("FR76 3", IBANInput("FR763").formattedValue)
    }

    @Test
    fun testCvvInput() {
        Assert.assertEquals("123", CvvInput("123").formattedValue)
        Assert.assertEquals("123", CvvInput("123456789").formattedValue)
    }

    @Test
    fun testCardExpiryInput() {
        Assert.assertEquals("12/12", CardExpiryInput("1212").formattedValue)

        Assert.assertEquals(1, CardExpiryInput("0102").month)
        Assert.assertEquals(2002, CardExpiryInput("0102").year)
        Assert.assertEquals(-1, CardExpiryInput("1").month)
        Assert.assertEquals("1", CardExpiryInput("1").formattedValue)
        Assert.assertEquals("03/", CardExpiryInput("3").formattedValue)
        Assert.assertEquals("13/", CardExpiryInput("13").formattedValue)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentYearString = "${currentYear-2000}"

        Assert.assertEquals(true, CardExpiryInput("12/99").valid)
        Assert.assertEquals(true, CardExpiryInput("12/${currentYearString}").valid)
        Assert.assertEquals(false, CardExpiryInput("12/13").valid)
        Assert.assertEquals(false, CardExpiryInput("22/99").valid)
        Assert.assertEquals(false, CardExpiryInput("00/99").valid)
    }
}
