package com.github.berezhkoe.spellchecker

import com.github.berezhkoe.spellchecker.distance.DamerauLevenshteinDistance

class Suggester {
  private val maxSuggestions = 3

  private val maxDist = 2.0
  private val initialSubDist = 2.0

  private val soundexDist = 0.6
  private val phonixDist = 0.2
  private val editexDist = 0.2
  private val qwertyDist = 0.6
  private val freqDist = 0.4

  private val dictionaryRoot = Dictionary.trieRoot

  private val distance = DamerauLevenshteinDistance(initialSubDist, soundexDist, phonixDist, editexDist, qwertyDist)

  init {
    Dictionary.initDictionary()
  }

  fun collectSuggestions(word: String): List<Pair<String, Double>> {
    val searchResult = search(word)

    return searchResultsModifiedByFrequency(searchResult)
  }

  /**
   * Modify results' distances depending on words frequencies and then sort and take top [maxSuggestions]
   */
  private fun searchResultsModifiedByFrequency(searchResult: Map<Dictionary.TrieNode, Double>): List<Pair<String, Double>> {
    val frequencies = searchResult.map { (node, _) -> node.frequency }

    return searchResult.toList()
      .map { (node, dist) ->
        Pair(
          node.word!!,
          maxOf(dist - normalize(node.frequency, frequencies.max(), frequencies.min()), 0.0)
        )
      }
      .sortedBy { (_, dist) -> dist }
      .take(maxSuggestions)
  }

  /**
   * Collect suggestions using modified [DamerauLevenshteinDistance]
   */
  private fun search(word: String): Map<Dictionary.TrieNode, Double> {
    val result = mutableMapOf<Dictionary.TrieNode, Double>()
    val previousRow = distance.getFirstRow(word)

    for ((letter, node) in dictionaryRoot.children) {
      searchRec(node, letter, word, previousRow, result)
    }
    return result
  }

  private fun searchRec(
    node: Dictionary.TrieNode,
    letter: Char,
    word: String,
    previousRow: DoubleArray,
    results: MutableMap<Dictionary.TrieNode, Double>,
    prevLetter: Char? = null,
    prevPreviousRow: DoubleArray? = null,
    maxCost: Double = maxDist
  ) {
    val currentRow = distance.getRow(word, letter, previousRow, prevLetter, prevPreviousRow)

    if (currentRow.last() <= maxCost && node.word != null) {
      if (currentRow.last() == 0.0) {
        results.clear()
      }
      results[node] = currentRow.last()
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

  private fun normalize(value: Double, max: Double, min: Double): Double {
    if (max == min) {
      return value * freqDist
    }
    return (value - min) / (max - min) * freqDist
  }
}
