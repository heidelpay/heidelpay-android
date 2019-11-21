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

package com.heidelpay.android.ui.model

import com.heidelpay.android.ui.heidelpay_limitedString
import com.heidelpay.android.ui.heidelpay_trim
import java.util.*

/**
 * The interface for all input types
 */
interface Input {
    /**
     * The formatted representation of the entered value. e.g. in case of an IBAN the formattedValue would hold the correctly grouped IBAN (AT12 1233 1234 1234).
     */
    val formattedValue: String

    /**
     * The entered value but without leading and trailing separator characters
     */
    val trimmedValue: String

    val valid: Boolean

    val groupingSeparator: String?
}

/**
 * Holds information about the entered credit card number
 */
class CreditCardInput : Input {

    /**
     * The entered credit card number
     */
    val creditCardNumber: String

    /**
     * The validation result of entered credit card number
     */
    val validationResult: PaymentTypeInformationValidationResult

    /**
     * The type of the credit card
     */
    val creditCardType: CreditCardType

    override val valid: Boolean
        get() = validationResult == PaymentTypeInformationValidationResult.ValidChecksum

    override val groupingSeparator = " "
    private val maxLength: Int
    private val groupingStyle: GroupingStyle

    constructor(creditCardNumber: String) {
        val condensedCreditCardNumber = GroupingStyle.ungroupString(creditCardNumber, groupingSeparator)

        this.creditCardType = CreditCardType.fromString(condensedCreditCardNumber)
        this.groupingStyle = creditCardType.groupingMode
        this.maxLength = creditCardType.maximumLength

        this.creditCardNumber = condensedCreditCardNumber.heidelpay_limitedString(maxLength)
        this.validationResult = creditCardType.validate(this.creditCardNumber)
    }

    override val formattedValue: String
        get() {
            return groupingStyle.groupString(creditCardNumber, groupingSeparator)
        }

    override val trimmedValue: String
        get() {
            return formattedValue.heidelpay_trim(groupingSeparator)
        }
}

/**
 * Holds information about the entered IBAN
 */
class IBANInput : Input {
    /**
     * The entered IBAN
     */
    val iban: String

    /**
     * The validation result of the entered IBAN
     */
    val validationResult: PaymentTypeInformationValidationResult

    override val valid: Boolean
        get() = validationResult == PaymentTypeInformationValidationResult.ValidChecksum

    override val groupingSeparator = " "

    private val maxLength = 34
    private val groupingStyle = FixedGroups(4, maxLength)

    /// The allowed alpha characters in an IBAN
    private val heidelpayAllowedIbanAlphaCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "

    /// The allowed number characters in an IBAN
    private val heidelpayAllowedIbanNumberCharacters = "01234567890 "

    constructor(iban: String) {
        val condensedIban = GroupingStyle.ungroupString(iban, groupingSeparator)
        this.iban = removeNotAllowedChars(condensedIban.heidelpay_limitedString(maxLength))
        this.validationResult = PaymentTypeInformationValidator.validateIBAN(iban)
    }

    /**
     * An IBAN has to start with 2 alpha characters and after that only numerical characters are allowed. This method returns a string assuring that.
     */
    private fun removeNotAllowedChars(string: String): String {
        var iban = ""
        for (index in string.indices) {
            val character = string[index]
            if (index < 2 && heidelpayAllowedIbanAlphaCharacters.contains(character.toUpperCase()) == false) {
                continue
            } else if (index >= 2 && heidelpayAllowedIbanNumberCharacters.contains(character.toUpperCase()) == false) {
                continue
            }
            iban = iban + character
        }
        return iban
    }

    override val formattedValue: String
        get() {
            return groupingStyle.groupString(iban, groupingSeparator)
        }

    override val trimmedValue: String
        get() {
            return formattedValue.heidelpay_trim(groupingSeparator)
        }
}

/**
 * Holds information about the entered CVV
 */
class CvvInput : Input {

    /**
     * The entered CVV
     */
    val cvv: String

    /**
     * A flag representing if the cvv is valid (only performs length check)
     */
    override val valid: Boolean

    private val maxLength = 3

    override val groupingSeparator: String? = null

    constructor(cvv: String) {
        this.cvv = cvv.heidelpay_limitedString(maxLength)
        this.valid = this.cvv.length == maxLength
    }

    override val formattedValue: String
        get() {
            return cvv
        }

    override val trimmedValue: String
        get() {
            return formattedValue
        }
}

/**
 * Holds information about the entered credit card expiry
 */
class CardExpiryInput : Input {

    /**
     * The normalized expiryDate (MM/YY) if the input is valid or the current user input otherwise
     */
    val expiryDate: String

    /**
     * A flag representing if the expiryDate is valid.
     * Includes format checks as well as if the expiryDate lies in the future
     */
    override val valid: Boolean

    /**
     * The month of the expiry date as an Int between 1-12 or -1 if the expiryDate is not valid
     */
    val month: Int
        get() {
            if (expiryDate.length == 4) {
                val monthValue = expiryDate.substring(0, 2).toInt()
                if (monthValue > 0 && monthValue < 13) {
                    return monthValue
                }
            }
            return -1
        }

    /**
     * the year of the expiry date as an Int with the full year count (e.g. 2025 if the expiry date is XX/25)
     * or -1 if the expiryDate is not valid
     */
    val year: Int
        get() {
            if (expiryDate.length == 4) {
                return (expiryDate.substring(expiryDate.length - 2, expiryDate.length)).toInt() + 2000
            }
            return -1
        }

    override val groupingSeparator = "/"
    private val maxLength = 4
    private val groupingStyle = FixedGroups(2, maxLength)

    constructor(expiryDateAsString: String) {
        val condensedExpDate = GroupingStyle.ungroupString(expiryDateAsString, groupingSeparator).heidelpay_limitedString(maxLength)

        if (condensedExpDate.length == 1 && (condensedExpDate[0] != '1' && condensedExpDate[0] != '0')) {
            this.expiryDate = "0${condensedExpDate}${groupingSeparator}"
        } else {
            this.expiryDate = condensedExpDate
        }

        if (condensedExpDate.length == maxLength) {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            valid = (month <= 12 && month > 0) && (year > currentYear || (year == currentYear && month > currentMonth))
        } else {
            valid = false
        }
    }

    override val formattedValue: String
        get() {
            return groupingStyle.groupString(expiryDate, groupingSeparator)
        }

    override val trimmedValue: String
        get() {
            return formattedValue.heidelpay_trim(groupingSeparator)
        }
}

/**
 * Holds information about the user input for a BIC
 */
class BICInput : Input {
    /**
     * The entered bic
     */
    val bic: String

    /**
     * A flag representing if the bic is valid (only performs length check)
     */
    override val valid: Boolean

    override val groupingSeparator: String? = null

    constructor(bic: String) {
        this.bic = bic
        // bic length: http://databaseadvisors.com/pipermail/accessd/2003-November/034201.html
        this.valid = this.bic.length >= 8
    }

    override val formattedValue: String
        get() {
            return bic
        }

    override val trimmedValue: String
        get() {
            return formattedValue
        }
}
