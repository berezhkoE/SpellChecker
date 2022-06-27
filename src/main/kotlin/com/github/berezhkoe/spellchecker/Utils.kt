package com.github.berezhkoe.spellchecker

const val ansiReset = "\u001B[0m"
const val ansiGreen = "\u001B[32m"
const val ansiYellow = "\u001B[33m"

const val maxSuggestions = 3

const val maxDist = 2.0
const val initialSubDist = 2.0

const val soundexDist = 0.6
const val phonixDist = 0.2
const val editexDist = 0.2
const val qwertyDist = 0.6
const val freqDist = 0.4
