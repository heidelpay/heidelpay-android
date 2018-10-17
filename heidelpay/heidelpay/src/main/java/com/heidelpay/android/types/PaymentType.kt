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

package com.heidelpay.android.types

import android.os.Parcel
import android.os.Parcelable
import com.heidelpay.android.types.PaymentMethod.*
import org.json.JSONArray
import org.json.JSONObject

/***
 * A customer Payment Type which can be used for a charge (on the server side)
 */
class PaymentType : Parcelable {

    /// payment id which will be used for the charge on the server side
    val paymentId: String

    /// payment method of that payment type
    val method: PaymentMethod

    /// a user printable value of that payment type
    val title: String

    /// data parameters specific to this payment type and payment method
    /// e.g. a credit card type has a data paremeter "brand" which holds
    /// the brand of the card. The names of these parameter match the name
    /// of the parameters of the server request to create a payment type
    val data: Map<String, Any>

    /// standard initializer for all properties
    internal constructor(paymentId: String, method: PaymentMethod, title: String, data: Map<String, Any>) {
        this.paymentId = paymentId
        this.method = method
        this.title = title
        this.data = data
    }

    /// implementation of equals to only compare paymentId
    override fun equals(other: Any?): Boolean {
        if (other is PaymentType) {
            return this.paymentId == other.paymentId
        }
        return false
    }

    override fun hashCode(): Int {
        return paymentId.hashCode()
    }

    private constructor(p: Parcel) {
        paymentId = p.readString()
        method = p.readParcelable<PaymentMethod>(PaymentMethod::class.java.classLoader)
        title = p.readString()

        val map = mutableMapOf<String, Any>()
        p.readMap(map, null)
        data = map
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(paymentId)
        dest.writeParcelable(method, 0)
        dest.writeString(title)
        dest.writeMap(data)
    }

    override fun describeContents() = 0

    companion object {

        @JvmField
        internal val CREATOR = object : Parcelable.Creator<PaymentType> {
            override fun createFromParcel(parcel: Parcel) = PaymentType(parcel)

            override fun newArray(size: Int) = arrayOfNulls<PaymentType>(size)
        }

        /**
         * Map a JSONArray of JSONObjects with payment type data to a list of payment types. You can use
         * this method with data obtained from the Heidelpay backend from your server
         * (e.g. a list of payment types associated with a customer).
         *
         * @param jsonArray: JSONArray containing JSONObject instances with payment type data
         * @return list of payment type data that could be decoded. Any elements that could not be decoded are filtered automatically
         */
        fun map(jsonArray: JSONArray): List<PaymentType> {
            val list = mutableListOf<PaymentType>()
            for (index in 0..(jsonArray.length() - 1)) {
                jsonArray.optJSONObject(index)?.let { jsonObject ->
                    map(jsonObject)?.let { paymentType ->
                        list.add(paymentType)
                    }
                }
            }
            return list
        }

        /**
         * Map a List of Maps with payment type data to a list of payment types. You can use
         * this method with data obtained from the Heidelpay backend from your server
         * (e.g. a list of payment types associated with a customer).
         *
         * @param jsonList: list containing Maps with payment type data
         * @return list of payment type data that could be decoded. Any elements that could not be decoded are filtered automatically
         */
        fun map(jsonList: List<Any>): List<PaymentType> {
            val list = mutableListOf<PaymentType>()

            for (index in 0..(jsonList.count() - 1)) {
                val objectAtIndex = jsonList.get(index)
                if (objectAtIndex is JSONObject) {
                    map(objectAtIndex)?.let { paymentType ->
                        list.add(paymentType)
                    }
                } else if (objectAtIndex is Map<*, *>) {
                    var hasFoundNonStringKey = false
                    objectAtIndex.keys.forEach {
                        if (it == null) {
                            hasFoundNonStringKey = true
                        } else if (it is String == false) {
                            hasFoundNonStringKey = true
                        }
                    }
                    if (hasFoundNonStringKey == false) {
                        map(objectAtIndex as Map<String, Any>)?.let { paymentType ->
                            list.add(paymentType)
                        }
                    }
                }
            }

            return list
        }

        /**
         * convert a JSONObject with payment type data to a PaymentType element
         *
         * @param jsonObject: JSONObject with payment type data
         * @return PaymentType element if decoding was successfull. Otherwise null
         */
        fun map(jsonObject: JSONObject): PaymentType? {
            return map(jsonObject.heidelpay_toMap())
        }

        /**
         * convert a Map with payment type data to a PaymentType element
         *
         * @param jsonMap: map with payment type data
         * @return PaymentType element if decoding was successfull. Otherwise null
         */
        fun map(jsonMap: Map<String, Any>): PaymentType? {
            val methodString = jsonMap.get("method") as? String ?: return null
            val method = PaymentMethod.fromString(methodString) ?: return null
            val paymentId = jsonMap.get("id") as? String ?: return null

            var title: String = method.rawValue
            when (method) {
                Card -> {
                    (jsonMap.get("brand") as? String)?.let {
                        title = it
                    }
                }
                SepaDirectDebit, SepaDirectDebitGuaranteed -> {
                    (jsonMap.get("iban") as? String)?.let {
                        title = it
                    }
                }
                else -> {
                }
            }

            val dataMap = jsonMap.filter { entry ->
                entry.key != "id" && entry.key != "method"
            }

            return PaymentType(paymentId, method, title, dataMap)
        }
    }
}

fun JSONObject.heidelpay_toMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()

    for (key in keys()) {
        optJSONObject(key)?.let {
            map.set(key, it.heidelpay_toMap())
        }
        optJSONArray(key)?.let {
            map.set(key, it.heidelpay_toList())
        }
        opt(key)?.let {
            map.set(key, it)
        }
    }

    return map
}

fun JSONArray.heidelpay_toList(): List<Any> {
    val list = mutableListOf<Any>()

    for (i in 0..(this.length() - 1)) {
        optJSONObject(i)?.let {
            list.add(it.heidelpay_toMap())
        }
        optJSONArray(i)?.let {
            list.add(it.heidelpay_toList())
        }
        opt(i)?.let {
            list.add(it)
        }
    }

    return list
}
