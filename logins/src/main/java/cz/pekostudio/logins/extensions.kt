package cz.pekostudio.logins

inline fun <E> multiple(vararg params: E, block: (E) -> Unit) =
    params.forEach(block)