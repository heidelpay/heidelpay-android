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

/**
 * Trims separator from a given string
 * @param   separator   the separator character as String
 * @return              a new string trimmed by the separator
 */
fun String.heidelpay_trim(separator: String): String {
    return trim { char ->
        separator.contains(char)
    }
}


/**
 * Removes all separators from a given string
 * @param   groupingSeparator   the separator character as String
 * @return                      a new string not containing the separator
 */
fun String.heidelpay_condensedString(groupingSeparator: String? = null): String {
    var string = replace("\\s".toRegex(), "")
    if (groupingSeparator != null) {
        string = replace(groupingSeparator.toRegex(), "")
    }
    return string
}

/**
 * Creates a new string limited to maxLength
 * @param   maxLength   the maximum length of the string
 * @return              a new string with maximum length maxLength
 */
fun String.heidelpay_limitedString(maxLength: Int): String {
    return this.substring(0, kotlin.math.min(this.length, maxLength))
}

/**
 *  Returns true if the string only contains numbers and no other characters (including whitespaces)
 */
val String.heidelpayStrictlyNumerical: Boolean
    get() {
        try {
            java.lang.Double.parseDouble(this)
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }
