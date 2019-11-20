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

import com.heidelpay.android.ui.heidelpay_condensedString

/**
 * Base class of all available grouping styles
 */
sealed class GroupingStyle {
    companion object {
        /**
         * Removes all grouping separators
         * @param   string      The string which should be 'ungrouped'
         * @param   separator   The grouping separator
         * @return              A new string without groups
         */
        fun ungroupString(string: String, separator: String): String {
            return string.heidelpay_condensedString(separator)
        }
    }

    /**
     * Creates a string with groups using the given separator
     * @param   string      The string which should be 'grouped'
     * @param   separator   The grouping separator
     * @return              A new string with groups
     */
    abstract fun groupString(string: String, separator: String): String
}


/**
 * This grouping style uses a fixed groupSize (i.e. all groups are of the same length) to create new strings grouped by a separator.
 */
data class FixedGroups(val groupSize: Int, val maximumLength: Int) : GroupingStyle() {

    /**
     * Creates a string with groups using the given separator. Uses the configured groupSize to determine the length of each group.
     * @param   string      The string which should be 'grouped'
     * @param   separator   The grouping separator
     * @return              A new string with groups
     */
    override fun groupString(string: String, separator: String): String {
        var result = ""
        //remove all separators so we can rebuild all groups if necessary
        var condensedString = string.heidelpay_condensedString(separator)
        for (character in condensedString) {
            if (result.heidelpay_condensedString(separator).length >= maximumLength) {
                break
            }
            result = "${result}${character}"
            val groups = result.split(separator)

            //check if we should add the group separator
            if (groups.last().length == groupSize && result.heidelpay_condensedString(separator).length < maximumLength) {
                result = "${result}${separator}"
            }
        }
        return result
    }
}

/**
 * This grouping style uses an array of groupSizes (i.e. groups can have different lengths) to create new strings grouped by a separator.
 */
data class VariableGroups(val groupSizes: IntArray, val maximumLength: Int) : GroupingStyle() {

    /**
     * Creates a string with groups using the given separator. Uses the configured groupSizes to determine the length of each group.
     * @param   string      The string which should be 'grouped'
     * @param   separator   The grouping separator
     * @return              A new string with groups
     */
    override fun groupString(string: String, separator: String): String {
        var result = ""
        //remove all separators so we can rebuild all groups if necessary
        var condensedString = string.heidelpay_condensedString(separator)
        for (character in condensedString) {
            if (result.heidelpay_condensedString(separator).length >= maximumLength) {
                break
            }
            result = "${result}${character}"
            val groups = result.split(separator)

            val groupSize = groupSizes[groups.size - 1]
            //check if we should add the group separator
            if (groups.last().length == groupSize && result.heidelpay_condensedString(separator).length < maximumLength) {
                result = "${result}${separator}"
            }
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other is VariableGroups) {
            return groupSizes.contentEquals(other.groupSizes) && maximumLength == other.maximumLength
        } else {
            return false
        }
    }
}
