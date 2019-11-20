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

package com.heidelpay.android.types

import android.util.Base64


/**
 * Public key to authenticate against the Heidelpay backend
 */
class PublicKey {

    /// value of Authorization header for backend requests
    val authorizationHeaderValue: String

    private val publicKey: String

    /**
     * create a public key out of a string
     *
     * @param publicKey: public key of the merchant
     *
     * If the provided key is not in the correct format the
     * application will end with an **exception **.
     *
     * ## Note:
     * Use the `PublicKey.isPublicKey` function if you want to check if the key
     * is in the correct format of a public key.
     */
    constructor(publicKey: String) {
        this.publicKey = publicKey

        if (PublicKey.isPublicKey(publicKey) == false) {
            throw RuntimeException("Invalid Public Key")
        }

        this.authorizationHeaderValue = "Basic ${Base64.encodeToString("$publicKey:".toByteArray(Charsets.UTF_8), Base64.DEFAULT).trim()}"
    }

    companion object {
        /**
         * verifies if the provided key has the expected public key format
         *
         * @param key: key that shall be verified
         * @return true if the provided key is in the public key format
         */
        fun isPublicKey(key: String): Boolean {
            val isConvertibleToUTF8Data = key.toByteArray(Charsets.UTF_8).isNotEmpty()
            return (key.startsWith("s-pub-") || key.startsWith("p-pub")) && isConvertibleToUTF8Data
        }
    }
}
