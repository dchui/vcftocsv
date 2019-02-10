package com.dc.vcftocsv.model

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

class Email {

  @BeanProperty var address : String = _

  @BeanProperty var types : java.util.List[String] = _

  def isHome : Boolean = getTypes.asScala.contains("home")

  def isWork : Boolean = getTypes.asScala.contains("work")

  def isPreferred: Boolean = getTypes.asScala.contains("pref")
}
