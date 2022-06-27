# Spellchecker

Simple command line spell checker for English language.

## Usage

* Execute `./gradlew assembleDist` command.
* Unpack `build/distributions/SpellChecker-1.0-SNAPSHOT.zip`
* Run commands as follows: `sh .../SpellChecker-1.0-SNAPSHOT/bin/SpellChecker [-f <file_name>]`
* Spell Checker runs in two modes: 
  * `-f` option to check file
  * No arguments to check standard input
* Output format: `<line_number>:<word_number> : <word_to_correct> -> <suggestions>`
* Words written in camelCase, snake_case and kebab-case are treated as separate words:
```
  superSourcez
      1:2 : Sourcez -> [sources, source, sourced]
  super_sourcez
      2:2 : sourcez -> [sources, source, sourced]
  super-sourcez
      3:2 : sourcez -> [sources, source, sourced]
```
* Words with digits in it are not considered.

## Suggestion algorithm

To rank suggestions the modified Damerau-Levenshtein distance is used:
* The distance for insertion and deletion operation is same, that is 1. 
* For the substitution operation, the initial distance is 2, which equals one deletion and one insertion.
  To calculate the substitution distance, two letters are compared and checked whether both are in the same (phonetic or typographic) group or not. 
  If they are in the same group, the initial distance is then subtracted with each distance group.
* After collecting suggestions distances are modified using weighted words frequencies

## Sources

* [Damerau–Levenshtein distance, Wikipedia](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance)
* [Edit distance weighting modification using phonetic and typographic letter grouping over homomorphic encrypted data. 
Ahmad, T., Indrayana, K., Wibisono, W., & Ijtihadie, R. M. (2017). 2017 3rd ICSITech](https://ieeexplore.ieee.org/abstract/document/8257147)
* [SymSpell en dictionary](https://github.com/wolfgarbe/SymSpell/blob/master/SymSpell.FrequencyDictionary/en-80k.txt)
