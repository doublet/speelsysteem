package models.json

import java.time.LocalDate

import models.Volunteer
import play.api.libs.functional.syntax._
import play.api.libs.json._

object VolunteerJson {

  implicit val volunteerWrites: Writes[Volunteer] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and

      (JsPath \ "mobilePhone").writeNullable[String] and
      (JsPath \ "landline").writeNullable[String] and
      (JsPath \ "email").writeNullable[String] and

      (JsPath \ "street").writeNullable[String] and
      (JsPath \ "city").writeNullable[String] and

      (JsPath \ "bankAccount").writeNullable[String] and

      (JsPath \ "yearStartedVolunteering").writeNullable[Int] and
      (JsPath \ "isPartOfCore").write[Boolean] and
      (JsPath \ "birthDate").writeNullable[LocalDate]

    )(unlift(Volunteer.unapply))

  implicit val volunteerReads: Reads[Volunteer] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and

      (JsPath \ "mobilePhone").readNullable[String] and
      (JsPath \ "landline").readNullable[String] and
      (JsPath \ "email").readNullable[String] and

      (JsPath \ "street").readNullable[String] and
      (JsPath \ "city").readNullable[String] and

      (JsPath \ "bankAccount").readNullable[String] and

      (JsPath \ "yearStartedVolunteering").readNullable[Int] and
      (JsPath \ "isPartOfCore").read[Boolean] and
      (JsPath \ "birthDate").readNullable[LocalDate]

    )(Volunteer.apply _)
}