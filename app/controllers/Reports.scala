package controllers

import java.io._
import java.time.LocalDate
import javax.inject.Inject

import helpers.DateTime._
import helpers.FiscalCertificateBuilder.{FiscalCertificateAttendances, FiscalCertificateResponsible, FiscalCertificateInformation}
import helpers.{FiscalCertificateBuilder, ReportBuilder}
import models.dao.{ChildDao, AnimatorDao}
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel._
import play.api.mvc._
import models.Child

import scala.concurrent.ExecutionContext.Implicits.global


class Reports @Inject()(animatorDao: AnimatorDao, childDao: ChildDao)
  extends Controller
{

  def personalInfo: Action[AnyContent] = Action.async {
    for {
      children <- childDao.findAll
      volunteers <- animatorDao.findAll
    } yield {
      val today = LocalDate.now
      val tmpFile: File = File.createTempFile(s"${today.format(fmt)} Export children and volunteers", ".xls.tmp")

      val wb: Workbook = new HSSFWorkbook

      ReportBuilder.addFrontPage(today, wb)
      ReportBuilder.addVolunteerSheet(volunteers.sortBy(_.lastName), wb)
      ReportBuilder.addChildrenSheet(children.sortBy(_.lastName), wb)

      val fileOut: FileOutputStream = new FileOutputStream(tmpFile)
      wb write fileOut
      fileOut close()

      Ok.sendFile(tmpFile, fileName = _ => s"${today.format(fmt)} Export kinderen en vrijwilligers.xls")
    }
  }

  def fiscalCertificate(childId: Long): Action[AnyContent] = Action.async {
    childDao.findById(childId)map(_.fold(BadRequest("Child not found"))(child => {

      // Placeholders
      val certificateId = 101

      val attendancesVm = 10
      val attendancesMid = 15
      val attendancesNm = 27

      val fiscalInfo = FiscalCertificateInformation(
        FiscalCertificateResponsible("Example Example", "Street", "1234 City"),
        certificateId,
        child,
        "ne langentijd",
        FiscalCertificateAttendances(attendancesVm, attendancesMid, attendancesNm)
      )
      val result = FiscalCertificateBuilder.build(fiscalInfo)

      val today = LocalDate.now
      val tmpFile = File.createTempFile(s"${today.format(fmt)} Fiscal certificate for ${child.firstName} ${child.lastName}", ".doc.tmp")
      val fileOut: FileOutputStream = new FileOutputStream(tmpFile)
      result.write(fileOut)

      Ok.sendFile(tmpFile, fileName = _ => s"${today.format(fmt)} Fiscaal attest voor ${child.firstName} ${child.lastName}.doc")
    }))

  }
}
