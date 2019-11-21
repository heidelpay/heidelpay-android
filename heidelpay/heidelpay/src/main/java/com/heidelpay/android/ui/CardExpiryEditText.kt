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

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.heidelpay.android.R
import com.heidelpay.android.ui.model.CardExpiryInput

/**
 * Custom {@link android.widget.EditText} that automatically takes care of card expiry input and handles correct formatting as well as validation.
 * The entered card expiry information can be found in the property userInput.
 */
class CardExpiryEditText : FormattingEditText<CardExpiryInput> {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        keyListener = DigitsKeyListener.getInstance("1234567890")
        hint = "MM/YY"
    }

    /**
     * Creates an instance of type CardExpiryInput
     * @param   string  the entered string
     * @return          the created CardExpiryInput instance
     */
    override fun createInput(string: String): CardExpiryInput {
        val input = CardExpiryInput(string)
        var img: Drawable?
        var rightBounds = 0
        var bottomBounds = 0
        if (input.valid) {
            img = ContextCompat.getDrawable(context, R.drawable.success)
            rightBounds = 65
            bottomBounds = 65
        } else {
            img = ContextCompat.getDrawable(context, R.drawable.expiry)
            bottomBounds = 43
            rightBounds = 68
        }
        if (img != null) {
            img.setBounds(0, 0, rightBounds, bottomBounds)
            compoundDrawablePadding = 20
            setCompoundDrawables(img, null, null, null)
        }
        return input
    }
}
