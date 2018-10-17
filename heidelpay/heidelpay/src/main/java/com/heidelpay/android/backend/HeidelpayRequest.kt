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

package com.heidelpay.android.backend

import org.json.JSONObject

/**
 * Interface implemented by all Heidelpay backend request types that don't send data to the backend (i.e. the request body is empty)
 */
internal interface HeidelpayRequest {
    /**
     * Request path of the request
     */
    val requestPath: String

    /**
     * Convert the received data to a response object that belongs to that request
     * @param   dataString  Data received from the backend
     * @return  Response object if successful
     */

    fun createResponseFromDataString(dataString: String): Any
}

/**
 * Interface implemented by all Heidelpay backend request tyeps that send JSON
 */
internal interface HeidelpayDataRequest : HeidelpayRequest {
    fun encodeToJSON(): JSONObject
}
