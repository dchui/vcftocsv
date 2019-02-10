package com.dc.vcftocsv.cli

import java.io._

import com.dc.vcftocsv.service.{ContactToCsvService, VcfReadService}
import org.apache.commons.cli.{CommandLine, ParseException}
import org.springframework.stereotype.Component

import scala.util.{Failure, Success, Try}

@Component
class VcfToCsv (
                              private val vcfReadService: VcfReadService,
                              private val contactToCsvService: ContactToCsvService,
                              private val commandLineParser: CommandLineParser
                            ) {

  @throws(classOf[IOException])
  def run(args: Array[String]): Unit =
    commandLineParser.getCommandLine(args).map(
      c => {
        (getInputStream(c), getOutputStream(c)) match {
          case (Success(i), Success(o)) =>
            vcfReadService.readVcf(i).map(contacts => contactToCsvService.write(contacts, o))
            Try(i.close())
            Try(o.close())
          case (Success(i), Failure(error)) =>
            Try(i.close())
            throw error
          case (Failure(error), Success(o)) =>
            Try(o.close())
            throw error
          case (_, _) =>
            throw new IOException("Failed to create both input and output stream.")
        }
      }
    ) match {
      case Failure(error: ParseException) =>
        commandLineParser.printHelp(error.getMessage)
      case Failure(error) =>
        throw error
      case Success(_) => {}
    }

  private def getOutputStream(cmdLine: CommandLine): Try[Writer] = {
    Option(cmdLine.getOptionValue("o")).filter(_ != "-").map(
      p => Try(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(p)), "UTF-8"))
    ).getOrElse(Try(new OutputStreamWriter(System.out)))
  }

  private def getInputStream(cmdLine: CommandLine): Try[InputStream] =
    Try(new BufferedInputStream(new FileInputStream(cmdLine.getOptionValue("i"))))
}
