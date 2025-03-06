import java.io.File
import javax.swing.text.html.HTML.Tag.HEAD

fun main(args: Array<String>) {
    try {
        val arguments = parseCliArguments(args)
        parseEncryptDecrypt(
            alg = arguments[ARG_ALG]!!,
            mode = arguments[ARG_MODE]!!,
            key = arguments[ARG_KEY]!!.toInt(),
            data = arguments[ARG_DATA]!!,
            inFile = arguments[ARG_IN]!!,
            outFile = arguments[ARG_OUT]!!
        )
    } catch (e: IllegalArgumentException) {
        println(e.localizedMessage)
    }
}

/**
 *  organizes & returns a map of arguments
 *  passed into the program for the following
 *  flags: -alg, -mode, -key, -data, -in, -out
 *  @param args string array storing program arguments
 *  @throws IllegalArgumentException
 */
private fun parseCliArguments(args: Array<String>): Map<String, String> {
    // check if array empty or contains blank arguments
    when {
        args.size !in SIX..TWELVE -> throw IllegalArgumentException(
            ILLEGAL_INPUT.replace(ASTERISK, ERROR_NUMBER)
        )
        args.contains(EMPTY_STRING) -> throw IllegalArgumentException(
            ILLEGAL_INPUT.replace(ASTERISK, ERROR_MISSING)
        )
    }
    // map default argument values
    val map = mutableMapOf(
        ARG_ALG to ALG_SHIFT, ARG_MODE to MODE_ENC, ARG_KEY to "$ZERO",
        ARG_DATA to EMPTY_STRING, ARG_IN to EMPTY_STRING, ARG_OUT to EMPTY_STRING
    )
    // trim array arguments
    val array = args.map { it.trim() }.toTypedArray()

    // lambda flag check
    val isNotFlag = { f: String -> f !in listOf(
        "-$ARG_ALG", "-$ARG_MODE", "-$ARG_KEY",
        "-$ARG_DATA", "-$ARG_IN", "-$ARG_OUT")
    }

    // lambda algorithm check
    val isValidAlgo = { a: String -> a in listOf(ALG_SHIFT, ALG_UNICODE) }

    // lambda mode check
    val isValidMode = { m: String -> m in listOf(MODE_ENC, MODE_DEC) }

    // lambda key check
    val isValidKey = { k: String -> Character.isDigit(k.first()) }

    // lambda return if true else
    val checkIfElse = { x: (String) -> Boolean, y: String, z: String -> if (x(y)) y else z }

    // organize arguments
    for (i in array.indices step TWO) {
        // current & next indices
        val currentI = array[i]; val nextI = array[i.inc()]
        when (currentI) {
            "-$ARG_ALG" -> {    // check next index valid algo else default
                map[ARG_ALG] = checkIfElse(isValidAlgo, nextI, ALG_SHIFT)
            }
            "-$ARG_MODE" -> {    // check next index valid mode else default
                map[ARG_MODE] = checkIfElse(isValidMode, nextI, MODE_ENC)
            }
            "-$ARG_KEY" -> {     // check next index valid key else default
                map[ARG_KEY] = checkIfElse(isValidKey, nextI, "$ZERO")
            }
            "-$ARG_DATA" -> {    // check next index not flag else default
                map[ARG_DATA] = checkIfElse(isNotFlag, nextI, EMPTY_STRING)
            }
            "-$ARG_IN" -> {      // check next index not flag else default
                map[ARG_IN] = checkIfElse(isNotFlag, nextI, EMPTY_STRING)
            }
            "-$ARG_OUT" -> {     // check next index not flag else default
                map[ARG_OUT] = checkIfElse(isNotFlag, nextI, EMPTY_STRING)
            }
            // throw exception for unknown flags
            else -> throw IllegalArgumentException(ILLEGAL_INPUT.replace(ASTERISK, currentI))
        }
    }
    // return arguments
    return map
}

/**
 *  prints/writes a new string with each
 *  character shifted a number of times
 *  to the Left/Right according to the
 *  provided Algorithm, Mode, & Key value
 *  @param alg encryption/decryption style: shift/unicode
 *  @param mode string value deciding encryption/decryption
 *  @param key determines the number of Left/Right shifts
 *  @param data string message to encrypt/decrypt with key
 *  @param inFile file read-from path
 *  @param outFile file write-to path
 *  @throws NoSuchFileException
 */
private fun parseEncryptDecrypt(
    alg: String,
    mode: String,
    key: Int,
    data: String,
    inFile: String,
    outFile: String
) {
    // default read/write path
    val defaultPath = System.getProperty(USER_DIR) + "${File.separator}.test${File.separator}"

    // regex capturing file + extension
    val fileFormatRegex = FILE_EXT_REGEX.toRegex()

    // lambda checking if string is valid file format
    val isValidFormat = { s: String -> s.matches(fileFormatRegex) }

    // lambda encrypt/decrypt by mode + algo
    val logic = { char: Char ->
        when (alg) {
            ALG_SHIFT -> {  // shift characters from front/back of alphabet
                val alphaRange = 'a'..'z'
                val lowerCased = char.lowercase().first()
                if (lowerCased !in alphaRange) char // return char if not letter
                else {
                    when (mode) {
                        MODE_ENC -> {   // encrypt
                            val temp = lowerCased + key // if pass z, count from a
                            if (temp > alphaRange.last) char + key - TWO_SIX
                            else char + key
                        }
                        MODE_DEC -> {   // decrypt
                            val temp = lowerCased - key // if pass a, count from z
                            if (temp < alphaRange.first) char - key + TWO_SIX
                            else char - key
                        }
                        else -> char
                    }
                }
            }
            ALG_UNICODE -> {    // shift characters +/- key value
                when (mode) {
                    MODE_ENC -> char + key
                    MODE_DEC -> char - key
                    else -> char
                }
            }
            else -> char
        }
    }

    // lambda returns string encrypted
    val encrypt = { s: String -> s.map { c -> logic(c) }.joinToString(EMPTY_STRING) }

    // check if data blank else assign infile value
    val inputData = data.ifBlank { inFile }

    when { // input is valid file format
        isValidFormat(inputData) -> {
            // create reader for in-file
            val reader = File("${defaultPath}$inputData")
            when {  // out-file is a valid file format
                isValidFormat(outFile) -> {
                    // create writer for out-file
                    val writer = File("${defaultPath}$outFile")
                    // create / overwrite empty file
                    writer.writeText(EMPTY_STRING)
                    // read from in-file & write to out-file
                    reader.useLines {
                        it.forEach {  line ->
                            writer.appendText("${encrypt(line)}\n")
                        }
                    }
                }   // out-file empty/incorrectly formatted, so print to screen encrypted
                else -> reader.forEachLine { println(encrypt(it)) }
            }
        }   // input data contains string data
        inputData.isNotBlank() -> {
            when {  // out-file is a valid file format
                isValidFormat(outFile) -> {
                    // create writer for file
                    val writer = File("${defaultPath}$outFile")
                    // write encrypted data to file
                    writer.writeText(encrypt(inputData))

                }   // out-file empty/incorrectly formatted, so print to screen encrypted
                else -> println(encrypt(inputData))
            }
        }   // input blank
        else -> {   // create / overwrite file if out-file is a valid file format
            if (isValidFormat(outFile)) File("${defaultPath}$outFile").writeText(inputData)
            else println(inputData)
        }
    }
}