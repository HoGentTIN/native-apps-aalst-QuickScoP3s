package com.quickdev.projectdashboard.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.security.keystore.UserNotAuthenticatedException
import android.util.Base64
import com.auth0.android.jwt.JWT
import com.quickdev.projectdashboard.models.domain.User
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId

class UserHelper(context: Context) {

    private val dataHelper: LocalDataHelper = LocalDataHelper("Auth", context)

    val authToken: String?
        get() = dataHelper.getString(LocalDataHelper.Key.STR_USERTOKEN)

    val isSignedIn: Boolean
        get() = authToken != null

    val isTokenExpired: Boolean?
        get() {
            if (!isSignedIn)
                return null

            val jwt = JWT(authToken!!)
            val expiredDate = LocalDateTime.ofInstant(jwt.expiresAt!!.toInstant(), ZoneId.systemDefault())
            return LocalDateTime.now().isAfter(expiredDate)
        }

    fun getSignedInUser(): User? {
        if (!isSignedIn)
            return null

        val afbeelding = dataHelper.getString(LocalDataHelper.Key.STR_USERPICTURE)!!

        val jwt = JWT(authToken!!)

        val id = jwt.getClaim("sub").asInt()!!
        val email = jwt.getClaim("email").asString()!!
        val firstName = jwt.getClaim("given_name").asString()!!
        val lastName = jwt.getClaim("family_name").asString()!!
        val phoneNr = jwt.getClaim("phone_number").asString()!!
        val companyId = jwt.getClaim("company").asInt()

        return User(id, afbeelding, firstName, lastName, email, phoneNr, companyId)
    }

    fun signOut() {
        dataHelper.remove(LocalDataHelper.Key.BOOL_ISFIRSTSETUP)
        dataHelper.remove(LocalDataHelper.Key.STR_USERPICTURE)
        dataHelper.remove(LocalDataHelper.Key.STR_USERTOKEN)
        dataHelper.remove(LocalDataHelper.Key.STR_USERNAME)
        dataHelper.remove(LocalDataHelper.Key.STR_USERPASS)
        dataHelper.applyChanges()
    }

    fun setUserCredentials(email: String, password: String) {
        dataHelper.put(LocalDataHelper.Key.STR_USERNAME, email)
        dataHelper.put(LocalDataHelper.Key.STR_USERPASS, password)
        dataHelper.applyChanges()
    }

    fun getUserCredentials(): Pair<String, String> {
        if (!isSignedIn)
            throw UserNotAuthenticatedException()

        val email = dataHelper.getString(LocalDataHelper.Key.STR_USERNAME)!!
        val password = dataHelper.getString(LocalDataHelper.Key.STR_USERPASS)!!

        return Pair(email, password)
    }

    fun saveUser(authToken: String, afbeelding: String?) {
        dataHelper.put(LocalDataHelper.Key.STR_USERTOKEN, authToken)

        if (afbeelding != null)
            dataHelper.put(LocalDataHelper.Key.STR_USERPICTURE, afbeelding)
        else
            dataHelper.put(LocalDataHelper.Key.STR_USERPICTURE, "")

        dataHelper.applyChanges()
    }
}

object UserPictureHelper {

    fun decodeImage(base64String: String?): Bitmap? {
        if (base64String == null || base64String.isBlank())
            return null

        val decodedString = Base64.decode(base64String, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun encodePicture(inputStream: InputStream): String? {
        val bytes: ByteArray
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()
        try {
            bytesRead = inputStream.read(buffer)
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bytes = output.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
