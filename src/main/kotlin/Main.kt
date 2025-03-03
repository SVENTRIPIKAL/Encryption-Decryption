const val ASTERISK = "*"
const val ILLEGAL_INPUT = "IllegalArgumentException - for \"*\""

fun main(args: Array<String>) {
    try {
        val arguments = groupCliArguments(args)
        println(encryptDecryptWithKey(
            mode = arguments["mode"]!!,
            key = arguments["key"]!!.toInt(),
            data = arguments["data"]!!
        ))
    } catch (e: IllegalArgumentException) {
        println(e.localizedMessage)
    }
}

/**
 *  organizes & returns a map of arguments
 *  passed into the program for the following
 *  flags: -mode, -key, -data
 *  @param args string array storing program arguments
 *  @throws IllegalArgumentException
 */
private fun groupCliArguments(args: Array<String>): Map<String, String> {
    // check if array empty or contains blank arguments
    if (args.size != 6 || args.contains(""))
        throw IllegalArgumentException(
            ILLEGAL_INPUT.replace(ASTERISK, "Missing Arguments")
        )

    // map for flag & argument values
    val map = mutableMapOf<String, String>()

    // lambda flag check
    val isNotFlag = { f: String -> f !in listOf("-mode", "-key", "-data") }

    // lambda mode check
    val isValidMode = { m: String -> m in listOf("enc", "dec") }

    // lambda key check
    val isValidKey = { k: String -> Character.isDigit(k.first()) }

    // organize arguments
    for (i in args.indices step 2) {
        // current & next indices
        val currentI = args[i]; val nextI = args[i.inc()]
        when (currentI) {
            "-mode" -> {    // check next index valid mode else default
                map["mode"] = if (isValidMode(nextI)) nextI else "enc"
            }
            "-key" -> {     // check next index valid key else default
                map["key"] = if (isValidKey(nextI)) nextI else "0"
            }
            "-data" -> {    // check next index not flag else default
                map["data"] = if (isNotFlag(nextI)) nextI else ""
            }               // throw exception for unknown flags
            else -> throw IllegalArgumentException(ILLEGAL_INPUT.replace(ASTERISK, currentI))
        }
    }
    // return arguments
    return map
}

/**
 *  returns a new string with each
 *  character shifted a number of times
 *  to the Left/Right according to the
 *  provided Mode & Key value
 *  @param mode string value deciding encryption/decryption
 *  @param key determines the number of Left/Right shifts
 *  @param data string message to encrypt/decrypt with key
 */
private fun encryptDecryptWithKey(mode: String, key: Int, data: String): String {
    val logic = { char: Char ->
        when (mode) {
            "enc" -> char + key
            "dec" -> char - key
            else -> char
        }
    }
    return data.map { logic(it) }.joinToString("")
}
