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

package com.heidelpay.android.backend

import android.net.http.X509TrustManagerExtensions
import android.os.AsyncTask
import android.util.Base64
import com.heidelpay.android.types.PublicKey
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

/**
 * Internal lambda expression for callback from AsyncTask to BackendService
 */
typealias HeidelpayHttpBackendServiceInternalCompletion = (urlConnection: HttpsURLConnection, responseString: String?, exception: Exception?) -> Unit

/**
 * implementation of the BackendService protocol which does concrete HTTPs calls.
 */
internal class HttpBackendService(private val publicKey: PublicKey, private val environment: Environment) : BackendService {

    /// Builds a HttpsURLConnection from the given request with an optional JSONObject to pass as the request body
    internal fun buildUrlConnection(request: HeidelpayRequest): Pair<HttpsURLConnection, JSONObject?>? {
        val requestURL = environment.fullURLPathForPath(request.requestPath)
        val urlConnection = requestURL.openConnection() as? HttpsURLConnection
        if (urlConnection != null) {
            urlConnection.addRequestProperty("Accept", "application/json")
            urlConnection.addRequestProperty("Accept-Language", "en")
            urlConnection.addRequestProperty("Authorization", publicKey.authorizationHeaderValue)

            var dataObject: JSONObject? = null
            if (request is HeidelpayDataRequest) {
                urlConnection.requestMethod = "POST"
                urlConnection.addRequestProperty("Content-Type", "application/json")
                dataObject = request.encodeToJSON()
            } else {
                urlConnection.requestMethod = "GET"
            }

            return Pair(urlConnection, dataObject)
        }

        return null
    }

    /// Performs a request
    override fun performRequest(request: HeidelpayRequest, completion: HeidelpayBackendRequestCompletion) {
        val urlConnectionAndDataObject = buildUrlConnection(request)
        if (urlConnectionAndDataObject != null) {
            BackendRequestAsyncTask().execute(BackendRequestParams(urlConnectionAndDataObject.first, urlConnectionAndDataObject.second, environment.pinnedPublicKeyHash) { urlConnection, responseString, exception ->
                val response = buildResponse(urlConnection, responseString, exception)
                if (response.first != null) {
                    completion(request.createResponseFromDataString(response.first!!), null)
                } else {
                    completion(null, response.second)
                }
            })
        } else {
            completion(null, BackendError.InvalidRequest())
        }
    }

    /// Builds the response from an urlconnection and an optional response or exception
    internal fun buildResponse(urlConnection: HttpsURLConnection, responseString: String?, exception: Exception?): Pair<String?, BackendError?> {
        if (exception != null) {
            if (exception is IOException) {
                return Pair(null, BackendError.NoInternet())
            } else {
                return Pair(null, BackendError.RequestFailed(exception))
            }
        } else {
            if (urlConnection.responseCode >= 400) {
                return Pair(null, BackendError.ServerHTTPError(urlConnection.responseCode))
            } else if (responseString != null) {
                val backendServerErrorResponse = BackendServerErrorResponse.fromJSONString(responseString)

                if (backendServerErrorResponse != null) {
                    return Pair(null, BackendError.ServerResponseError(backendServerErrorResponse.errors))
                } else {
                    return Pair(responseString, null)
                }
            } else {
                return Pair(null, BackendError.InvalidRequest())
            }
        }
    }
}

/**
 * Helper type to pass information to the asynchronous backend task
 */
internal data class BackendRequestParams(val urlConnection: HttpsURLConnection, val dataObject: JSONObject?, val pinHash: String, val completion: HeidelpayHttpBackendServiceInternalCompletion) {

}

/**
 * Helper type to pass information to the post execution block of the asynchronous backend task
 */
internal data class BackendRequestResponse(val urlConnection: HttpsURLConnection, val responseString: String?, val exception: Exception?, val completion: HeidelpayHttpBackendServiceInternalCompletion) {

}

/**
 * Asynchronous backend task that performs a backend call
 */
internal class BackendRequestAsyncTask : AsyncTask<BackendRequestParams, String, BackendRequestResponse>() {

    override fun doInBackground(vararg request: BackendRequestParams): BackendRequestResponse {
        val backendRequestParams = request.get(0)

        try {
            val urlConnection = backendRequestParams.urlConnection
            urlConnection.connect()
            val publicKeyForPinning = backendRequestParams.pinHash
            urlConnection.pin(publicKeyForPinning)

            if (backendRequestParams.dataObject != null) {
                urlConnection.outputStream.write(backendRequestParams.dataObject.toString().toByteArray())
            }

            val responseString = readStream(urlConnection.inputStream)

            return BackendRequestResponse(urlConnection, responseString, null, backendRequestParams.completion)
        } catch (e: Exception) {
            return BackendRequestResponse(backendRequestParams.urlConnection, null, e, backendRequestParams.completion)
        }
    }

    override fun onPostExecute(result: BackendRequestResponse) {
        super.onPostExecute(result)

        result.completion(result.urlConnection, result.responseString, result.exception)
    }

    private fun readStream(input: InputStream): String {
        var reader: BufferedReader? = null
        val response = StringBuffer()
        try {
            reader = BufferedReader(InputStreamReader(input))
            var line = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }
        } catch (e: Exception) {

        } finally {
            reader?.close()
        }
        return response.toString();
    }
}

/**
 * Performs public key pinning
 * @param   publicKeyToPin              the expected pubic key of the server
 * @throws  SSLPeerUnverifiedException  if the pubic keys don't match
 */
private fun HttpsURLConnection.pin(publicKeyToPin: String) {
    val trustManagerExt = getTrustManagerExt()

    var certChainMsg = ""
    try {
        val md = MessageDigest.getInstance("SHA-256")
        val trustedChain = trustedChain(trustManagerExt)
        for (cert in trustedChain) {
            val publicKeyOfServer = cert.getPublicKey().getEncoded()
            md.update(publicKeyOfServer, 0, publicKeyOfServer.size)
            val pin = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            certChainMsg += "    sha256/" + pin + " : " + cert.getSubjectDN().toString() + "\n"
            if (publicKeyToPin.equals(pin)) {
                return
            }
        }
    } catch (e: NoSuchAlgorithmException) {
        throw SSLException(e)
    }

    throw SSLPeerUnverifiedException("Certificate pinning " +
            "failure\n  Peer certificate chain:\n" + certChainMsg)
}

/**
 * Checks if the server certificates of the HttpsURLConnection are trusted
 * @throws  SSLException    if certificates are not trusted
 */
@Throws(SSLException::class)
private fun HttpsURLConnection.trustedChain(trustManagerExt: X509TrustManagerExtensions): List<X509Certificate> {
    val serverCerts = getServerCertificates()
    val untrustedCerts = Arrays.copyOf(serverCerts, serverCerts.size, Array<X509Certificate>::class.java)
    val host = getURL().getHost()
    try {
        return trustManagerExt.checkServerTrusted(untrustedCerts, "RSA", host)
    } catch (e: CertificateException) {
        throw SSLException(e)
    }
}

/**
 * Creates a X509TrustManagerExtensions used for pubic key pinning
 */
private fun getTrustManagerExt(): X509TrustManagerExtensions {
    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(null as KeyStore?)

    var x509TrustManager: X509TrustManager? = null
    for (trustManager in trustManagerFactory.getTrustManagers()) {
        if (trustManager is X509TrustManager) {
            x509TrustManager = trustManager
            break
        }
    }
    val trustManagerExt = X509TrustManagerExtensions(x509TrustManager)
    return trustManagerExt
}
