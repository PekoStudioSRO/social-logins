package cz.pekostudio.logins.facebook

import android.util.Log
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import cz.pekostudio.logins.logins.FacebookLogin

/**
 * @author Miroslav Hýbler
 */

class DefaultFacebookLoginCallback(private val facebookHelper: FacebookLogin): FacebookCallback<LoginResult?> {

    override fun onSuccess(loginResult: LoginResult?) {
        val accessToken = AccessToken.getCurrentAccessToken()
        facebookHelper.onSuccess?.invoke(accessToken)
    }

    override fun onCancel() {
        Log.d("facebook-login", "Facebook login canceled")
    }

    override fun onError(exception: FacebookException) {
        Log.d("facebook-login", exception.message + " " + exception.cause + " " + exception.localizedMessage)
    }
}