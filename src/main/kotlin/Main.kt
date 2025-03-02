fun main() = println(encryptWithKey("welcome to hyperskill", 5))

/**
 *  returns a new string with each
 *  character shifted a number of times
 *  to the right according to the key value.
 *  Incrementing restarts at 'a' after 'z'.
 *  Keeps punctuations & spaces unedited
 *  @param string string message to encrypt with cypher
 *  @param key determines the number of Right shifts
 */
private fun encryptWithKey(string: String, key: Int): String {
    // create alphabet range
    val alphabetRange = ('a'..'z')
    // loop through string & create lowercase output
    var output = ""
    for (char in string.lowercase()) {
        // update output
        output += when {
            // keep non-letters unedited
            char !in alphabetRange -> char
            // add key shift & subtract 26 if greater than range
            else -> {
                var shifted = char + key
                if (shifted > alphabetRange.last) shifted -= 26
                shifted
            }
        }
    }
    return output
}