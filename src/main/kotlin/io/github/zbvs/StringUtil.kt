package io.github.zbvs





object StringUtil {
    fun toUpper(inputValue: String): String {
        val convertedString = inputValue.uppercase()
        println("toUpperCase: from $inputValue to $convertedString")
        return convertedString
    }

    fun toLower(inputValue: String): String {
        val convertedString = inputValue.lowercase()
        println("toLowerCase: from $inputValue to $convertedString")
        return convertedString
    }
}
