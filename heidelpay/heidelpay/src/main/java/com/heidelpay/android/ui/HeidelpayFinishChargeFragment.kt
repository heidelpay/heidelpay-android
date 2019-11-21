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
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.heidelpay.android.R
import kotlinx.android.synthetic.main.fragment_finish_charge.*

/**
 * Listener for the Finish Charge Fragment
 */

interface HeidelpayFinishChargeFragmentListener {
    /**
     * This method is called when the the payment specific payment UI has finished or the user canceled the process.
     * You should than dismiss the HeidelpayFinishChargeFragment.
     * *Note:* You have to check the status your backend to get the current state of the payment!
     * @param    canceledByUser  true if the user canceled the process by tapping the cancel button. This does not
    necessarily indicate that the payment was canceled. You have to check the state from
    your server!
     * */
    fun heidelpayChargeFragmentDidFinish(canceledByUser: Boolean)

}

/**
 * This fragment handles pending charges where the user must confirm the payment with a specific (web) UI.
 */
class HeidelpayFinishChargeFragment : Fragment() {
    private var listener: HeidelpayFinishChargeFragmentListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_finish_charge, container, false)

        setHasOptionsMenu(true)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val redirectUrl = arguments?.getString(EXTRA_REDIRECT_URL)
        if (redirectUrl != null) {
            finishChargeWebView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val webViewUrl = request?.url
                    val returnUrl = arguments?.getString(EXTRA_RETURN_URL)
                    if (returnUrl != null) {
                        if (webViewUrl != null && webViewUrl.toString().startsWith(returnUrl)) {
                            listener?.heidelpayChargeFragmentDidFinish(false)
                            return false
                        }
                    } else {
                        Log.e(TAG, "No redirect URL found")
                    }

                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            finishChargeWebView.settings.javaScriptEnabled = true
            finishChargeWebView.loadUrl(redirectUrl)
        } else {
            Log.e(TAG, "No redirect URL found")
            listener?.heidelpayChargeFragmentDidFinish(false)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HeidelpayFinishChargeFragmentListener) {
            this.listener = context
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_finish_charge_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.finish_charge_cancel_action -> {
                listener?.heidelpayChargeFragmentDidFinish(true)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val TAG = "HPFinishedCharge"
        val EXTRA_REDIRECT_URL = this@Companion.javaClass.canonicalName + ".EXTRA_REDIRECT_URL"
        val EXTRA_RETURN_URL = this@Companion.javaClass.canonicalName + ".EXTRA_RETURN_URL"
    }
}
