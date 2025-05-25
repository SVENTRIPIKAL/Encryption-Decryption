# Encryption, Decryption

A terminal program that parses & encrypts/decrypts data according to its \
provided algorithm, mode, & key arguments. The program is also able to \
read/write data to/from selected files within the default **.test** directory inside \
the program's parent directory. If in/out files are not specified, then the \
program prints the encrypted/decrypted results to the console. Flags \
are interchangeable across one another & their ordering does not matter.

### Options:
| FLAGS |        ARGUMENTS        | DEFAULTS |
|:------|:-----------------------:|:--------:|
| -alg  |     shift, unicode      |  shift   |
| -mode |        enc, dec         |   enc    |
| -key  |       <*number*>        |    0     |
| -data |    "<*string_data*>"    |    ""    |
| -in   | <*file_name.extension*> |    ""    |
| -out  | <*file_name.extension*> |    ""    |

### Examples:
```
-alg shift -mode enc -key 10 -data "Hello, World!"

Rovvy, Gybvn!
```
```
-key 10 -data "Rovvy, Gybvn!" -mode dec -alg shift

Hello, World!
```

### Note For Files:
Files to be encrypted/decrypted must be placed within a **.test** folder in the \
repository's parent directory to allow the program to discover them by default, \
else **defaultPath** in **parseEncryptDecrypt** function must point to the preferred directory.
```kotlin
val defaultPath = System.getProperty(USER_DIR) + "${File.separator}.test${File.separator}"
```

### Highlights
* Map
* File Read / Write
* Exception Handling
* Regular Expressions
* Lambda Expressions
* Character Sequences
* Encryption / Decryption
* Command Line Arguments

### Inspiration
[Encryption-Decryption](https://hyperskill.org/projects/279) (Kotlin) \
_Part of the [JetBrains Academy: Hyperskill - Kotlin Developer](https://hyperskill.org/courses/3-kotlin-developer) Course_
