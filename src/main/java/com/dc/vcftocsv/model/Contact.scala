package com.dc.vcftocsv.model

import scala.beans.BeanProperty

class Contact {

  @BeanProperty var firstName: String = _

  @BeanProperty var lastName: String = _

  @BeanProperty var company: Boolean = _

  @BeanProperty var organisations: java.util.List[Organisation] = _

  @BeanProperty var formattedName: String = _

  @BeanProperty var emails: java.util.List[Email] = _

  @BeanProperty var telephones : java.util.List[Telephone] = _

  @BeanProperty var addresses: java.util.List[Address] = _
}
