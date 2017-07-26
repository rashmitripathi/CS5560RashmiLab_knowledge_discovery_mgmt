package openie

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Rashmi
  */
object SparkOpenIE {

  def main(args: Array[String]) {
    // Configuration
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    val input = sc.textFile("data/triplets").map(line => {
      //val input = sc.textFile("d")input1.map(line => {
      //Getting OpenIE Form of the word using lda.CoreNLP
      println("Inside:")
      println("Line is:" + line)
      val t = CoreNLP.returnTriplets(line)
      println("Triplets are:")
      println(t)
    })

    input.collect().mkString("\n")
    //println(CoreNLP.returnTriplets("The dog has a lifespan of upto 10-12 years."))
    // println(input.collect().mkString("\n"))


  }

}
