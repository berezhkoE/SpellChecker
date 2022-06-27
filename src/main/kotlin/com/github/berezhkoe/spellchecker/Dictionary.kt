package com.github.berezhkoe.spellchecker

import java.io.InputStream

object Dictionary {
  val trieRoot = TrieNode()

  private var totalCount = 0L

  private fun insert(word: String, count: Long = 0L) {
    var node = trieRoot
    for (letter in word) {
      if (letter !in node.children) {
        node.children[letter] = TrieNode()
      }
      node = node.children[letter]!!
    }
    node.word = word
    node.count = count
  }

  fun initDictionary() {
    if (trieRoot.children.isEmpty()) {
      val dictInputStream: InputStream? = javaClass.classLoader.getResourceAsStream("en-80k.txt")
      dictInputStream?.bufferedReader()?.forEachLine { line ->
        line.split(" ", limit = 2).let { (word, count) ->
          insert(word, count.toLong())
          totalCount += count.toLong()
        }
      }
      if (trieRoot.children.isEmpty()) {
        throw SPException("Dictionary is empty or not found")
      }
    }
  }

  data class TrieNode(
    var word: String? = null,
    val children: MutableMap<Char, TrieNode> = mutableMapOf(),
    var count: Long = 0L
  ) {
    val frequency: Double
      get() = count.toDouble() / totalCount
  }
}
