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

import com.heidelpay.android.ui.model.CreditCardType
import com.heidelpay.android.ui.model.FixedGroups
import com.heidelpay.android.ui.model.PaymentTypeInformationValidationResult
import com.heidelpay.android.ui.model.VariableGroups
import org.junit.Assert
import org.junit.Test

class CreditCardTypeTest {
    @Test
    fun testCreditCardTypesForNumber() {
        Assert.assertEquals(CreditCardType.Unknown, CreditCardType.fromString("1234"))
        Assert.assertEquals(CreditCardType.MasterCard, CreditCardType.fromString("2345"))
        Assert.assertEquals(CreditCardType.AmericanExpress, CreditCardType.fromString("3456"))
        Assert.assertEquals(CreditCardType.Visa, CreditCardType.fromString("4567"))
        Assert.assertEquals(CreditCardType.MasterCard, CreditCardType.fromString("5678"))
        Assert.assertEquals(CreditCardType.Maestro, CreditCardType.fromString("6789"))
        Assert.assertEquals(CreditCardType.Maestro, CreditCardType.fromString("7890"))
        Assert.assertEquals(CreditCardType.Unknown, CreditCardType.fromString("8901"))
        Assert.assertEquals(CreditCardType.Maestro, CreditCardType.fromString("9012"))
        Assert.assertEquals(CreditCardType.Unknown, CreditCardType.fromString("0123"))

        Assert.assertEquals(CreditCardType.Unknown, CreditCardType.fromString("a123"))
    }

    @Test
    fun testGrouping() {
        val unknown = CreditCardType.Unknown
        Assert.assertEquals(16, unknown.minimumLength)
        Assert.assertEquals(16, unknown.maximumLength)
        Assert.assertEquals(FixedGroups(4, 16), unknown.groupingMode)

        val visa = CreditCardType.Visa
        Assert.assertEquals(16, visa.minimumLength)
        Assert.assertEquals(16, visa.maximumLength)
        Assert.assertEquals(FixedGroups(groupSize = 4, maximumLength = 16), visa.groupingMode)

        val amex = CreditCardType.AmericanExpress
        Assert.assertEquals(15, amex.minimumLength)
        Assert.assertEquals(15, amex.maximumLength)
        Assert.assertEquals(VariableGroups(groupSizes = intArrayOf(4, 6, 5), maximumLength = 15), amex.groupingMode)

        val masterCard = CreditCardType.MasterCard
        Assert.assertEquals(16, masterCard.minimumLength)
        Assert.assertEquals(16, masterCard.maximumLength)
        Assert.assertEquals(FixedGroups(groupSize = 4, maximumLength = 16), masterCard.groupingMode)

        val maestro = CreditCardType.Maestro
        Assert.assertEquals(12, maestro.minimumLength)
        Assert.assertEquals(19, maestro.maximumLength)
        Assert.assertEquals(FixedGroups(groupSize = 4, maximumLength = 19), maestro.groupingMode)
    }

    @Test
    fun testAmexValidation() {
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength,
                CreditCardType.AmericanExpress.validate("37035549687"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum,
                CreditCardType.AmericanExpress.validate("370355496876137"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum,
                CreditCardType.AmericanExpress.validate("37 03554968 76  \n137 "))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidChecksum,
                CreditCardType.AmericanExpress.validate("380355496876137"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength,
                CreditCardType.AmericanExpress.validate("3703554968761377"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidChecksum,
                CreditCardType.AmericanExpress.validate("370355496876138"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters,
                CreditCardType.AmericanExpress.validate("37035549687613a"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters,
                CreditCardType.AmericanExpress.validate("3703554968 7613  a"))
    }

    @Test
    fun testMasterCardValidation() {
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength,
                CreditCardType.MasterCard.validate("538950124765"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum,
                CreditCardType.MasterCard.validate("5389501247653501"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum,
                CreditCardType.MasterCard.validate("5389  501247653  50 \n\n1"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidChecksum,
                CreditCardType.MasterCard.validate("5489501247653501"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength,
                CreditCardType.MasterCard.validate("5389501247653501123"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidChecksum,
                CreditCardType.MasterCard.validate("5389501247653502"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters,
                CreditCardType.MasterCard.validate("538950124765350a"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters,
                CreditCardType.MasterCard.validate("53895012476  5 b5  e1"))
    }

    @Test
    fun testVisaValidation() {
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength,
                CreditCardType.Visa.validate("4539260780952"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum,
                CreditCardType.Visa.validate("4539260780952497"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum,
                CreditCardType.Visa.validate("45  3926 07 809  52497"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidChecksum,
                CreditCardType.Visa.validate("4639260780952497"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength,
                CreditCardType.Visa.validate("4539260780952497123"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidChecksum,
                CreditCardType.Visa.validate("4539260780952498"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters,
                CreditCardType.Visa.validate("453926078095249a"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters,
                CreditCardType.Visa.validate("453 a260780952   \nb97"))
    }
}
