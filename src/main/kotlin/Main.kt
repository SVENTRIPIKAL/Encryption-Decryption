fun main() {
    println(encryptMessage("we found a treasure!"))
}

/**
 *  returns a new string with each
 *  character shifted from the back
 *  of the alphabet starting from z-a.
 *  Keeps punctuations & spaces unedited
 *  @param string string message to encrypt with cypher
 */
private fun encryptMessage(string: String): String {
    // create alphabet list
    val alphabet = mutableListOf<Char>()
    for (letter in 'a'..'z') alphabet += letter

    // loop through string & create lowercase output
    var output = ""
    for (char in string.lowercase()) {
        // get index of letter in alphabet
        val letterIndex = alphabet.indexOf(char)
        // update output
        output += when (letterIndex) {
            // keep non-letters unedited
            -1 -> char
            // shift letter with z-a letter shift
            else -> alphabet[alphabet.lastIndex - letterIndex]
        }
    }
    return output
}