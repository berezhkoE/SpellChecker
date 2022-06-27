package com.github.berezhkoe.spellchecker.distance

import org.jetbrains.annotations.TestOnly

/**
 * Calculates Damerau-Levenshtein distance.
 * Modified distance takes into consideration typographic and phonetic distances
 */
class DamerauLevenshteinDistance(
  initialSubDist: Double = 2.0,
  soundexDist: Double = 0.6,
  phonixDist: Double = 0.2,
  editexDist: Double = 0.2,
  qwertyDist: Double = 0.6,
  private val modified: Boolean = true
) {

  private val substitutionDistance: SubstitutionDistance = SubstitutionDistance(
    initialSubDist,
    soundexDist,
    phonixDist,
    editexDist,
    qwertyDist
  )

  @TestOnly
  fun getDistance(source: String, target: String): Double {
    val sourceLength = source.length
    val targetLength = target.length

    if (sourceLength == 0) {
      return targetLength.toDouble()
    }
    if (targetLength == 0) {
      return sourceLength.toDouble()
    }

    val dist = Array(targetLength + 1) { DoubleArray(sourceLength + 1) }
    dist[0] = getFirstRow(source)

    for (i in 1..targetLength) {
      dist[i] = if (i > 1) {
        getRow(source, target[i - 1], dist[i - 1], target[i - 2], dist[i - 2])
      } else {
        getRow(source, target[0], dist[0])
      }
    }
    return dist[targetLength][sourceLength]
  }

  fun getFirstRow(word: String): DoubleArray {
    return DoubleArray(word.length + 1) { it.toDouble() }
  }

  fun getRow(
    word: String,
    letter: Char,
    previousRow: DoubleArray,
    prevLetter: Char? = null,
    prevPreviousRow: DoubleArray? = null
  ): DoubleArray {
    val columns = word.length + 1
    val currentRow = DoubleArray(columns)
    currentRow[0] = previousRow[0] + 1

    for (column in 1 until columns) {
      val cost = if (word[column - 1] == letter) 0 else 1
      val subCost = if (modified) substitutionDistance.getDistance(word[column - 1], letter) else cost.toDouble()
      currentRow[column] = minOf(
        previousRow[column] + 1, // deletion
        currentRow[column - 1] + 1, // insertion
        previousRow[column - 1] + subCost // substitution
      )
      if (prevPreviousRow != null && column > 1 && word[column - 1] == prevLetter && word[column - 2] == letter) {
        currentRow[column] = minOf(
          currentRow[column],
          prevPreviousRow[column - 2] + 1 // transposition
        )
      }
    }
    return currentRow
  }
}
