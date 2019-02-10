package com.dc.vcftocsv.cli

import org.apache.commons.cli.{CommandLine, DefaultParser, HelpFormatter, Option, Options}
import org.springframework.stereotype.Component

import scala.util.Try

@Component
class CommandLineParser {

  def getOptions : Options =
    new Options().addOption(
      Option.builder("i").longOpt("input").required().hasArg(true).numberOfArgs(1).build()
    ).addOption(
      Option.builder("o").longOpt("output").hasArg(true).numberOfArgs(1).build()
    )

  def printHelp(message: String) : Unit = {
    new HelpFormatter().printHelp(message, getOptions)
  }

  def getCommandLine(args: Array[String]) : Try[CommandLine] =
    Try(new DefaultParser().parse(getOptions, args))
}
