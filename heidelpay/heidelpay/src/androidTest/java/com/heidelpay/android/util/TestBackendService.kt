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

import com.heidelpay.android.backend.BackendError
import com.heidelpay.android.backend.BackendService
import com.heidelpay.android.backend.HeidelpayBackendRequestCompletion
import com.heidelpay.android.backend.HeidelpayRequest

interface MockedResponse

internal data class MockedJSONResponse(val json: String) : MockedResponse

internal data class MockedBackendErrorResponse(val backendError: BackendError) : MockedResponse

internal class TestBackendService : BackendService {

    private var mockedPaths: MutableMap<String, MockedResponse> = mutableMapOf()

    fun mock(path: String, response: MockedResponse) {
        mockedPaths.set(path, response)
    }

    fun mock(path: String, jsonResponse: String) {
        mockedPaths.set(path, MockedJSONResponse(jsonResponse))
    }

    override fun performRequest(request: HeidelpayRequest, completion: HeidelpayBackendRequestCompletion) {
        mockedPaths.get(request.requestPath)?.let {
            if (it is MockedBackendErrorResponse) {
                completion(null, it.backendError)
            } else if (it is MockedJSONResponse) {
                completion(request.createResponseFromDataString(it.json), null)
            } else {
                completion(null, BackendError.InvalidRequest())
            }
            return
        }
        completion(null, BackendError.InvalidRequest())
    }

}
