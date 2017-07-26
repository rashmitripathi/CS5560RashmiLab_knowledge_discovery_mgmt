package openie

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 27-Jun-16.
  */
object SparkOpenIE {

  def main(args: Array[String]) {
    // Configuration
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    // Turn off Info Logger for Console
    //    Logger.getLogger("org").setLevel(Level.ALL)
    //  Logger.getLogger("akka").setLevel(Level.ALL)
    //val input1=sc.textFile("data/doc")

    //print("helloooooooooooo",input1)
    val input = sc.textFile("data/doc.txt").map(line => {
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
