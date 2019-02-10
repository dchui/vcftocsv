package com.dc.vcftocsv.service

import java.io.InputStream

import com.dc.vcftocsv.model._
import ezvcard.{Ezvcard, VCard}
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.util.Try

@Service
class VcfReadService {

  def readVcf(in: InputStream): Try[List[Contact]] = Try {
    Ezvcard.parse(in).all().asScala.map(
      v => {
        val c = new Contact

        c.setCompany(Option(v.getExtendedProperty("X-ABShowAs")).exists(_.getValue == "COMPANY"))

        setContactFirstAndLastNames(v, c)
        setContactOrganisations(v, c)
        setContactFormattedName(c)
        setContactEmails(v, c)
        setContactTelephones(v, c)
        setContactAddresses(v, c)

        c
      }
    ).toList
  }

  private def setContactAddresses(v: VCard, c: Contact): Unit = {
    c.setAddresses(
      Option(v.getAddresses).map(
        _.asScala.map(
          a => {
            val addr = new Address

            addr.setStreetAddress(a.getStreetAddress)
            addr.setLocality(a.getLocality)
            addr.setRegion(a.getRegion)
            addr.setPostalCode(a.getPostalCode)
            addr.setCountry(a.getCountry)

            addr.setTypes(Option(a.getTypes).map(_.asScala.map(_.toString).toList.asJava).orNull)

            addr
          }
        ).toList.asJava
      ).orNull
    )
  }

  private def setContactTelephones(v: VCard, c: Contact): Unit = {
    c.setTelephones(
      Option(v.getTelephoneNumbers).map(
        _.asScala.map(
          t => {
            val tel = new Telephone
            tel.setTypes(Option(t.getTypes).map(_.asScala.map(_.toString).toList.asJava).orNull)
            tel.setNumber(t.getText)
            tel
          }
        ).toList.asJava
      ).orNull
    )
  }

  private def setContactEmails(v: VCard, c: Contact): Unit = {
    c.setEmails(
      Option(v.getEmails).map(
        _.asScala.map(
          e => {
            val eml = new Email
            eml.setTypes(Option(e.getTypes).map(_.asScala.map(_.toString).toList.asJava).orNull)
            eml.setAddress(e.getValue)
            eml
          }
        ).toList.asJava
      ).orNull
    )
  }

  private def setContactFormattedName(c: Contact): Unit = {
    c.setFormattedName(
      List(Option(c.getFirstName), Option(c.getLastName), c.getOrganisations.headOption.map(_.getName))
        .filter(_.isDefined)
        .map(_.get)
        .take(2)
        .mkString(" ")
    )
  }

  private def setContactOrganisations(v: VCard, c: Contact): Unit = {
    c.setOrganisations(
      Option(v.getOrganizations).map(
        _.asScala.map(
          org => {
            val o = new Organisation
            o.setName(org.getValues.mkString(" "))
            o
          }
        ).toList.asJava
      ).orNull
    )
  }

  private def setContactFirstAndLastNames(v: VCard, c: Contact): Unit = {
    Option(v.getStructuredName).foreach(sn => {
      c.setFirstName(sn.getGiven)
      c.setLastName(sn.getFamily)
    })
  }
}
