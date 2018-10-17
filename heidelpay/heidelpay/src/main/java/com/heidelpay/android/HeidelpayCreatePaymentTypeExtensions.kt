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

import com.heidelpay.android.types.paymenttypes.*

/**
 * Create a card payment type
 *
 * @param number: credit card number
 * @param cvc: cvc number
 * @param expiryDate: expiry date of card in the format MM/YY
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentCard(number: String, cvc: String, expiryDate: String, completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = CardPayment(number = number, cvc = cvc, expiryDate = expiryDate), completion = completion)
}

/**
 * Create a Sepa direct debit payment type
 *
 * @param iban: International bank account number (IBAN)
 * @param bic: Bank identification number (only needed for some countries) (optional)
 * @param holder: Name of the bank account (optional)
 * @param guaranteed: toggles if this is a guaranteed or non guaranteed sepa direct payment. Default is non guaranteed.
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentSepaDirect(iban: String, bic: String?, holder: String?, guaranteed: Boolean = false, completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = SepaDirectDebitPayment(iban, bic, holder, guaranteed), completion = completion)
}

/**
 * Create an invoice payment type
 *
 * @param guaranteed: toggles if this is a guaranteed or non guaranteed invoice payment. Default is non guaranteed
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentInvoice(guaranteed: Boolean = false, completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = InvoicePayment(guaranteed), completion = completion)
}

/**
 * Create a Sofort Ueberweisung payment type
 *
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentSofort(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = SofortPayment(), completion = completion)
}

/**
 * Create a Giropay payment type
 *
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentGiropay(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = GiropayPayment(), completion = completion)
}

/**
 * Create a Prepayment payment type
 *
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentPrepayment(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = PrepaymentPayment(), completion = completion)
}

/**
 * Create a Przelewy24 payment type
 *
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentPrzelewy24(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = Przelewy24Payment(), completion = completion)
}

/**
 * Create a Paypal payment type
 *
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentPayPal(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = PaypalPayment(), completion = completion)
}

/**
 * Create a iDEAL payment type
 *
 * @param bic: bic to use for this Ideal payment
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentIdeal(bic: String, completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = IdealPayment(bic), completion = completion)
}
