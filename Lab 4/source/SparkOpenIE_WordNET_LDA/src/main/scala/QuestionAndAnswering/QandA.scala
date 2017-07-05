package QuestionAndAnswering

/**
  * Created by Rashmi on 7/4/2017.
  */

import openie.CoreNLP
import org.apache.spark.{SparkConf, SparkContext}

object QandA {

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "E:\\winutils")
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)


    for (index <- 0 to 3) {
      println("Enter your question:")
      val ques = scala.io.StdIn.readLine().toLowerCase
      calculateAnswer(sc,ques);

    }
  }

  def calculateAnswer(sc:SparkContext,ques:String)
  {
    println("answer through openIE :")

    val input = sc.textFile("data/doc.txt").map(line => {
      //println("Line is:" + line)
      val t = CoreNLP.calculateAnswer(line,ques)
      if(  ! t.equals(""))
      {
        println("Question is : "+ques+" \n Final Answer is:"+t)
      }
    })

    input.collect().mkString("\n")
    println(input.collect().mkString("\n"))
  }

}
