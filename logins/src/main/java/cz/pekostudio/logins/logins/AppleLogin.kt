package cz.pekostudio.logins.logins

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleCallback
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.view.SignInWithAppleButton
import cz.pekostudio.logins.LoginConfigurationException
import cz.pekostudio.logins.LoginElement

/**
 * @author Miroslav Hýbler
 * @see LoginElement
 */

class AppleLogin(private val activity: AppCompatActivity): LoginElement<String> {

    companion object {
        private var configuration: SignInWithAppleConfiguration? = null

        fun config(init: AppleLoginConfig.() -> Unit): AppleLoginConfig = AppleLoginConfig().also {
            it.init()
            if (it.isValid)
                configuration = SignInWithAppleConfiguration(it.clientId!!, it.redirectUri!!, it.scope!!)
            else
                throw LoginConfigurationException("Provided data must not be null or empty")
        }
    }

    private lateinit var invisibleButton: SignInWithAppleButton
    private val calllback = AppleLoginCallback()

    init {
        init(activity)
    }

    override var onSuccess: ((String) -> Unit)? = null

    override fun init(activity: AppCompatActivity) {
        invisibleButton = SignInWithAppleButton(activity)
    }

    override fun login(onSuccess: (String) -> Unit) {
        this.onSuccess = onSuccess

        if (configuration != null) {

            if(configuration?.redirectUri.isNullOrEmpty())
                throw LoginConfigurationException("Apple configuration data must not be null or empty.\nYou have to set it via configuration() before login")
            else
                invisibleButton.setUpSignInWithAppleOnClick(activity.supportFragmentManager, configuration!!, calllback)
        }
        else
            throw LoginConfigurationException("Apple configuration is null. You have to set configuration via configuration() before login")

        invisibleButton.performClick()
    }

    override fun logout() {

    }

    inner class AppleLoginCallback : SignInWithAppleCallback {

        override fun onSignInWithAppleSuccess(authorizationCode: String) {
            onSuccess?.invoke(authorizationCode)
        }
        override fun onSignInWithAppleCancel() {
            Log.d("apple-login", "Canceled")
        }

        override fun onSignInWithAppleFailure(error: Throwable) {
            Log.d("apple-login", "Error")
            error.printStackTrace()
        }
    }

    class AppleLoginConfig {
        var clientId: String? = null
        var redirectUri: String? = null
        var scope: String? = "email"

        val isValid get() = !clientId.isNullOrEmpty() &&
                !redirectUri.isNullOrEmpty() && !scope.isNullOrEmpty()
    }
}