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

import com.heidelpay.android.types.PaymentType
import com.heidelpay.android.types.PublicKey

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class PublicKeyTestInstrumented {

    @Test
    fun testCreate() {
        val key = PublicKey("s-pub-1234567890")
        assertEquals(key.authorizationHeaderValue, "Basic cy1wdWItMTIzNDU2Nzg5MDo=")
    }

    @Test
    fun testVerifyKey() {
        assertTrue(PublicKey.isPublicKey("s-pub-1234567890"))
        assertTrue(PublicKey.isPublicKey("p-pub-1234567890"))

        assertFalse(PublicKey.isPublicKey("s-PUB-1234567890"))
        assertFalse(PublicKey.isPublicKey("S-PUB-1234567890"))
        assertFalse(PublicKey.isPublicKey("s-pub1234567890"))

        assertFalse(PublicKey.isPublicKey("s-priv-1234567890"))
        assertFalse(PublicKey.isPublicKey("s-what-1234567890"))
        assertFalse(PublicKey.isPublicKey("s-Non-1234567890"))
    }
}
