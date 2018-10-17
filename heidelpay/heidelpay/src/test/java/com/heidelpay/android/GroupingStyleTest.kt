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

import com.heidelpay.android.ui.model.FixedGroups
import com.heidelpay.android.ui.model.VariableGroups
import org.junit.Assert
import org.junit.Test

class GroupingStyleTest {

    @Test
    fun testFixedGroupingStyle() {
        Assert.assertEquals("12_34_56_78_9", FixedGroups(groupSize = 2, maximumLength = 20).groupString("123456789", "_"))

        Assert.assertEquals("12_34_56", FixedGroups(groupSize = 2, maximumLength = 6).groupString("123456789", "_"))
    }

    @Test
    fun testVarGroupingStyle() {
        Assert.assertEquals("12_3_45_6", VariableGroups(groupSizes = intArrayOf(2,1,2,1), maximumLength = 6).groupString("123456789", "_"))

        Assert.assertEquals("12_3456_78", VariableGroups(groupSizes = intArrayOf(2,4,2), maximumLength = 8).groupString("1234567890", "_"))
    }
}
