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
 * Sofort Ueberweisung Payment Type
 */
internal data class SofortPayment(val dataClassPlaceholder: String = "") : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.Sofort
}

/**
 * Giropay Payment Type
 */
internal data class GiropayPayment(val dataClassPlaceholder: String = "") : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.Giropay
}

/**
 * Prepayment Payment Type
 */
internal data class PrepaymentPayment(val dataClassPlaceholder: String = "") : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.Prepayment
}

/**
 * Przelewy24 Payment Type
 */
internal data class Przelewy24Payment(val dataClassPlaceholder: String = "") : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.Przelewy24
}

/**
 * Paypal Payment Type
 */
internal data class PaypalPayment(val dataClassPlaceholder: String = "") : CreatePaymentType {
    override val method: PaymentMethod = PaymentMethod.Paypal
}
