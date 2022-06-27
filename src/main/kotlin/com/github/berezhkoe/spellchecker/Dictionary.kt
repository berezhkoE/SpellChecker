package com.github.berezhkoe.spellchecker

import java.io.InputStream

object Dictionary {
  private val dictInputStream: InputStream? = javaClass.classLoader.getResourceAsStream("en-80k.txt")

  val trieRoot = TrieNode()
  var frequencyDict: Map<String, Double>? = null

  fun insert(word: String) {
    var node = trieRoot
    for (letter in word) {
      if (letter !in node.children) {
        node.children[letter] = TrieNode()
      }
      node = node.children[letter]!!
    }
    node.word = word
  }

  fun initDictionary() {
    if (trieRoot.children.isEmpty()) {
      var totalCount = 0L
      val words = mutableMapOf<String, Long>()

      dictInputStream?.bufferedReader()?.forEachLine { line ->
        line.split(" ", limit = 2).let { (word, count) ->
          insert(word)
          totalCount += count.toLong()
          words[word] = count.toLong()
        }
      }
      if (totalCount != 0L) {
        frequencyDict = words.mapValues { (_, count) -> count.toDouble() / totalCount }
      }

      if (trieRoot.children.isEmpty()) {
        throw SPException("Dictionary is empty or not found")
      }
    }
  }
}

data class TrieNode(var word: String? = null, val children: MutableMap<Char, TrieNode> = mutableMapOf())
