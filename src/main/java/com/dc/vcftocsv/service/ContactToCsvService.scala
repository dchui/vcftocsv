package com.dc.vcftocsv.service

import java.io.Writer

import com.dc.vcftocsv.model.Contact
import org.springframework.stereotype.Service
import org.supercsv.io.dozer.CsvDozerBeanWriter
import org.supercsv.prefs.CsvPreference

import scala.collection.JavaConverters._
import scala.util.Try

@Service
class ContactToCsvService {

  def write(contacts: List[Contact], writer: Writer): Try[Unit] = Try {
    val csvWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE)
    val csvMapping = buildFieldMappingForContact(contacts)

    csvWriter.writeHeader(csvMapping: _*)

    csvWriter.configureBeanMapping(classOf[Contact], csvMapping)
    contacts.foreach(c => csvWriter.write(c))
    csvWriter.flush()
  }

  private def buildFieldMappingForContact(contacts: List[Contact]) : Array[String] = {
    def getMaxNumberOfElements[A](f: Contact => List[A]): Int =
      Option(contacts.maxBy(c => Option(f(c)).map(_.size).getOrElse(0))).map(c => f(c).size).getOrElse(0)

    def createMappingForCollectionAttributes[A](f: Contact => List[A], f2: Int => List[String]): List[String] = {
      def appendToMapping(currentIndex: Int, currentMappings: List[String], maxElems: Int) : List[String] = {
        if (currentIndex < maxElems)
          appendToMapping(currentIndex + 1, currentMappings ++ f2(currentIndex), maxElems)
        else
          currentMappings
      }

      appendToMapping(0, List(), getMaxNumberOfElements(f))
    }

    (
      List("firstName", "lastName", "formattedName", "company") ++
        createMappingForCollectionAttributes(
          (c: Contact) => c.getOrganisations.asScala.toList,
          (m: Int) => {
            List(s"organisations[$m].name")
          }
        ) ++
        createMappingForCollectionAttributes(
          (c: Contact) => c.getTelephones.asScala.toList,
          (m: Int) => {
            List(s"telephones[$m].number", s"telephones[$m].preferred", s"telephones[$m].cell", s"telephones[$m].home", s"telephones[$m].work")
          }
        ) ++
        createMappingForCollectionAttributes(
          (c: Contact) => c.getEmails.asScala.toList,
          (m: Int) => {
            List(s"emails[$m].address", s"emails[$m].preferred", s"emails[$m].home", s"emails[$m].work")
          }
        ) ++
        createMappingForCollectionAttributes(
          (c: Contact) => c.getAddresses.asScala.toList,
          (m: Int) => {
            List(
              s"addresses[$m].streetAddress", s"addresses[$m].locality",
              s"addresses[$m].region", s"addresses[$m].postalCode", s"addresses[$m].postalCode",
              s"addresses[$m].preferred", s"addresses[$m].home", s"addresses[$m].work"
            )
          }
        )
    ).toArray
  }
}
