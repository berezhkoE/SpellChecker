package com.github.berezhkoe.spellchecker

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class SuggestionsTest {
  private val spellChecker = SpellChecker()

  @Test
  fun `test correct word`() {
    val searchResult = spellChecker.collectSuggestions("qwerty")
    assert(searchResult.size == 1)
    assert(searchResult.first().first == "qwerty")
    assert(searchResult.first().second == 0.0)
  }

  @Test
  fun `test suggestions`() {
    val searchResultS = spellChecker.collectSuggestions("sourcez").map { (word, _) -> word }
    assertContentEquals(searchResultS, listOf("sources", "source", "sourced"))

    val searchResultB = spellChecker.collectSuggestions("builts").map { (word, _) -> word }
    assertContentEquals(searchResultB, listOf("built", "builds", "bolts"))
  }
}
