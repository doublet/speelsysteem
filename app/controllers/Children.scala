package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.db.slick._
import scala.slick.driver.H2Driver.simple._

import views._
import models._
import models.{Children => ChildrenModel}

object Children extends Controller {

  val childForm = Form(
    mapping(
      "id" -> optional(of[Long]),
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "mobilePhone" -> optional(text),
      "landline" -> optional(text),
      
      "street" -> optional(text),
      "city" -> optional(text),
      
      "birthDate" -> optional(date("dd-MM-yyyy")),
      "medicalRecordGood" -> boolean,
      "medicalRecordChecked" -> optional(date("dd-MM-yyyy"))
    )(Child.apply)(Child.unapply)
  )
  
  
  def showList = DBAction { implicit rs => Ok(html.child_list.render(ChildrenModel.findAll, rs.flash)) }
  
  def newChild = Action { implicit rs => Ok(html.child_form.render(childForm, rs.flash)) }
  
  def saveChild = DBAction { implicit rs =>
    childForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.child_form.render(formWithErrors, rs.flash)),
      child => {
        child.id match{
          case Some(x) => {
              ChildrenModel.update(child)
              Redirect(routes.Children.showList).flashing("success" -> "Kind upgedated")
            }
          case _ =>  {
              ChildrenModel.insert(child)
              Redirect(routes.Children.showList).flashing("success" -> "Kind toegevoegd")
            }
        }
      }
    )
    
  }
  
  def editChild(id: Long) = DBAction { implicit rs =>
    val child = ChildrenModel.findById(id)
    child match{
      case Some(ch) => Ok(html.child_form.render(childForm.fill(ch), rs.flash))
      case _ => BadRequest("Geen geldige id")
    }
  }
  
  def details(id: Long) = DBAction { implicit rs => 
    val child = ChildrenModel.findById(id)
    child match {
      case Some(x) => Ok(html.child_details(x))
      case None => BadRequest("Geen kind met die ID")
    }
  }
}