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

package com.heidelpay.android.types

import org.json.JSONObject

/**
 * Customer data to be used in authorize or charges calls.
 * The customer component is an additional input form for certain payment methods.
 */
class Customer(

        /// First name (max: 40 chars)
        val firstname: String? = null,

        /// Last name (max: 40 chars)
        val lastname: String? = null,

        /// Must be unique and identifies the customer. Can be used in place of the resource id (max: 256 chars)
        val customerId: String? = null,

        /// Must be either 'mr', 'mrs' or 'unknown'
        val salutation: String? = null,

        /// Company name (max: 40 chars)
        val company: String? = null,

        /// Birthdate of the customer in format yyyy-mm-dd or dd.mm.yyyy
        val birthDate: String? = null,

        /// E-Mail of customer (max: 100 chars)
        val email: String? = null,

        /// Phone number of the customer (max: 20 chars)
        val phone: String? = null,

        /// Mobile phone (max: 40 chars)
        val mobile: String? = null,

        /// Billing address of the customer
        val billingAddress: CustomerAddress? = null,

        /// Shipping address of the customer
        val shippingAddress: CustomerAddress? = null,

        /// Company Info
        val companyInfo: CompanyInfo? = null) : JsonType {

    override fun encodeAsJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("firstname", firstname)
        jsonObject.put("lastname", lastname)
        jsonObject.put("customerId", customerId)
        jsonObject.put("salutation", salutation)
        jsonObject.put("company", company)
        jsonObject.put("birthDate", birthDate)
        jsonObject.put("email", email)
        jsonObject.put("phone", phone)
        jsonObject.put("mobile", mobile)
        billingAddress?.let { jsonObject.put("billingAddress", it.encodeAsJSON()) }
        shippingAddress?.let { jsonObject.put("shippingAddress", it.encodeAsJSON()) }
        companyInfo?.let {
            jsonObject.put("companyInfo", it.encodeAsJSON())
        }

        return jsonObject
    }

    companion object {
        /**
         * Create a customer structure for a natural / private customer
         * @param firstname: First name (max: 40 chars)
         * @param lastname: Last name (max: 40 chars)
         * @param customerId: Unique identifier of the customer
         * @param salutation Must be either 'mr', 'mrs' or 'unknown'
         * @param company: The name of the company
         * @param birthDate: Birthdate of the customer in format yyyy-mm-dd or dd.mm.yyyy
         * @param email: email of the customer
         * @param phone: phone number of customer
         * @param mobile: mobile phone number of customer
         * @param billingAddress: Billing address of the customer
         * @param shippingAddress: shipping address of customer
         */
        fun createCustomer(firstname: String, lastname: String, customerId: String? = null,
                           salutation: String? = null, company: String? = null, birthDate: String? = null,
                           email: String? = null, phone: String? = null,
                           mobile: String? = null, billingAddress: CustomerAddress? = null,
                           shippingAddress: CustomerAddress? = null): Customer {

            return Customer(firstname, lastname, customerId, salutation,
                    company, birthDate, email, phone, mobile,
                    billingAddress, shippingAddress, null)
        }

        /**
         * Create a customer structure for a registered company
         * @param company: The name of the company
         * @param commercialRegisterNumber: The identity of the company to verify the legal existence
         * @param billingAddress: Billing address of the company
         * @param customerId: Unique identifier of the customer
         * @param email: email of the company
         * @param phone: phone number of company
         * @param mobile: mobile phone number of company
         * @param shippingAddress: shipping address of company
         */
        fun createRegisteredCompanyCustomer(company: String, commercialRegisterNumber: String, billingAddress: CustomerAddress, customerId: String? = null, email: String? = null, phone: String? = null, mobile: String? = null, shippingAddress: CustomerAddress? = null): Customer {
            return Customer(null, null, customerId, null, company, null, email, phone, mobile, billingAddress, shippingAddress, CompanyInfo.registered(commercialRegisterNumber))
        }


        /**
         * Create a customer structure for a non registered company
         * @param firstname: First name (max: 40 chars)
         * @param lastname: Last name (max: 40 chars)
         * @param company: The name of the company
         * @param birthDate: Birthdate of the customer in format yyyy-mm-dd or dd.mm.yyyy
         * @param email: email of the customer
         * @param billingAddress: Billing address of the customer
         * @param function: Customer's working function
         * @param commercialSector: Customer's  working commercial sector
         * @param customerId: Unique identifier of the customer
         * @param salutation Must be either 'mr', 'mrs' or 'unknown'
         * @param phone: phone number of customer
         * @param mobile: mobile phone number of customer
         * @param shippingAddress: shipping address of customer
         */

        fun createNonRegisteredCompanyCustomer(firstname: String, lastname: String, company: String, birthDate: String, email: String, billingAddress: CustomerAddress, function: String,
                                               commercialSector: String, customerId: String? = null, salutation: String? = null, phone: String? = null, mobile: String? = null,
                                               shippingAddress: CustomerAddress? = null): Customer {

            return Customer(firstname, lastname, customerId, salutation,
                    company, birthDate, email, phone, mobile,
                    billingAddress, shippingAddress,
                    CompanyInfo.nonRegistered(function, commercialSector))
        }
    }
}

/**
 * Address structure of a customer
 */
class CustomerAddress(

        /// Billing address first and last name (max. 40 chars)
        val name: String,

        /// Billing address street (max. 50 chars)
        val street: String,

        /// Billing address state in ISO 3166-2 format (max. 8 chars)
        val state: String,

        /// Billing address zip code (max. 10 chars)
        val zip: String,

        /// Billing address city (max. 30 chars)
        val city: String,

        /// Billing address country in ISO A2 format (max. 2 chars)
        val country: String) : JsonType {

    override fun encodeAsJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("street", street)
        jsonObject.put("state", state)
        jsonObject.put("zip", zip)
        jsonObject.put("city", city)
        jsonObject.put("country", country)
        return jsonObject
    }
}


/**
 * Company Info of a registered company
 */
class CompanyInfo(

        /// The fix value: registered
        val registrationType: String,

        /// The identify your company or limited partnership and verify its legal existence as an incorporated entity
        val commercialRegisterNumber: String?,

        /// The identify your working function
        val function: String?,

        /// The identify your working commercial sector
        val commercialSector: String?) : JsonType {

    override fun encodeAsJSON(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("registrationType", registrationType)
        jsonObject.put("commercialRegisterNumber", commercialRegisterNumber)
        jsonObject.put("function", function)
        jsonObject.put("commercialSector", commercialSector)
        return jsonObject
    }

    companion object {

        fun registered(commercialRegisterNumber: String): CompanyInfo {
            return CompanyInfo("registered", commercialRegisterNumber, null, null)
        }

        fun nonRegistered(function: String, commercialSector: String): CompanyInfo {
            return CompanyInfo("not_registered", null, function, commercialSector)
        }
    }
}
