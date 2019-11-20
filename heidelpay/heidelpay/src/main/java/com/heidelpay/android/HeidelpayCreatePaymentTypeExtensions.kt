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

package com.heidelpay.android

import com.heidelpay.android.types.paymenttypes.*

/**
 * Create a card payment type
 *
 * @param number: credit card number
 * @param cvc: cvc number
 * @param expiryDate: expiry date of card in the format MM/YY
 * @param completion: Completion lambda expression
 * @param use3ds: flag to indicate if 3ds shall be used for this card on charge. (Depends on your contract).  Default is true
 */
fun Heidelpay.createPaymentCard(number: String, cvc: String, expiryDate: String, use3ds: Boolean = true, completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = CardPayment(number = number, cvc = cvc, expiryDate = expiryDate, use3ds = use3ds), completion = completion)
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

/**
 * create an Alipay payment type
 * @param completion: Completion lambda expression
 */
fun Heidelpay.createPaymentAlipay(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = AlipayPayment(), completion = completion)
}

/**
 * Create a WeChat payment type
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentWeChatPay(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = WeChatPayPayment(), completion = completion)
}

/**
 * Create a PIS payment type
 * @param completion: Completion lambda expression
 *
 */

fun Heidelpay.createPaymentPIS(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = PISPayment(), completion = completion)
}

/**
 * Create an Invoice-Factoring payment type
 * @param completion: Completion lambda expression
 *
 */
fun Heidelpay.createPaymentInvoiceFactoring(completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = InvoiceFactoringPayment(), completion = completion)
}

/**
 * Create a Hire Purchase payment type
 * @param iban: International bank account number (IBAN)
 * @param bic: Bank identification number (only needed for some countries)
 * @param holder: Name of the bank account
 * @param plan: Hire purchase plan
 * @param completion: completion handler of type CreatePaymentCompletionBlock.
 */
fun Heidelpay.createPaymentHirePurchase(iban: String, bic: String, holder: String, plan: HirePurchasePlan, completion: HeidelpayCreatePaymentCompletion) {
    createPayment(type = HirePurchasePayment(iban = iban, bic = bic, holder = holder, plan = plan), completion = completion)
}
