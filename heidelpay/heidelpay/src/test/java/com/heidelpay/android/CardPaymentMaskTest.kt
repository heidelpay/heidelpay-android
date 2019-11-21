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

import com.heidelpay.android.ui.mask
import org.junit.Assert
import org.junit.Test

class CardPaymentMaskTest {

    @Test
    fun testMaskCard() {

        Assert.assertEquals("*1111", "4444333322221111".mask())
        Assert.assertEquals("*2345", "4444333322221111012345".mask())

        Assert.assertEquals("*7890", "1234567890".mask())
        Assert.assertEquals("*2345", "12345".mask())
        Assert.assertEquals("*", "1234".mask())
        Assert.assertEquals("*", "1".mask())

    }
}
