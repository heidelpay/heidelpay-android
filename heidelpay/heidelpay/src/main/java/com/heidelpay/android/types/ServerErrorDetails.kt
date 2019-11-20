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

import com.heidelpay.android.backend.BackendServerError

/**
 * Details of a received Server Error.
 *
 * The messages are localized for the language configured in the
 * device settings and provided by the system (Foundation: `Locale.current`)
 *
 * ## Note: The server may send more than one error for a request. In case there
 * is more than one error, the property addtionalErrorDetails holds the further errors.
 *
 * @property code: error code as defined by the Server API
 * @property merchantMessage: user readable message (for internal use)
 * @property customerMessage: user readable message for customer
 * @property additionalErrorDetails: additional errors send by the server for a particular request
 *
 */
class ServerErrorDetails {

    /// error code as defined by the Server API
    val code: String

    /// user readable message (for internal use)
    val merchantMessage: String

    /// user readable message for customer
    val customerMessage: String


    /// additional errors send by the server for a particular request
    val additionalErrorDetails: List<ServerErrorDetails>?

    internal constructor(backendServerError: BackendServerError, additionalErrorDetails: List<ServerErrorDetails>? = null) {
        this.code = backendServerError.code
        this.merchantMessage = backendServerError.merchantMessage
        this.customerMessage = backendServerError.customerMessage

        this.additionalErrorDetails = additionalErrorDetails
    }

    companion object {

        /**
         * maps the BackendServerError to the ServerErrorDetails structure
         *
         * @param errors: List of backend errors
         * @return a ServerErrorDetails element with the data mapped from the first backend server error.
         * Additional backend server errors are mapped to the additionalErrorDetails property of
         * that element. This method returns nil in case there is no backend error element in the
         * provided array.
         */
        internal fun fromBackendErrors(errors: List<BackendServerError>): ServerErrorDetails? {
            errors.firstOrNull()?.let { firstBackendServerError ->

                var additionalErrorDetails: List<ServerErrorDetails>? = null
                val additionalErrors = errors.drop(1)
                if (additionalErrors.isNotEmpty()) {
                    additionalErrorDetails = additionalErrors.map { ServerErrorDetails(it, null) }
                }

                return ServerErrorDetails(firstBackendServerError, additionalErrorDetails)
            }
            return null
        }
    }
}
