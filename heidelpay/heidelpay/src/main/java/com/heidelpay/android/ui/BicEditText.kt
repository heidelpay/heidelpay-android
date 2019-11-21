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
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.heidelpay.android.R
import com.heidelpay.android.ui.model.BICInput

/**
 * Custom {@link android.widget.EditText} that automatically takes care of BIC input.
 * The entered BIC information can be found in the property userInput.
 */
class BicEditText : FormattingEditText<BICInput> {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        keyListener = DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyz1234567890")
        setKeyboardType()
        hint = "STZZATWWXXX"
    }

    /**
     * Sets EditText's inputType depending on the given BIC
     * @param bic an BIC
     */
    private fun setKeyboardType() {
        inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_CLASS_TEXT
    }

    /**
     * Creates an instance of type BICInput
     * @param   string  the entered string
     * @return          the created BICInput instance
     */
    override fun createInput(string: String): BICInput {
        var bic = string

        val img = ContextCompat.getDrawable(context, R.drawable.card_debit)
        if (img != null) {
            img.setBounds(0, 0, 68, 43)
            compoundDrawablePadding = 20
            setCompoundDrawables(img, null, null, null)
        }
        return BICInput(bic)
    }
}
