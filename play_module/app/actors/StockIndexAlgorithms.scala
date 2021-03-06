package actors

import model.Quotation

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by peter on 22/06/16.
  */
object StockIndexAlgorithms {


  /** 1 - BEGIN CALCULATE MOVING AVERAGE (from date to date) **/

  /**
    *
    * @param companiesData
    * @param dateFrom
    * @param dateTo
    * @return
    */

  def calculateMovingAveragesOnActorSystem(companiesData: Future[Seq[Quotation]], dateFrom: java.util.Date, dateTo: java.util.Date) = {

    val fivemin = 5.minutes
    val listFromFuture: Seq[Quotation] =  Await.result(companiesData, fivemin)

    var selectedQuotations = new ListBuffer[Quotation]

    var closingDayPricesForLongerPeriodAmount : Double = 0.0

    listFromFuture.map(
      q => {
        if ( q.date.after(dateFrom) && q.date.before(dateTo) ) {

          selectedQuotations += q
          closingDayPricesForLongerPeriodAmount += q.closing.toDouble

        }
      })

    var listOfPrices = ListBuffer.empty[Double]

    selectedQuotations.map(
      q => listOfPrices += q.closing
    )

    this.calculateMovingAveragesForIndex(listOfPrices)

  }

  def calculateWeightedMovingAverage(closingDayPrices: ListBuffer[Double])={
    val sum: Double= closingDayPrices.sum
    sum/closingDayPrices.size
  }

  def calculateMovingAveragesForIndex(closingDayIndexPricesForLongerPeriod: ListBuffer[Double])={
    val closingDayPricesForLongerPeriodAmount = closingDayIndexPricesForLongerPeriod.size
    var middleSlot = closingDayPricesForLongerPeriodAmount/2
    var results = new ListBuffer[Double]()

    for( i <- 0 until middleSlot) {
      val listToBePassed = closingDayIndexPricesForLongerPeriod.slice(i, middleSlot)
      var resultToBeAppended = calculateWeightedMovingAverage(listToBePassed)
      results += resultToBeAppended
      if(middleSlot<=closingDayPricesForLongerPeriodAmount){
        middleSlot+=1
      }
    }
    results

  }

  /** 1 - END CALCULATE MOVING AVERAGE **/


  /** 2 - BEGIN CALCULATE EASE OF MOVEMENT (from date to date, from date to date) **/

  def calculateMaxValueOfCompanyForGivenPeriod(companiesData: Future[Seq[Quotation]], dateFrom: java.util.Date, dateTo: java.util.Date): Double = {

    val fivemin = 5.minute
    val listFromFuture: Seq[Quotation] =  Await.result(companiesData, fivemin)
    var selectedQuotations = new ListBuffer[Quotation]
    var maxValues = new ListBuffer[Double]

    listFromFuture.map(
      q => {
        if ( q.date.after(dateFrom) && q.date.before(dateTo) ) {

          selectedQuotations += q

        }
      })

    selectedQuotations.map(
      q => maxValues += q.max
    )

    maxValues.max

  }

  def calculateMinValueOfCompanyForGivenPeriod(companiesData: Future[Seq[Quotation]], dateFrom: java.util.Date, dateTo: java.util.Date): Double = {

    val fivemin = 5.minute
    val listFromFuture: Seq[Quotation] =  Await.result(companiesData, fivemin)
    var selectedQuotations = new ListBuffer[Quotation]
    var minValues = new ListBuffer[Double]

    listFromFuture.map(
      q => {
        if ( q.date.after(dateFrom) && q.date.before(dateTo) ) {

          selectedQuotations += q

        }
      })

    selectedQuotations.map(
      q => minValues += q.min
    )

    minValues.min
  }

  /**
    *
    * @param companiesData
    * @param pastDateFrom
    * @param pastDateTo
    * @param presentDateFrom
    * @param presentDateTo
    * @return
    */

  def calculateEaseOfMovement(companiesData: Future[Seq[Quotation]], pastDateFrom: java.util.Date, pastDateTo: java.util.Date, presentDateFrom: java.util.Date, presentDateTo: java.util.Date): Double ={

    val maxPresent = this.calculateMaxValueOfCompanyForGivenPeriod(companiesData, presentDateFrom, presentDateTo)
    val minPresent = this.calculateMinValueOfCompanyForGivenPeriod(companiesData, presentDateFrom, presentDateTo)

    val maxPast = this.calculateMaxValueOfCompanyForGivenPeriod(companiesData, pastDateFrom, pastDateTo)
    val minPast = this.calculateMinValueOfCompanyForGivenPeriod(companiesData, pastDateFrom, pastDateTo)

    return ( ( maxPresent+minPresent ) / 2 - ( maxPast + minPast ) / 2 ) / maxPresent - minPresent

  }

  /** 2 - END CALCULATE AVERAGE VALUE **/


  /** X - BEGIN CALCULATE AVERAGE VALUE (for single date (day granularity)) **/

  /**
    *
    * @param companiesData
    * @param date
    * @return
    */

  def calculateAverageValueForStockForGivenDay(companiesData: Future[Seq[Quotation]], date: java.util.Date) : Double = {
    var counter = 0;
    var stockValueForGivenDay: Double = 0;
    val fivemin = 5.minute
    val listFromFuture =  Await.result(companiesData, fivemin)

    listFromFuture.map(q=>if(q.date.equals(date)){
      stockValueForGivenDay+=q.closing
      counter+=1
    })

    var result : Double = stockValueForGivenDay/counter
    result
  }

  /** X - END CALCULATE AVERAGE VALUE **/


  /** 3 - BEGIN CALCULATE AVERAGE TRUE RANGE **/

  //TODO

  /** 3 - END CALCULATE AVERAGE TRUE RANGE **/

}
