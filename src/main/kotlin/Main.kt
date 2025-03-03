const val ASTERISK = "*"
const val ILLEGAL_INPUT = "Illegal Flag Argument - for \"*\""

fun main(args: Array<String>) {
    try {
        when {
            args.isEmpty() -> throw IllegalArgumentException(
                ILLEGAL_INPUT.replace(ASTERISK, "Empty Array")
            )
            else -> {
                val arguments = groupCliArguments(args)
                println(encryptDecryptWithKey(
                    mode = arguments["mode"]!!,
                    key = arguments["key"]!!.toInt(),
                    data = arguments["data"]!!
                ))
            }
        }
    } catch (e: Exception) {
        println(e.localizedMessage)
    }
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

/**
 *  organizes & returns a map of arguments
 *  passed into the program for the following
 *  flags: -mode, -key, -data
 *  @param args string array storing program arguments
 *  @throws IllegalArgumentException
 */
private fun groupCliArguments(args: Array<String>): Map<String, String> {
    // map for flag & argument values
    val map = mutableMapOf<String, String>()

    // lambda flag check
    val isNotFlag = { s: String -> s !in listOf("-mode", "-key", "-data") }

    // organize arguments
    for (i in args.indices step 2) {
        // check index in range
        if (i.inc() != args.lastIndex.inc()) {
            // current index
            when (args[i]) {
                "-mode" -> {    // check next index not flag & update with index else default
                    map["mode"] = if (isNotFlag(args[i.inc()])) args[i.inc()] else "enc"
                }
                "-key" -> {    // check next index not flag & update with index else default
                    map["key"] = if (isNotFlag(args[i.inc()]) &&
                        Character.isDigit(args[i.inc()].first())
                    ) args[i.inc()] else "0"
                }
                "-data" -> {    // check next index not flag & update with index else default
                    map["data"] = if (isNotFlag(args[i.inc()])) args[i.inc()] else ""
                }               // throw exception for all other cases
                else -> throw IllegalArgumentException(ILLEGAL_INPUT.replace(ASTERISK, args[i]))
            }
        }
    }
    // return arguments
    return map
}