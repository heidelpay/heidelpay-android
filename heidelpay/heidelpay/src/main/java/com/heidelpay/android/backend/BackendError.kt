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

import com.heidelpay.android.HeidelpayError
import com.heidelpay.android.types.ServerErrorDetails
import org.json.JSONObject

/**
 * enumeration of errors from the Backend Service
 */
internal sealed class BackendError : Exception() {

    /// the request data or url is invalid and a URL can't be created
    class InvalidRequest : BackendError()

    /// the server data are in a format that wasn't expected
    class InvalidServerResponse : BackendError()

    /// the system reported that there is no connection to the internet
    class NoInternet : BackendError()

    /// request failed on a system level. server may not be reached at all
    class RequestFailed(val error: Exception) : BackendError()

    /// the server was reached and responded with a HTTP error code >= 400
    class ServerHTTPError(val httpCode: Int) : BackendError()

    // the server was reached and responded with a JSON error object which was parsed
    // and the concrete error information is provided as an arry of type ServerError
    class ServerResponseError(val errors: List<BackendServerError>) : BackendError()

    /// equals implementation
    override fun equals(other: Any?): Boolean {
        if (other is BackendError) {
            if (this is InvalidRequest && other is InvalidRequest) return true
            if (this is InvalidServerResponse && other is InvalidServerResponse) return true
            if (this is NoInternet && other is NoInternet) return true
            if (this is RequestFailed && other is RequestFailed) return this.error == other.error
            if (this is ServerHTTPError && other is ServerHTTPError) return this.httpCode == other.httpCode
        }
        return false
    }
}

/// map a BackendError to a HeidelpayError
internal fun HeidelpayError.Companion.mapFromBackendError(backendError: BackendError): HeidelpayError {
    when (backendError) {
        is BackendError.InvalidRequest, is BackendError.InvalidServerResponse, is BackendError.RequestFailed ->
            return HeidelpayError.GeneralProcessingError()
        is BackendError.NoInternet -> return HeidelpayError.NoInternetConnection()
        is BackendError.ServerHTTPError ->
            if (backendError.httpCode == 401 || backendError.httpCode == 403) {
                return HeidelpayError.NotAuthorized()
            } else {
                return HeidelpayError.GeneralProcessingError()
            }
        is BackendError.ServerResponseError -> {
            ServerErrorDetails.fromBackendErrors(backendError.errors)?.let {
                return HeidelpayError.ServerError(it)
            }
            return HeidelpayError.GeneralProcessingError()
        }

    }
}

/**
 * type used to decode the error json of the server
 */
internal class BackendServerErrorResponse(val url: String, val timestamp: String, val errors: List<BackendServerError>) {

    companion object {
        fun fromJSONString(jsonString: String): BackendServerErrorResponse? {
            try {
                val jsonObject = JSONObject(jsonString)
                if (jsonObject.optString("url") != null &&
                        jsonObject.optString("timestamp") != null &&
                        jsonObject.optJSONArray("errors") != null) {
                    val errors = mutableListOf<BackendServerError>()

                    val errorsJSONArray = jsonObject.optJSONArray("errors")
                    for (index in 0..(errorsJSONArray.length() - 1)) {
                        BackendServerError.fromJSONObject(errorsJSONArray.optJSONObject(index))?.let {
                            errors.add(it)
                        }
                    }

                    return BackendServerErrorResponse(jsonObject.optString("url"), jsonObject.optString("timestamp"), errors)
                }
            } catch (e: Exception) {
            }

            return null
        }
    }
}

/**
 * type used to decode the error json of the server
 */
internal class BackendServerError(val code: String, val merchantMessage: String, val customerMessage: String) {

    companion object {
        fun fromJSONObject(jsonObject: JSONObject): BackendServerError? {
            try {
                if (jsonObject.optString("code") != null &&
                        jsonObject.optString("merchantMessage") != null &&
                        jsonObject.optString("customerMessage") != null) {
                    return BackendServerError(jsonObject.optString("code"),
                            jsonObject.optString("merchantMessage"),
                            jsonObject.optString("customerMessage"))
                }
            } catch (e: Exception) {
            }
            return null
        }
    }
}
