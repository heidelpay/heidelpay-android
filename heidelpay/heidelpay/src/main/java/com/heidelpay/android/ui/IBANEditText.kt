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
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import com.heidelpay.android.ui.model.IBANInput

/**
 * Custom {@link android.widget.EditText} that automatically takes care of IBAN input and handles correct formatting as well as validation.
 * The entered IBAN information can be found in the property userInput.
 */
class IBANEditText : FormattingEditText<IBANInput> {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz1234567890 "))
        setKeyboardType("")
        hint = "IBAN"
    }

    /**
     * Sets EditText's inputType depending on the given IBAN
     * @param iban an IBAN
     */
    private fun setKeyboardType(iban: String) {
        if (iban.trim().length >= 2) {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        } else {
            inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_CLASS_TEXT
        }
    }

    /**
     * Creates an instance of type IBANInput
     * @param   string  the entered string
     * @return          the created IBANInput instance
     */
    override fun createInput(string: String): IBANInput {
        var iban = string

        setKeyboardType(iban)

        return IBANInput(iban)
    }
}
