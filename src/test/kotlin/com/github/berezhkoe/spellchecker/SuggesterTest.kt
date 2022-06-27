package com.github.berezhkoe.spellchecker

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class SuggesterTest {
  private val suggester = Suggester()

  @Test
  fun `test correct word`() {
    val searchResult = suggester.collectSuggestions("qwerty")
    assert(searchResult.size == 1)
    assert(searchResult.first().first == "qwerty")
    assert(searchResult.first().second == 0.0)
  }

  @Test
  fun `test suggestions`() {
    val searchResultS = suggester.collectSuggestions("sourcez").map { (word, _) -> word }
    assertContentEquals(searchResultS, listOf("sources", "source", "sourced"))

    val searchResultB = suggester.collectSuggestions("builts").map { (word, _) -> word }
    assertContentEquals(searchResultB, listOf("built", "builds", "bolts"))
  }
}
