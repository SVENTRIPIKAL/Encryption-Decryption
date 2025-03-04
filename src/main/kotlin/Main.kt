import java.io.File

const val ASTERISK = "*"
const val ILLEGAL_INPUT = "IllegalArgument Error - for \"*\""
const val ARGS_NUMBER = "Number of Arguments"
const val ARGS_MISSING = "Missing Arguments"

fun main(args: Array<String>) {
    try {
        val arguments = parseCliArguments(args)
        parseEncryptDecrypt(
            mode = arguments["mode"]!!,
            key = arguments["key"]!!.toInt(),
            data = arguments["data"]!!,
            inFile = arguments["in"]!!,
            outFile = arguments["out"]!!
        )
    } catch (e: IllegalArgumentException) {
        println(e.localizedMessage)
    }
}

/**
 *  organizes & returns a map of arguments
 *  passed into the program for the following
 *  flags: -mode, -key, -data, -in, -out
 *  @param args string array storing program arguments
 *  @throws IllegalArgumentException
 */
private fun parseCliArguments(args: Array<String>): Map<String, String> {
    // check if array empty or contains blank arguments
    when {
        args.size !in 6..10 -> throw IllegalArgumentException(
            ILLEGAL_INPUT.replace(ASTERISK, ARGS_NUMBER)
        )
        args.contains("") -> throw IllegalArgumentException(
            ILLEGAL_INPUT.replace(ASTERISK, ARGS_MISSING)
        )
    }
    // map for flag & argument values
    val map = mutableMapOf(
        "mode" to "enc", "key" to "0", "data" to "", "in" to "", "out" to ""
    )
    // trim array arguments
    val array = args.map { it.trim() }.toTypedArray()

    // lambda flag check
    val isNotFlag = { f: String -> f !in listOf("-mode", "-key", "-data", "-in", "-out") }

    // lambda mode check
    val isValidMode = { m: String -> m in listOf("enc", "dec") }

    // lambda key check
    val isValidKey = { k: String -> Character.isDigit(k.first()) }

    // lambda return if true else
    val checkIfElse = { x: (String) -> Boolean, y: String, z: String -> if (x(y)) y else z }

    // organize arguments
    for (i in array.indices step 2) {
        // current & next indices
        val currentI = array[i]; val nextI = array[i.inc()]
        when (currentI) {
            "-mode" -> {    // check next index valid mode else default
                map["mode"] = checkIfElse(isValidMode, nextI, "enc")
            }
            "-key" -> {     // check next index valid key else default
                map["key"] = checkIfElse(isValidKey, nextI, "0")
            }
            "-data" -> {    // check next index not flag else default
                map["data"] = checkIfElse(isNotFlag, nextI, "")
            }
            "-in" -> {      // check next index not flag else default
                map["in"] = checkIfElse(isNotFlag, nextI, "")
            }
            "-out" -> {     // check next index not flag else default
                map["out"] = checkIfElse(isNotFlag, nextI, "")
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
 *  provided Mode & Key value
 *  @param mode string value deciding encryption/decryption
 *  @param key determines the number of Left/Right shifts
 *  @param data string message to encrypt/decrypt with key
 *  @param inFile file read-from path
 *  @param outFile file write-to path
 *  @throws NoSuchFileException
 */
private fun parseEncryptDecrypt(
    mode: String,
    key: Int,
    data: String,
    inFile: String,
    outFile: String
) {
    // default read/write path
    val defaultPath = System.getProperty("user.dir") +
            "${File.separator}.test${File.separator}"

    // regex capturing file + extension
    val fileFormatRegex = "^\\w+([.][A-Za-z0-9]{3,8})".toRegex()

    // lambda checking if string is valid file format
    val isValidFormat = { s: String -> s.matches(fileFormatRegex) }

    // lambda encrypt/decrypt by mode
    val logic = { char: Char ->
        when (mode) {
            "enc" -> char + key
            "dec" -> char - key
            else -> char
        }
    }
    // lambda returns string encrypted
    val encrypt = { s: String -> s.map { c -> logic(c) }.joinToString("") }

    // check if data blank else assign infile value
    val inputData = data.ifBlank { inFile }

    when {
        // input is valid file format
        isValidFormat(inputData) -> {
            // create reader for in-file
            val reader = File("${defaultPath}$inputData")
            when {  // out-file is a valid file format
                isValidFormat(outFile) -> {
                    // create writer for out-file
                    val writer = File("${defaultPath}$outFile")
                    // create empty file
                    writer.writeText("")
                    // read from in-file & write to out-file
                    reader.useLines {
                        it.forEach {  line ->
                            writer.appendText("${encrypt(line)}\n")
                        }
                    }
                }   // out-file empty/incorrectly formatted, so print to screen encrypted
                else -> reader.forEachLine { println(encrypt(it)) }
            }
        }
        // input data contains regular string data
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
        }
    }
}