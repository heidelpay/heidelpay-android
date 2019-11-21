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

import com.heidelpay.android.backend.BackendServerError
import com.heidelpay.android.types.ServerErrorDetails
import org.junit.Assert
import org.junit.Test

class ServerErrorDetailsTest {

    @Test
    fun testNoElementMapping() {
        Assert.assertNull(ServerErrorDetails.fromBackendErrors(listOf()))
    }

    @Test
    fun testSingleElementMapping() {
        val errorDetails = ServerErrorDetails.fromBackendErrors(listOf(BackendServerError("123", "merchant", "customer")))
        Assert.assertEquals("123", errorDetails?.code)
        Assert.assertEquals("merchant", errorDetails?.merchantMessage)
        Assert.assertEquals("customer", errorDetails?.customerMessage)
    }

    @Test
    fun testMultipleElementMapping() {
        val errorDetails = ServerErrorDetails.fromBackendErrors(listOf(
                BackendServerError("123", "merchant", "customer"),
                BackendServerError("sub1-123", "sub1-merchant", "sub1-customer"),
                BackendServerError("sub2-123", "sub2-merchant", "sub2-customer")
        ))

        Assert.assertEquals("123", errorDetails?.code)
        Assert.assertEquals("merchant", errorDetails?.merchantMessage)
        Assert.assertEquals("customer", errorDetails?.customerMessage)
        Assert.assertEquals(2, errorDetails?.additionalErrorDetails?.count())

        Assert.assertEquals("sub1-123", errorDetails?.additionalErrorDetails?.get(0)?.code)
        Assert.assertEquals("sub1-merchant", errorDetails?.additionalErrorDetails?.get(0)?.merchantMessage)
        Assert.assertEquals("sub1-customer", errorDetails?.additionalErrorDetails?.get(0)?.customerMessage)

        Assert.assertEquals("sub2-123", errorDetails?.additionalErrorDetails?.get(1)?.code)
        Assert.assertEquals("sub2-merchant", errorDetails?.additionalErrorDetails?.get(1)?.merchantMessage)
        Assert.assertEquals("sub2-customer", errorDetails?.additionalErrorDetails?.get(1)?.customerMessage)
    }
}
