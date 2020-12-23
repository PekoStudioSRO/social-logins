package cz.pekostudio.logins

/**
 * @author Miroslav Hýbler
 */

class LoginConfigurationException(cause: String? = null)
    : IllegalStateException("Login configuration is incorrect \n$cause")
