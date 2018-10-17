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
import com.heidelpay.android.ui.model.CvvInput

/**
 * Custom {@link android.widget.EditText} that automatically takes care of CVV input and handles correct formatting as well as validation.
 * The entered CVV information can be found in the property userInput.
 */
class CvvEditText : FormattingEditText<CvvInput> {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        setKeyListener(DigitsKeyListener.getInstance("1234567890"))
        hint = "CVC"
    }

    /**
     * Creates an instance of type CvvInput.
     * @param   string  the entered string
     * @return          the created CvvInput instance
     */
    override fun createInput(string: String): CvvInput {
        return CvvInput(string)
    }
}
