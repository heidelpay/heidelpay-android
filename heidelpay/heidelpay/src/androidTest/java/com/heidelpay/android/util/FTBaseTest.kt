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

package com.heidelpay.android.util

import com.heidelpay.android.Heidelpay
import com.heidelpay.android.types.PublicKey
import org.junit.Assert
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

open class FTBaseTest {
    fun setupHeidelpay(): Heidelpay {
        val key = PublicKey("s-pub-2a10ifVINFAjpQJ9qW8jBe5OJPBx6Gxa")

        val lock = CountDownLatch(1)

        var heidelpay: Heidelpay? = null

        Heidelpay.setup(key) { heidelPayInstance, error ->
            Assert.assertNull(error)
            Assert.assertNotNull(heidelPayInstance)

            heidelpay = heidelPayInstance

            lock.countDown()
        }
        lock.await(5, TimeUnit.SECONDS)

        Assert.assertNotNull(heidelpay)

        return heidelpay!!
    }
}
