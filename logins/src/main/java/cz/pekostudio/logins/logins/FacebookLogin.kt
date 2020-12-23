package cz.pekostudio.logins.logins

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import cz.pekostudio.logins.LoginElement
import cz.pekostudio.logins.facebook.DefaultFacebookLoginCallback
import cz.pekostudio.logins.facebook.DefaultFacebookRefreshCallback

/**
 * @author Miroslav Hýbler
 */

@Suppress("UNCHECKED_CAST")
class FacebookLogin(private val activity: AppCompatActivity): LoginElement<AccessToken> {

    companion object {
        val defaultPermissions = arrayListOf("email", "public_profile")
        val permissions = arrayListOf("email", "public_profile", "user_gender", "user_birthday", "user_link", "user_photos")
    }

    private val loginManager: LoginManager = LoginManager.getInstance()
    private val loginCallbackManger: CallbackManager = CallbackManager.Factory.create()
    private var loginCallback: FacebookCallback<LoginResult?> = DefaultFacebookLoginCallback(this)

    val isLoggedIn get() =
        AccessToken.getCurrentAccessToken() != null

    private val expired get() =
        AccessToken.getCurrentAccessToken()?.isExpired == true

    val token: String get() =
        AccessToken.getCurrentAccessToken().token

    override var onSuccess: ((AccessToken) -> Unit)? = null


    override fun init(activity: AppCompatActivity) {

        if (isLoggedIn && expired)
            refresh()
    }

    override fun login(onSuccess: (AccessToken) -> Unit) {
        this.onSuccess = onSuccess
        loginManager.registerCallback(loginCallbackManger, loginCallback)
        loginManager.logInWithReadPermissions(activity, defaultPermissions)
    }

    override fun logout() {
        loginManager.logOut()
    }

    private fun refresh() {
        AccessToken.refreshCurrentAccessTokenAsync(DefaultFacebookRefreshCallback())
    }

    fun <C: FacebookCallback<LoginResult?>> setCallback(callback: C) {
        this.loginCallback = callback
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loginCallbackManger.onActivityResult(requestCode, resultCode, data)
    }
}