fun main() {
    println(encryptDecryptWithKey(
        "enc",
        "Welcome to hyperskill!",
        5
    ))
}

/**
 *  returns a new string with each
 *  character shifted a number of times
 *  to the Left/Right according to the
 *  provided Mode & Key value
 *  @param mode string value deciding encryption/decryption
 *  @param message string message to encrypt/decrypt with cypher
 *  @param key determines the number of Left/Right shifts
 */
private fun encryptDecryptWithKey(mode: String, message: String, key: Int): String {
    val logic = { char: Char ->
        when (mode) {
            "enc" -> char + key
            "dec" -> char - key
            else -> char
        }
    }
    return message.map { logic(it) }.joinToString("")
}