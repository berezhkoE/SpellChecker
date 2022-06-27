package com.github.berezhkoe.spellchecker

import java.io.InputStream

class SpellChecker {
  private val ansiReset = "\u001B[0m"
  private val ansiGreen = "\u001B[32m"
  private val ansiYellow = "\u001B[33m"

  private val suggester = Suggester()

  /**
   * Split line by punctuation, underscore, whitespace characters and capital letters
   */
  private val splitRegex = Regex("[\\W_]+|(?=[A-Z])")

  fun check(input: InputStream) {
    var count = 0

    var anySuggestions = false
    input.bufferedReader().forEachLine { line ->
      count++

      if (line.isNotEmpty()) {
        getWords(line).forEachIndexed { index, word ->
          var result: List<Pair<String, Double>>
          word.lowercase().let { lowercaseWord ->
            result = suggester.collectSuggestions(lowercaseWord)
          }

          if (result.isNotEmpty() && result.first().second != 0.0) {
            if (!anySuggestions) {
              println("Suggestions:")
              anySuggestions = true
            }
            printSuggestions(word, result.toMap().keys, index, count)
          }
        }
      }
    }
    if (!anySuggestions) {
      println("${ansiGreen}Everything is correct!$ansiReset")
    }
  }

  fun getWords(line: String): List<String> {
    return line.trim().split(splitRegex).filter { it.isNotEmpty() && it.all { c -> c.isLetter() }}
  }

  private fun printSuggestions(word: String, suggestions: Set<String>, index: Int, line: Int) {
    println("\t$line:${index + 1} : $ansiYellow$word$ansiReset -> $ansiGreen$suggestions$ansiReset")
  }
}
