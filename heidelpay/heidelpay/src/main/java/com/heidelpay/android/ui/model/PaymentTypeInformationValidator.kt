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

import com.heidelpay.android.ui.heidelpayStrictlyNumerical
import com.heidelpay.android.ui.heidelpay_condensedString

/**
 * implements various algorithms for calculating and verifying payment information / checksums
 */

object PaymentTypeInformationValidator {
    private val heidelpayIbanCharactersToConvert = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    /**
     * Validates an IBAN by calculating the checksum as described at https://www.sparkonto.org/manuelles-berechnen-der-iban-pruefziffer-sepa/
     * @param iban: IBAN to validate
     * @result result of the validation
     */
    fun validateIBAN(iban: String): PaymentTypeInformationValidationResult {
        if (iban.length < 9) {
            return PaymentTypeInformationValidationResult.InvalidLength
        }

        val normalizedIban = normalizeIBAN(iban)

        if (normalizedIban.heidelpayStrictlyNumerical == false) {
            return PaymentTypeInformationValidationResult.InvalidCharacters
        }

        var modulo: Int = 0
        var calculationString: String? = normalizedIban

        while (calculationString != null) {
            val result = nextModuloPart(calculationString, modulo)
            modulo = result.first
            calculationString = result.second

        }
        return if (modulo == 1) PaymentTypeInformationValidationResult.ValidChecksum else PaymentTypeInformationValidationResult.InvalidChecksum
    }

    /**
     * Creates the normalied representation of an IBAN
     * @param   iban    the iban
     * @return          the normalized iban
     */
    private fun normalizeIBAN(iban: String): String {
        val cleanedIBAN = iban.toUpperCase().heidelpay_condensedString()

        val reversedIBAN = reverseIBAN(cleanedIBAN)

        return convertAlphaCharactersToDigitsIBAN(reversedIBAN)
    }

    /**
     * Creates the reversed representation of an IBAN
     * @param   iban    the iban
     * @return          the reversed iban
     */
    private fun reverseIBAN(iban: String): String {
        val startCharacters = iban.substring(0, 4)
        val remainder = iban.substring(4)
        return "${remainder}${startCharacters}"
    }

    /**
     * Returns the next modulo part of the given IBAN needed for creating the check sum of the IBAN
     * @param   iban    the iban
     * @return          the next modulo part
     */
    private fun nextModuloPart(iban: String, modulo: Int?): Pair<Int, String?> {
        if (modulo != null) {
            if (iban.length < 7) {
                return Pair("${modulo}${iban}".toInt() % 97, null)
            } else {
                val calculationString = iban.subSequence(0, 7)
                val newModulo = "${modulo}${calculationString}".toInt() % 97
                val remainder = iban.subSequence(7, iban.length).toString()
                return Pair(newModulo, if (remainder.length > 0) remainder else null)
            }
        }
        val firstModulo = iban.substring(0, 9).toInt() % 97
        return Pair(firstModulo, iban.substring(9))
    }

    /**
     * Converts the alpha characters inside the IBAN to the corresponding digits
     * @param   iban    the iban
     * @return          the converted iban
     */
    private fun convertAlphaCharactersToDigitsIBAN(iban: String): String {
        val charsToConvert = PaymentTypeInformationValidator.heidelpayIbanCharactersToConvert

        var convertedString = iban
        for (index in charsToConvert.indices) {
            val character = charsToConvert[index].toString()
            // A = 10, B = 11, ...
            convertedString = convertedString.replace(character, "${index + 10}", true)
        }
        return convertedString
    }

    /**
     * Validates a Credit Card Number with the Luhn algorithm as described at https://en.wikipedia.org/wiki/Luhn_algorithm
     * @param   cardNumber  the credit card number ot validate
     * @return              result of the validation
     */
    fun validateCreditCardNumber(cardNumber: String): PaymentTypeInformationValidationResult {
        val condensedCardNumber = cardNumber.heidelpay_condensedString()
        var index = condensedCardNumber.length - 2
        var doublingDigit = true
        var sum = 0
        while (index >= 0) {
            val character = condensedCardNumber[index]
            try {
                val intValueForCharacter = character.toString().toInt()
                var addedValue = intValueForCharacter * (if (doublingDigit) 2 else 1)
                if (addedValue > 9) {
                    addedValue -= 9
                }
                sum += addedValue
            } catch (exception: Exception) {
                return PaymentTypeInformationValidationResult.InvalidCharacters
            }

            index -= 1
            doublingDigit = !doublingDigit
        }

        try {
            val checkDigitValue = condensedCardNumber[condensedCardNumber.length - 1].toString().toInt()
            if ((sum + checkDigitValue) % 10 == 0) {
                return PaymentTypeInformationValidationResult.ValidChecksum
            } else {
                return PaymentTypeInformationValidationResult.InvalidChecksum
            }
        } catch (exception: Exception) {
            return PaymentTypeInformationValidationResult.InvalidCharacters
        }
    }
}
