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

package com.heidelpay.android.ui.model

import com.heidelpay.android.ui.heidelpay_condensedString

/**
 * An enum representing all supported credit card types.
 */
enum class CreditCardType {
    Unknown, Visa, AmericanExpress, MasterCard, Maestro;

    /**
     * The minimum length of this credit card type
     */
    val minimumLength: Int
        get() {
            when (this) {
                AmericanExpress -> return 15
                Visa -> return 16
                Maestro -> return 12
                else -> return 16
            }
        }

    /**
     * The maximum length of this credit card type
     */
    val maximumLength: Int
        get() {
            when (this) {
                AmericanExpress -> return 15
                Visa -> return 16
                Maestro -> return 19
                else -> return 16
            }
        }

    /**
     * The grouping style for this credit card type
     */
    val groupingMode: GroupingStyle
        get() {
            when (this) {
                AmericanExpress -> return VariableGroups(intArrayOf(4, 6, 5), maximumLength)
                else -> return FixedGroups(4, maximumLength)
            }
        }

    /**
     * Validates the given card number
     * @param   cardNumber  the given credit card number
     * @return              the validation result
     */
    fun validate(cardNumber: String): PaymentTypeInformationValidationResult {
        val condensedNumber = cardNumber.heidelpay_condensedString()
        //simple length checks
        if (condensedNumber.length < minimumLength || condensedNumber.length > maximumLength) {
            return PaymentTypeInformationValidationResult.InvalidLength
        }

        return PaymentTypeInformationValidator.validateCreditCardNumber(cardNumber)
    }

    companion object {
        internal fun fromString(string: String): CreditCardType {
            if (string.isEmpty()) {
                return Unknown
            }
            var firstCharacter = string.first()
            when (firstCharacter) {
                '2', '5' -> return MasterCard
                '3' -> return AmericanExpress
                '4' -> return Visa
                '6', '7', '9' -> return Maestro
            }
            return Unknown
        }
    }
}
