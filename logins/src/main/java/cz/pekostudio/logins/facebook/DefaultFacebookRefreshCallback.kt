package cz.pekostudio.logins.facebook

import android.util.Log
import com.facebook.AccessToken
import com.facebook.FacebookException

/**
 * @author Miroslav Hýbler
 */

class DefaultFacebookRefreshCallback(): AccessToken.AccessTokenRefreshCallback {

    override fun OnTokenRefreshed(accessToken: AccessToken?) {
        Log.d("facebook-refresh", "success")
    }

    override fun OnTokenRefreshFailed(exception: FacebookException?) {
        Log.d("facebook-refresh", "facebook refresh tokens error")
        exception?.printStackTrace()
    }
}