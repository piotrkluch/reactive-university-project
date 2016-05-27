package services

import model.{Quotation, Quotations}

import scala.concurrent.Future

/**
  * @author alisowsk
  */
object QuotationService {

  def addQuotation(quotation: Quotation): Future[String] = {
    Quotations.add(quotation)
  }

  def deleteQuotation(id: Int): Future[Int] = {
    Quotations.delete(id)
  }

  def getQuotation(id: Int): Future[Option[Quotation]] = {
    Quotations.get(id)
  }

  def getByCompanyName(companyName: String): Future[Seq[Quotation]] = {
    Quotations.getByCompanyName(companyName)
  }

  def listAllQuotations: Future[Seq[Quotation]] = {
    Quotations.listAll
  }
}
