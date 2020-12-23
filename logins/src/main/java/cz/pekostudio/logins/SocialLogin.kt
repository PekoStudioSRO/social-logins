package cz.pekostudio.logins

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookSdk
import cz.pekostudio.logins.logins.FacebookLogin
import cz.pekostudio.logins.logins.AppleLogin
import cz.pekostudio.logins.logins.GoogleLogin

/**
 * @author Miroslav Hýbler
 * this object is simplest way to authenticate
 * you need to init it before login
 */

@Suppress("UNCHECKED_CAST")
object SocialLogin {

    var defaultWebClientId: String? = null

    lateinit var facebookLogin: FacebookLogin
    lateinit var googleLogin: GoogleLogin
    lateinit var appleLogin: AppleLogin

    /**
     * initialize all login helpers
     */
    fun init(activity: AppCompatActivity) {
        facebookLogin = FacebookLogin(activity)
        googleLogin = GoogleLogin(activity)
        appleLogin = AppleLogin(activity)

        multiple(facebookLogin, googleLogin, appleLogin) {
            it.init(activity)
        }
    }

    /**
     * @return -> Parameter in lambda is token given by sdks, can be null
     */
    fun login(type: Type, onLogin: (String?) -> Unit) {
        when (type) {
            Type.FACEBOOK -> facebookLogin.login {
                onLogin(it.token)
            }
            Type.GOOGLE -> googleLogin.login {
                onLogin(it?.idToken)
            }
            Type.APPLE -> appleLogin.login(onLogin)
        }
    }

    /**
     * logout user
     */
    fun logout() {
        multiple(facebookLogin, googleLogin, appleLogin) {
            it.logout()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        googleLogin.onActivityResult(requestCode, resultCode, data)
        facebookLogin.onActivityResult(requestCode, resultCode, data)
    }

    enum class Type {
        FACEBOOK, GOOGLE, APPLE
    }
}