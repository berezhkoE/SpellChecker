package com.github.berezhkoe.spellchecker

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class LineSplitTest {
  private val spellChecker = SpellChecker()

  @Test
  fun `test whitespaces split`() {
    val content = "test simple    split   "
    val expected = listOf("test", "simple", "split")
    assertContentEquals(expected, spellChecker.getWords(content))
  }

  @Test
  fun `test camelCase`() {
    val content = " Test   camelCase"
    val expected = listOf("Test", "camel", "Case")
    assertContentEquals(expected, spellChecker.getWords(content))
  }

  @Test
  fun `test snake_case`() {
    val content = " _test   snake_case "
    val expected = listOf("test", "snake", "case")
    assertContentEquals(expected, spellChecker.getWords(content))
  }

  @Test
  fun `test kebab-case`() {
    val content = " -test   kebab-case  - "
    val expected = listOf("test", "kebab", "case")
    assertContentEquals(expected, spellChecker.getWords(content))
  }

  @Test
  fun `test non-letter characters split`() {
    val content = "Test some sentence;  it has some%% weird: @punctuation!? st0p"
    val expected = listOf("Test", "some", "sentence", "it", "has", "some", "weird", "punctuation")
    println(spellChecker.getWords(content))
    assertContentEquals(expected, spellChecker.getWords(content))
  }
}
