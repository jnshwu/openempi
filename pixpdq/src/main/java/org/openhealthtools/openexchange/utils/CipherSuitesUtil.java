/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.utils;

/**
 * This util class contains cipher suites related utilities.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class CipherSuitesUtil {
   //Supported ciper suites by SunJSSE, see 
   //http://download.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJSSEProvider
   public static final String[] allCipherSuites = {
        "SSL_RSA_WITH_RC4_128_MD5",
		"SSL_RSA_WITH_RC4_128_SHA",
		"TLS_RSA_WITH_AES_128_CBC_SHA",
		"TLS_RSA_WITH_AES_256_CBC_SHA",
		"TLS_ECDH_ECDSA_WITH_RC4_128_SHA",
		"TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA",
		"TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA",
		"TLS_ECDH_RSA_WITH_RC4_128_SHA",
		"TLS_ECDH_RSA_WITH_AES_128_CBC_SHA",
		"TLS_ECDH_RSA_WITH_AES_256_CBC_SHA",
		"TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
		"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
		"TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
		"TLS_ECDHE_RSA_WITH_RC4_128_SHA",
		"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
		"TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
		"TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
		"TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
		"TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
		"TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
		"SSL_RSA_WITH_3DES_EDE_CBC_SHA",
		"TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA",
		"TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA",
		"TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
		"TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
		"SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
		"SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
		"SSL_RSA_WITH_DES_CBC_SHA",
		"SSL_DHE_RSA_WITH_DES_CBC_SHA",
		"SSL_DHE_DSS_WITH_DES_CBC_SHA",
		"SSL_RSA_EXPORT_WITH_RC4_40_MD5",
		"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
		"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
		"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA",
		"SSL_RSA_WITH_NULL_MD5",
		"SSL_RSA_WITH_NULL_SHA",
		"TLS_ECDH_ECDSA_WITH_NULL_SHA",
		"TLS_ECDH_RSA_WITH_NULL_SHA",
		"TLS_ECDHE_ECDSA_WITH_NULL_SHA",
		"TLS_ECDHE_RSA_WITH_NULL_SHA",
		"SSL_DH_anon_WITH_RC4_128_MD5",
		"TLS_DH_anon_WITH_AES_128_CBC_SHA",
		"TLS_DH_anon_WITH_AES_256_CBC_SHA",
		"SSL_DH_anon_WITH_3DES_EDE_CBC_SHA",
		"SSL_DH_anon_WITH_DES_CBC_SHA",
		"TLS_ECDH_anon_WITH_RC4_128_SHA",
		"TLS_ECDH_anon_WITH_AES_128_CBC_SHA",
		"TLS_ECDH_anon_WITH_AES_256_CBC_SHA",
		"TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA",
		"SSL_DH_anon_EXPORT_WITH_RC4_40_MD5",
		"SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA",
		"TLS_ECDH_anon_WITH_NULL_SHA",
		"TLS_KRB5_WITH_RC4_128_SHA",
		"TLS_KRB5_WITH_RC4_128_MD5",
		"TLS_KRB5_WITH_3DES_EDE_CBC_SHA",
		"TLS_KRB5_WITH_3DES_EDE_CBC_MD5",
		"TLS_KRB5_WITH_DES_CBC_SHA",
		"TLS_KRB5_WITH_DES_CBC_MD5",
		"TLS_KRB5_EXPORT_WITH_RC4_40_SHA",
		"TLS_KRB5_EXPORT_WITH_RC4_40_MD5",
		"TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA",
		"TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5",
		"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"};

}
