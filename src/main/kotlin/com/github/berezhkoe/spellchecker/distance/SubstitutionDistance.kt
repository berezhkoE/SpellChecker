package com.github.berezhkoe.spellchecker.distance

/**
 * Modified substitution cost that considers typographic and phonetic distances
 */
class SubstitutionDistance(
  private val initialSubDist: Double,
  soundexDist: Double,
  phonixDist: Double,
  editexDist: Double,
  qwertyDist: Double
) {
  private val soundexGroups: List<HashSet<Char>> = listOf(
    hashSetOf('a', 'e', 'i', 'o', 'u', 'y', 'h', 'w'),
    hashSetOf('b', 'p', 'f', 'v'),
    hashSetOf('c', 'g', 'j', 'k', 'q', 's', 'x', 'z'),
    hashSetOf('d', 't'),
    hashSetOf('l'),
    hashSetOf('m', 'n'),
    hashSetOf('r')
  )

  private val phonixGroups: List<HashSet<Char>> = listOf(
    hashSetOf('a', 'e', 'i', 'o', 'u', 'y', 'h', 'w'),
    hashSetOf('b', 'p'),
    hashSetOf('c', 'g', 'j', 'k', 'q'),
    hashSetOf('d', 't'),
    hashSetOf('l'),
    hashSetOf('m', 'n'),
    hashSetOf('r'),
    hashSetOf('f', 'v'),
    hashSetOf('s', 'x', 'z')
  )

  private val editexGroups: List<HashSet<Char>> = listOf(
    hashSetOf('a', 'e', 'i', 'o', 'u', 'y'),
    hashSetOf('b', 'p'),
    hashSetOf('c', 'k', 'q'),
    hashSetOf('d', 't'),
    hashSetOf('l', 'r'),
    hashSetOf('m', 'n'),
    hashSetOf('g', 'j'),
    hashSetOf('f', 'p', 'v'),
    hashSetOf('s', 'x', 'z'),
    hashSetOf('c', 's', 'z')
  )

  private val qwertyGroups: List<HashSet<Char>> = listOf(
    hashSetOf('q', 'a', 'w'),
    hashSetOf('w', 's', 'e'),
    hashSetOf('e', 'd', 'r'),
    hashSetOf('r', 'f', 't'),
    hashSetOf('t', 'g', 'y'),
    hashSetOf('y', 'h', 'u'),
    hashSetOf('u', 'j', 'i'),
    hashSetOf('i', 'k', 'o'),
    hashSetOf('o', 'l', 'p'),
    hashSetOf('a', 'z', 's'),
    hashSetOf('s', 'x', 'd'),
    hashSetOf('d', 'c', 'f'),
    hashSetOf('f', 'v', 'g'),
    hashSetOf('g', 'b', 'h'),
    hashSetOf('h', 'n', 'i'),
    hashSetOf('j', 'm', 'k'),
    hashSetOf('k', 'l'),
    hashSetOf('z', 'x'),
    hashSetOf('x', 'c'),
    hashSetOf('c', 'v'),
    hashSetOf('v', 'b'),
    hashSetOf('b', 'n'),
    hashSetOf('n', 'm')
  )

  private val groupings = mapOf(
    soundexGroups to soundexDist,
    phonixGroups to phonixDist,
    editexGroups to editexDist,
    qwertyGroups to qwertyDist
  )

  fun getDistance(first: Char, second: Char): Double {
    if (first == second) {
      return 0.0
    }

    return groupings.entries.fold(initialSubDist) { acc, (groups, dist) -> acc.resolveGrouping(groups, dist, first, second) }
  }

  private fun Double.resolveGrouping(groups: List<HashSet<Char>>, dist: Double, first: Char, second: Char): Double {
    for (group in groups) {
      if (first in group && second in group) {
        return this - dist
      }
    }
    return this
  }
}
