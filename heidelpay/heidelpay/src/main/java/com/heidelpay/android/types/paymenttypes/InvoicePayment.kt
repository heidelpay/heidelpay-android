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

package com.heidelpay.android.types.paymenttypes

import com.heidelpay.android.types.PaymentMethod

/**
 * Invoice Payment Type
 * There is a guaranteed and a non guaranteed version of the Invoice Payment type
 * which can be set as an additional constructor parameter. Default is non guaranteed.
 *
 * @property guaranteed: guaranteed flag
 */
internal data class InvoicePayment(val guaranteed: Boolean = false) : CreatePaymentType {

    /// dependent on the flag guaranteed the method is invoice guaranteed or not guaranteed
    override val method: PaymentMethod
        get() {
            if (guaranteed) {
                return PaymentMethod.InvoiceGuaranteed
            }
            return PaymentMethod.Invoice
        }
}
