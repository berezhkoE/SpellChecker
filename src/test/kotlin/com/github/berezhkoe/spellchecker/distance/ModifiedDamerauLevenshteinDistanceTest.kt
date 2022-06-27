package com.github.berezhkoe.spellchecker.distance

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModifiedDamerauLevenshteinDistanceTest {
  private val distance = DamerauLevenshteinDistance(modified = false)
  private val modifiedDistance = DamerauLevenshteinDistance()

  @Test
  fun `test same`() {
    val source = "qwerty"
    val target = "qwerty"
    assertEquals(0.0, modifiedDistance.getDistance(source, target))
  }

  @Test
  fun `test replace of typo`() {
    val source = "john"
    val target = "johm"
    assertEquals(0.4, modifiedDistance.getDistance(source, target))
  }

  @Test
  fun `test replace of misspell`() {
    val source = "john"
    val target = "jahn"
    assertEquals(1.0, modifiedDistance.getDistance(source, target))
  }

  @Test
  fun `test replace`() {
    val source = "john"
    val target = "jodn"
    assertEquals(1.0, distance.getDistance(source, target))
    assertEquals(2.0, modifiedDistance.getDistance(source, target))
  }

  @Test
  fun `test replace soundex`() {
    val source = "sox"
    val target = "socks"
    assertEquals(3.0, distance.getDistance(source, target))
    assertEquals(2.4, modifiedDistance.getDistance(source, target))
  }
}
