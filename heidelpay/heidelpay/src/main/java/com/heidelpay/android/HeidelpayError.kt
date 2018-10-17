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

package com.heidelpay.android

import com.heidelpay.android.types.ServerErrorDetails

/**
 * Enumeration of errors that may occur during setup or creation of payment types
 */
sealed class HeidelpayError {
    /// the server could not be reached because there seems to be no internet connection
    class NoInternetConnection : HeidelpayError()

    /// the request failed because of a technical issue. please contact support
    class GeneralProcessingError : HeidelpayError()

    /// the provided key is not authorized to use the Heidelpay service
    class NotAuthorized : HeidelpayError()

    /// the server reported an error
    class ServerError(val details: ServerErrorDetails) : HeidelpayError()

    //needed for adding an 'static' extension (see mapFromBackendError)
    companion object {
    }
}
