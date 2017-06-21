import java.nio.file.{Files, Paths}

import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by Rashmi on 6/18/2017.
  */
object SparkGroupByFirstLetter {

  def main(args: Array[String]) {

    System.setProperty("hadoop.home.dir", "E:\\winutils");
    val sparkConf = new SparkConf().setAppName("SparkTransformation").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)
    val lemma: NLPInputFIle =new NLPInputFIle
    val text = new String(Files.readAllBytes(Paths.get("E:\\KDM\\Lab 2\\CS5560-Tutorial2B-SparkTransformationActions\\SparkTransformationActions\\input.txt")))
    val output = lemma.getLemmatizedorNERWord(text,"lemma","")

    val x = sc.parallelize(Array(output))
    val y = x.flatMap(line=>{line.split(" ")}).groupBy(word=> word.charAt(0)).cache()
    y.saveAsTextFile("output")
    //y.collect().foreach(println)
  }

}
