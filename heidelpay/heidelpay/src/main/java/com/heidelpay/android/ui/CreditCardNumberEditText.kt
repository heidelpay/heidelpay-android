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

package com.heidelpay.android.ui

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import com.heidelpay.android.R
import com.heidelpay.android.ui.model.CreditCardInput
import com.heidelpay.android.ui.model.CreditCardType

/**`
 * Custom {@link android.widget.EditText} that automatically takes care of credit card number inputs and handles correct formatting as well as validation.
 * The entered credit card number can be found in the property userInput.
 */
class CreditCardNumberEditText : FormattingEditText<CreditCardInput> {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        setKeyListener(DigitsKeyListener.getInstance("1234567890 "))
        hint = "Card Number"
    }

    /**
     * Creates an instance of type CreditCardInput. Also set's the left-aligned credit card icon based on the credit card type
     * @param   string  the entered string
     * @return          the created CreditCardInput instance
     */
    override fun createInput(string: String): CreditCardInput {
        val input = CreditCardInput(string)

        if (input.creditCardType.image != null) {
            val img = ContextCompat.getDrawable(context, input.creditCardType.image!!)
            if (img != null) {
                if (drawableTintColor != 0) {
                    img.setColorFilter(drawableTintColor, PorterDuff.Mode.SRC_IN)
                }
                img.setBounds(0, 0, 68, 43)
                compoundDrawablePadding = 20
                setCompoundDrawables(img, null, null, null)
            }
        } else {
            setCompoundDrawables(null, null, null, null)
        }
        return input
    }
}

/**
 * An extension of {@link CreditCardType} returning the associated credit card icon for a given card type
 */
private val CreditCardType.image: Int?
    get() {
        when (this) {
            CreditCardType.Unknown -> return R.drawable.payment_method_genericcard
            CreditCardType.Visa -> return R.drawable.payment_method_visa
            CreditCardType.AmericanExpress -> return R.drawable.payment_method_american_express
            CreditCardType.MasterCard -> return R.drawable.payment_method_mastercard
            CreditCardType.Maestro -> return R.drawable.payment_method_maestro
            else -> return R.drawable.payment_method_genericcard
        }
    }
