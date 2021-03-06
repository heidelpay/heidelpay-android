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

package com.heidelpay.android.backend

import java.net.URL

/**
 * enumeration of the supported environments
 */
enum class Environment {
    /// Production system
    Production;

    /// Host that environment
    val host: String = "api.heidelpay.com"

    /// create an url for a given path
    fun fullURLPathForPath(path: String): URL {
        return URL("https://$host/v1/$path")
    }

    /// public key hash for certificate pinning
    val pinnedPublicKeyHash: String
        get() = "QBOk6PGlUr8Xs8v+DNxHAsJc3Z746CtldyNII4tsEnw="
}
