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

package com.heidelpay.android.requests

import com.heidelpay.android.backend.HeidelpayDataRequest
import com.heidelpay.android.types.JsonType
import org.json.JSONObject

/**
 * Heidelpay backend request for creating a server object. Might currently be customer or basket.
 */
internal class CreateServerObjectRequest<T : JsonType>(override val requestPath: String, val dataObject: T) : HeidelpayDataRequest {

    override fun encodeToJSON(): JSONObject {
        return dataObject.encodeAsJSON()
    }

    override fun createResponseFromDataString(dataString: String): Any {
        val json = JSONObject(dataString)
        return CreateServerObjectResponse(jsonObject = json)
    }

    override var httpMethod: String = "POST"
}

internal class CreateServerObjectResponse(jsonObject: JSONObject) {
    val id: String

    init {
        this.id = jsonObject.optString("id")
    }
}
