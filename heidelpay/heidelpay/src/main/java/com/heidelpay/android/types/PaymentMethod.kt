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

import android.os.Parcel
import android.os.Parcelable

/**
 * Enumation of payment methods supported by the version of the SDK
 */
enum class PaymentMethod(val rawValue: String) : Parcelable {
    /// Credit Card Payment
    Card("card"),

    /// Sofort Ueberweisung
    Sofort("sofort"),

    /// Sepa Direct Debit
    SepaDirectDebit("sepa-direct-debit"),

    /// Sepa Direct Debit Guaranteed
    SepaDirectDebitGuaranteed("sepa-direct-debit-guaranteed"),

    /// Invoice
    Invoice("invoice"),

    /// Invoice Guaranteed
    InvoiceGuaranteed("invoice-guaranteed"),

    /// Giropay
    Giropay("giropay"),

    /// Prepayment
    Prepayment("prepayment"),

    /// Przelewy24
    Przelewy24("przelewy24"),

    /// PayPal
    Paypal("paypal"),

    /// Ideal
    Ideal("ideal"),

    /// Alipay
    Alipay("alipay"),

    /// WeChat
    Wechatpay("wechatpay"),

    /// PIS
    PIS("PIS"),

    /// invoice factoring
    InvoiceFactoring("invoice-factoring"),

    /// Hire Purchase
    HirePurchase("hire-purchase-direct-debit");

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(rawValue)
    }

    /// backend path used to create a payment type for this method
    val createPaymentTypeBackendPath: String
        get() {
            return if (this == PIS) {
                "pis"
            } else {
                rawValue
            }
        }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<PaymentMethod> {
            override fun createFromParcel(parcel: Parcel): PaymentMethod {
                val rawValue = parcel.readString()
                rawValue?.let {
                    val mappedValue = fromString(it)
                    if (mappedValue != null) {
                        return mappedValue
                    }
                }

                return Card
            }

            override fun newArray(size: Int) = arrayOfNulls<PaymentMethod>(size)
        }

        internal fun fromString(string: String): PaymentMethod? {
            for (method in PaymentMethod.values()) {
                if (method.rawValue == string) {
                    return method
                }
            }
            return null
        }
    }
}
