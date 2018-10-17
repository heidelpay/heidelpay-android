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

internal typealias HeidelpayBackendRequestCompletion = (response: Any?, error: BackendError?) -> Unit

/**
 * Interface of the BackendService which is responsible to create and execute
 * backend requests
 *
 * The protocol allows the service to be mocked for unit testing
 */
internal interface BackendService {

    /**
     * perform a request on the backend
     *
     * @param request: the request to be performed
     * @param completion: Lambda expression that is called after completion. In case of success the response element
     * which belongs to the given request is provided as first parameter. The second
     * parameter will be nil.
     * In case of an error a BackendError is provided as second parameter and the first
     * parameter is nil.
     */
    fun performRequest(request: HeidelpayRequest, completion: HeidelpayBackendRequestCompletion)
}
