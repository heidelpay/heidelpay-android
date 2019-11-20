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

package com.heidelpay.android.ui

import com.heidelpay.android.R
import com.heidelpay.android.types.PaymentMethod

/**
 * Returns an appropriate image to be used for this PaymentMethod or nil if an appropriate image is not available
 */
val PaymentMethod.icon: Int?
    get() {
        when (this) {
            PaymentMethod.Card -> return R.drawable.payment_method_genericcard
            PaymentMethod.Giropay -> return R.drawable.payment_method_giropay
            PaymentMethod.Ideal -> return R.drawable.payment_method_ideal
            PaymentMethod.Invoice -> return R.drawable.payment_method_invoice
            PaymentMethod.InvoiceGuaranteed -> return R.drawable.payment_method_invoice
            PaymentMethod.InvoiceFactoring -> return R.drawable.payment_method_invoice
            PaymentMethod.SepaDirectDebit, PaymentMethod.SepaDirectDebitGuaranteed -> return R.drawable.payment_method_sepa
            PaymentMethod.Paypal -> return R.drawable.payment_method_paypal
            PaymentMethod.Przelewy24 -> return R.drawable.payment_method_przelewy
            PaymentMethod.Sofort -> return R.drawable.payment_method_sofort
            PaymentMethod.Prepayment -> return R.drawable.payment_method_prepayment
            PaymentMethod.Alipay -> return R.drawable.payment_method_alipay
            PaymentMethod.Wechatpay -> return R.drawable.payment_method_wechat
            PaymentMethod.PIS -> return R.drawable.payment_method_pis
            PaymentMethod.HirePurchase -> return R.drawable.payment_method_hire_purchase
        }
    }

/**
 * The displayName of the PaymentMethod
 */
val PaymentMethod.displayName: String
    get() {
        when (this) {
            PaymentMethod.Card -> return "Card"
            PaymentMethod.SepaDirectDebit -> return "Sepa Direct Debit"
            PaymentMethod.SepaDirectDebitGuaranteed -> return "Sepa Direct Debit Guaranteed"
            PaymentMethod.Paypal -> return "PayPal"
            PaymentMethod.Sofort -> return "Sofortüberweisung"
            PaymentMethod.Giropay -> return "Giropay"
            PaymentMethod.InvoiceGuaranteed -> return "Invoice Guaranteed"
            PaymentMethod.InvoiceFactoring -> return "Invoice Factoring"
            PaymentMethod.Invoice -> return "Invoice"
            PaymentMethod.Prepayment -> return "Prepayment"
            PaymentMethod.Przelewy24 -> return "Przelewy24"
            PaymentMethod.Ideal -> return "iDEAL"
            PaymentMethod.Alipay -> return "Alipay"
            PaymentMethod.Wechatpay -> return "WeChat"
            PaymentMethod.PIS -> return "PIS"
            PaymentMethod.HirePurchase -> return "Hire Purchase"

        }
    }

/**
 *  May the customer create multiple Payment Type instances of that payment method?
 */
val PaymentMethod.multipleInstancesAllowed: Boolean
    get() {
        when (this) {
            PaymentMethod.Card, PaymentMethod.SepaDirectDebit, PaymentMethod.SepaDirectDebitGuaranteed, PaymentMethod.Ideal -> return true
            else -> return false
        }
    }


fun String.mask(): String {
    if (this.length <= 4) {
        return "*"
    } else {
        return "*" + this.substring(this.length - 4)
    }
}
