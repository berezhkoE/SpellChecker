package com.github.berezhkoe.spellchecker

import org.apache.commons.cli.*
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess


fun main(args: Array<String>) {
  val options: Options = generateOptions()
  val commandLine: CommandLine? = generateCommandLine(options, args)
  if (commandLine != null) {
    if (commandLine.hasOption("f")) {
      val optionValues = commandLine.getOptionValues("f")
      try {
        File(optionValues[0]).inputStream().use {
          SpellChecker().check(it)
        }
      } catch (exception: FileNotFoundException) {
        println(exception.message)
        exitProcess(-1)
      }
    } else if (commandLine.hasOption("h")) {
      printHelp(options)
    } else {
      SpellChecker().check(System.`in`)
    }
  } else {
    printHelp(options)
    exitProcess(-1)
  }
}

private fun generateOptions(): Options {
  val initOption = Option.builder("f")
    .desc("Check file with spellchecker.")
    .hasArg()
    .argName("file")
    .build()

  val helpOption = Option.builder("h")
    .longOpt("help")
    .build()

  val optionGroup = OptionGroup()
  optionGroup.addOption(initOption)
  optionGroup.addOption(helpOption)

  val options = Options()
  options.addOptionGroup(optionGroup)

  return options
}

private fun generateCommandLine(options: Options, args: Array<String>): CommandLine? {
  val commandLineParser: CommandLineParser = DefaultParser()
  var commandLine: CommandLine? = null
  try {
    commandLine = commandLineParser.parse(options, args)
    if (commandLine.args.isNotEmpty()) {
      throw ParseException("Unrecognized options: ${commandLine.args}")
    }
  } catch (parseException: ParseException) {
    println("SpellChecker: ${parseException.message}")
  }
  return commandLine
}

private fun printHelp(options: Options) {
  val helpFormatter = HelpFormatter()
  val footer = "If no file passed Spell Checker waits for stdin"
  helpFormatter.printHelp("SpellChecker", "", options, footer, true)
}
