package com.github.berezhkoe.spellchecker

import com.github.berezhkoe.spellchecker.distance.DamerauLevenshteinDistance
import java.io.InputStream

class SpellChecker {
  private val dictionaryRoot = Dictionary.trieRoot
  private val frequencyDict: Map<String, Double>

  private val distance = DamerauLevenshteinDistance()

  /**
   * Split line by punctuation, underscore, whitespace characters and capital letters
   */
  private val splitRegex = Regex("[\\W_]+|(?=[A-Z])")

  init {
    Dictionary.initDictionary()
    frequencyDict = Dictionary.frequencyDict!!
  }

  fun check(input: InputStream) {
    var count = 0

    var anySuggestions = false
    input.bufferedReader().forEachLine { line ->
      count++

      if (line.isNotEmpty()) {
        getWords(line).forEachIndexed { index, word ->
          var result: List<Pair<String, Double>>
          word.lowercase().let { lowercaseWord ->
            result = collectSuggestions(lowercaseWord)
          }

          if (result.isNotEmpty() && result.first().second > 0.0) {
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

  fun collectSuggestions(word: String): List<Pair<String, Double>> {
    val searchResult = search(word)

    if (searchResult.size > 1) {
      return searchResultsModifiedByFrequency(searchResult)
    }
    return searchResult.toList()
  }

  /**
   * Modify results' distances depending on words frequencies
   */
  private fun searchResultsModifiedByFrequency(searchResult: Map<String, Double>): List<Pair<String, Double>> {
    val frequencies = searchResult.map { (word, _) -> frequencyDict[word]!!.toDouble() }

    return searchResult.toList()
      .map { (word, dist) ->
        Pair(
          word,
          dist - normalize(frequencyDict.getOrDefault(word, 0.0), frequencies.max(), frequencies.min())
        )
      }
      .sortedBy { (_, dist) -> dist }
      .take(maxSuggestions)
  }

  /**
   * Collect suggestions using modified [DamerauLevenshteinDistance]
   */
  private fun search(word: String): Map<String, Double> {
    val result = mutableMapOf<String, Double>()
    val previousRow = distance.getFirstRow(word)

    for ((letter, node) in dictionaryRoot.children) {
      searchRec(node, letter, word, previousRow, result)
    }
    return result
  }

  private fun searchRec(
    node: TrieNode,
    letter: Char,
    word: String,
    previousRow: DoubleArray,
    results: MutableMap<String, Double>,
    prevLetter: Char? = null,
    prevPreviousRow: DoubleArray? = null,
    maxCost: Double = maxDist
  ) {
    val currentRow = distance.getRow(word, letter, previousRow, prevLetter, prevPreviousRow)

    if (currentRow.last() <= maxCost && node.word != null) {
      if (currentRow.last() == 0.0) {
        results.clear()
      }
      results[node.word!!] = currentRow.last()
    }

    if (currentRow.min() <= maxCost && currentRow.last() > 0) {
      for ((l, child) in node.children) {
        if (results.size == 1 && results.containsValue(0.0)) {
          break
        }
        searchRec(child, l, word, currentRow, results, letter, previousRow, maxCost)
      }
    }
  }

  private fun printSuggestions(word: String, suggestions: Set<String>, index: Int, line: Int) {
    println("\t$line:${index + 1} : $ansiYellow$word$ansiReset -> $ansiGreen$suggestions$ansiReset")
  }

  private fun normalize(value: Double, max: Double, min: Double): Double {
    return (value - min) / (max - min) * freqDist
  }
}
