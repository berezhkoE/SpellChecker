package com.github.berezhkoe.spellchecker.distance

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SubstitutionDistanceTest {
  private val initialSubDist = 2.0

  private val soundexDist = 0.6
  private val phonixDist = 0.2
  private val editexDist = 0.2
  private val qwertyDist = 0.6

  private val substitutionDistance = SubstitutionDistance(initialSubDist, soundexDist, phonixDist, editexDist, qwertyDist)

  @Test
  fun `test same`() {
    assertEquals(0.0, substitutionDistance.getDistance('q', 'q'))
  }

  @Test
  fun `test chars from different groups`() {
    assertEquals(initialSubDist, substitutionDistance.getDistance('q', 'm'))
  }

  @Test
  fun `test chars with all common groups`() {
    assertEquals(
      initialSubDist - soundexDist - phonixDist - editexDist - qwertyDist,
      substitutionDistance.getDistance('m', 'n')
    )
  }

  @Test
  fun `test misspelled chars`() {
    assertEquals(
      initialSubDist - soundexDist - phonixDist - editexDist,
      substitutionDistance.getDistance('o', 'a')
    )
  }

  @Test
  fun `test completely different chars`() {
    assertEquals(
      initialSubDist,
      substitutionDistance.getDistance('h', 'd')
    )
  }
}
