package cz.pekostudio.logins

import androidx.appcompat.app.AppCompatActivity

/**
 * @author Miroslav Hýbler
 * implement this interface in helper classes for login
 * @param R -> Result data of successful login
 */

interface LoginElement<R> {

    /**
     * invoke method when login is successful and pass result as parameter
     */
    var onSuccess: ((R) -> Unit)?

    /**
     * call this method in primary constructor and
     * use it to initialize configs/options/other necessary things
     */
    fun init(activity: AppCompatActivity)

    /**
     * store onSuccess method and implement required login functionality
     */
    fun login(onSuccess: ((R) -> Unit))

    /**
     * Some platform keep your data, so its important to log out user
     */
    fun logout()
}