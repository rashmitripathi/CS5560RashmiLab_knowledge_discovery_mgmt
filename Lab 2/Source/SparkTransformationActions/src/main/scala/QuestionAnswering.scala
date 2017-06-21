import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by Rashmi on 6/20/2017.
  */
object QuestionAnswering {

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "E:\\winutils");
    val sparkConf = new SparkConf().setAppName("SparkTransformation").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    val nlp: NLPInputFIle = new NLPInputFIle();

    // dataset collected from BBC site
    val docText = sc.textFile("E:\\KDM\\Lab 2\\CS5560-Tutorial2B-SparkTransformationActions\\SparkTransformationActions\\input.txt");
    for (index <- 0 to 3) {
      println("Enter your question:")
      val ques = scala.io.StdIn.readLine()
      var qtype = ""
      if (ques.contains("who")){
        qtype = "PERSON"
      }
      if (ques.contains("where")){
        qtype = "LOCATION"
      }
      if (ques.contains("when")){
        qtype = "DATE"
      }

      val answer = docText.map(line => {
        nlp.getLemmatizedorNERWord(line,"ner" ,qtype)
      })
      showAnswer(answer)
    }

  }
  def showAnswer(value: RDD[String])
  {
    println("All possible answers are: ")
      val ans=value.flatMap(s=>{s.split(" ")}).map(w=>(w)).filter(f=>{f.length>2}).distinct()
      ans.take(10).foreach(println)

  }
}

