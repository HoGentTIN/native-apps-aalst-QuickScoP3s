package com.quickdev.projectdashboard.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.quickdev.projectdashboard.App
import com.quickdev.projectdashboard.BuildConfig
import com.quickdev.projectdashboard.data.UserHelper
import com.quickdev.projectdashboard.models.DTO.identity.LoginDTO
import com.quickdev.projectdashboard.util.converters.DateAdapter
import com.quickdev.projectdashboard.util.converters.TimeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object BaseService {

	private val userHelper = App.getUserHelper()

	private val BASE_URL: String
		get() = BuildConfig.BASE_URL

	private val logInterceptor: HttpLoggingInterceptor
		get() {
			val interceptor = HttpLoggingInterceptor()
			interceptor.level = HttpLoggingInterceptor.Level.BODY
			interceptor.redactHeader("Authorization")

			return interceptor
		}

	private val moshi = Moshi.Builder()
		.add(DateAdapter())
		.add(TimeAdapter())
		.add(KotlinJsonAdapterFactory())
		.build()

	val RETROFIT: Retrofit = Retrofit.Builder()
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.addCallAdapterFactory(CoroutineCallAdapterFactory())
		.baseUrl(BASE_URL)
		.client(createHttpClient())
		.build()

	/** Maak een OkHttpClient op basis van debugging status */
	private fun createHttpClient(): OkHttpClient {
		val okHttpClient = OkHttpClient.Builder()
			.connectTimeout(5, TimeUnit.SECONDS)
			.writeTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)

		// Source: https://www.vogella.com/tutorials/Retrofit/article.html
		okHttpClient.addInterceptor { chain ->
			val originalRequest: Request = chain.request()
			val builder = originalRequest
				.newBuilder()
				.header("content-type", "application/json")

			val newRequest: Request = builder.build()
			chain.proceed(newRequest)
		}

		okHttpClient.authenticator(TokenAuthenticator(userHelper))

		if (BuildConfig.DEBUG) {
			// Log all communication while debugging
			okHttpClient.addInterceptor(logInterceptor)

			// Accept all certs for hostnames while debugging & only on emulator
			if (BuildConfig.DEBUG && BuildConfig.IsEmulator) {
				try {
					// Create a trust manager that does not validate certificate chains
					val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
						override fun checkClientTrusted(
							chain: Array<out X509Certificate>?,
							authType: String?
						) = Unit

						override fun checkServerTrusted(
							chain: Array<out X509Certificate>?,
							authType: String?
						) = Unit

						override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
					})

					// Install the all-trusting trust manager
					val sslContext = SSLContext.getInstance("SSL")
					sslContext.init(null, trustAllCerts, SecureRandom())

					// Create an ssl socket factory with our all-trusting manager
					val sslSocketFactory = sslContext.socketFactory
					if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
						okHttpClient.sslSocketFactory(
							sslSocketFactory,
							trustAllCerts.first() as X509TrustManager
						)
						okHttpClient.hostnameVerifier(HostnameVerifier { _, _ -> true })
					}
				} catch (e: Exception) {
				}
			}
		}

		return okHttpClient.build()
	}
}

private class TokenAuthenticator(private val userHelper: UserHelper) : Authenticator {

	override fun authenticate(route: Route?, response: Response): Request? {
		if (!userHelper.isSignedIn) // If user isn't signed in, don't try
			return null

		// If the token is expired, try getting a new one
		if (userHelper.isTokenExpired!!) {
			val creds = userHelper.getUserCredentials()
			val authResponse = try {
				AuthService.HTTP.loginRefresh(LoginDTO(creds.first, creds.second)).execute()
			} catch (e: Exception) {
				return null
			}

			if (authResponse.isSuccessful && authResponse.body() != null) {
				val authDTO = authResponse.body()!!
				userHelper.saveUser(authDTO.authToken, authDTO.picture)

				return response.request.newBuilder()
					.header("Authorization", "Bearer ${authDTO.authToken}")
					.build()
			}

			return null
		}

		return response.request.newBuilder()
			.header("Authorization", "Bearer ${userHelper.authToken!!}")
			.build()
	}
}