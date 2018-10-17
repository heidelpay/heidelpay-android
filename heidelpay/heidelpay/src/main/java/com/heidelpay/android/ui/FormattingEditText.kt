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
import android.os.Build
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.heidelpay.android.R
import com.heidelpay.android.ui.model.Input


/**
 * Base class for all custom edit texts. Handles all text change events.
 */
abstract class FormattingEditText<T : Input> : AppCompatEditText {
    protected var errorTextColor: Int = 0
    protected var originalTextColor: Int = 0
    protected var drawableTintColor: Int = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        readAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        readAttributes(attrs)
    }

    var userInput: T? = null

    init {
        originalTextColor = this.currentTextColor
        addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this@FormattingEditText.removeTextChangedListener(this)
                if (s != null) {
                    var string = s.toString()
                    userInput = createInput(string)

                    if (userInput != null) {
                        if (isDeleting(count)) {
                            this@FormattingEditText.setText(userInput!!.trimmedValue)
                        } else {
                            this@FormattingEditText.setText(userInput!!.formattedValue)
                        }
                        if (userInput!!.valid) {
                            this@FormattingEditText.setTextColor(this@FormattingEditText.originalTextColor)
                        } else if (this@FormattingEditText.errorTextColor != 0) {
                            this@FormattingEditText.setTextColor(this@FormattingEditText.errorTextColor)
                        }
                    }
                    if (this@FormattingEditText.text != null) {
                        this@FormattingEditText.setSelection(this@FormattingEditText.text!!.length)
                    }
                } else {
                    userInput = null
                }
                this@FormattingEditText.addTextChangedListener(this)
            }

            private fun isDeleting(count: Int): Boolean {
                return count == 0
            }
        })
    }

    private fun readAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormattingEditText, 0, 0)
        try {
            errorTextColor = typedArray.getColor(R.styleable.FormattingEditText_errorTextColor, getColorFromId(R.color.heidelPayErrorColor))
            drawableTintColor = typedArray.getColor(R.styleable.FormattingEditText_drawableTintColor, 0)
        } catch (exception: Exception) {
        } finally {
            typedArray.recycle()
        }
    }

    private fun getColorFromId(colorId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(colorId, context.theme)
        } else {
            return resources.getColor(colorId)
        }
    }

    protected abstract fun createInput(string: String): T
}
