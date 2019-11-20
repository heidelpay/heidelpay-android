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

import com.heidelpay.android.ui.model.PaymentTypeInformationValidationResult
import com.heidelpay.android.ui.model.PaymentTypeInformationValidator
import org.junit.Assert
import org.junit.Test

class PaymentTypeInformationValidatorTest {

    @Test
    fun testValidIBANs() {
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("GB82 WEST 1234 5698 7654 32"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("GB82 WEST \n1234     56987654\n\n 32"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("     GB82 WEST 1234 5698 7654 3  2    "))

        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("BE71 0961 2345 6769"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("FR76 3000 6000 0112 3456 7890 189"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("DE91 1000 0000 0123 4567 89"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("GR96 0810 0010 0000 0123 4567 890"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("RO09 BCYP 0000 0012 3456 7890"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("SA44 2000 0001 2345 6789 1234"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("ES79 2100 0813 6101 2345 6789"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("CH56 0483 5012 3456 7800 9"))
        Assert.assertEquals(PaymentTypeInformationValidationResult.ValidChecksum, PaymentTypeInformationValidator.validateIBAN("GB98 MIDL 0700 9312 3456 78"))
    }

    @Test
    fun testInvalidIBANCharaters() {
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidCharacters, PaymentTypeInformationValidator.validateIBAN("ÖGB82 WEST 1234 5698 7654 32"))
    }

    @Test
    fun testInvalidIBANLength() {
        Assert.assertEquals(PaymentTypeInformationValidationResult.InvalidLength, PaymentTypeInformationValidator.validateIBAN("GB1"))
    }
}
