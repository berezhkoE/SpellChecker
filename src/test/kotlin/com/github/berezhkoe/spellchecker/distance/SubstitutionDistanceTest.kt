package com.github.berezhkoe.spellchecker.distance

import com.github.berezhkoe.spellchecker.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SubstitutionDistanceTest {

  @Test
  fun `test same`() {
    assertEquals(0.0, SubstitutionDistance('q', 'q').getDistance())
  }

  @Test
  fun `test chars from different groups`() {
    assertEquals(initialSubDist, SubstitutionDistance('q', 'm').getDistance())
  }

  @Test
  fun `test chars with all common groups`() {
    assertEquals(
      initialSubDist - soundexDist - phonixDist - editexDist - qwertyDist,
      SubstitutionDistance('m', 'n').getDistance()
    )
  }

  @Test
  fun `test misspelled chars`() {
    assertEquals(
      initialSubDist - soundexDist - phonixDist - editexDist,
      SubstitutionDistance('o', 'a').getDistance()
    )
  }

  @Test
  fun `test completely different chars`() {
    assertEquals(
      initialSubDist,
      SubstitutionDistance('h', 'd').getDistance()
    )
  }
}
