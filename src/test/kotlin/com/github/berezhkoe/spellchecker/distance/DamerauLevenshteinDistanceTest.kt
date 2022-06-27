package com.github.berezhkoe.spellchecker.distance

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DamerauLevenshteinDistanceTest {
  private val damerauLevenshteinDistance = DamerauLevenshteinDistance(modified = false)
  @Test
  fun `test same`() {
    val source = "qwerty"
    val target = "qwerty"
    assertEquals(0.0, damerauLevenshteinDistance.getDistance(source, target))
  }

  @Test
  fun `test another`() {
    val source = "ag qwe"
    val target = "a wqe"
    assertEquals(2.0, damerauLevenshteinDistance.getDistance(source, target))
  }

  @Test
  fun `test simple replace`() {
    val source = "john"
    val target = "johm"
    assertEquals(1.0, damerauLevenshteinDistance.getDistance(source, target))
  }
}
