
import java.io.File

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.feature.{HashingTF, IDF, Word2Vec, Word2VecModel}
import org.apache.spark.rdd.RDD

import scala.collection.immutable.HashMap

/**
  * Created by Puchu on 6/21/2017.
  */
object W2VwithTFIDF {

  def getNGrams(sentence: String, n:Int): Array[Array[String]] = {
    val words = sentence
    val ngrams = words.split(' ').sliding(n)
    ngrams.toArray
  }

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "E:\\winutils")
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //Reading the Text File
    val documents = sc.textFile("data/doc.txt")

    //Getting the Lemmatised form of the words in TextFile
    val documentseq = documents.map(f => {
      //val lemmatised = CoreNLP.returnLemma(f)
      val splitString = f.split(" ")
      splitString.toSeq
    })

    //Creating an object of HashingTF Class
    val hashingTF = new HashingTF()
    //Creating Term Frequency of the document
    val tf = hashingTF.transform(documentseq)
    tf.cache()

    val idf = new IDF().fit(tf)

    //Creating Inverse Document Frequency
    val tfidf = idf.transform(tf)

    val tfidfvalues = tf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val values = ff(2).replace("]", "").replace(")", "").split(",")
      values
    })

    val tfidfindex = tf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val indices = ff(1).replace("]", "").replace(")", "").split(",")
      indices
    })

    print("1111111111111111")
    tfidf.foreach(f => println(f))

    val tfidfData = tfidfindex.zip(tfidfvalues)

    var hm = new HashMap[String, Double]

    tfidfData.collect().foreach(f => {
      hm += f._1 -> f._2.toDouble
    })

    val mapp = sc.broadcast(hm)

    val documentData = documentseq.flatMap(_.toList)
    val dd = documentData.map(f => {
      val i = hashingTF.indexOf(f)
      val h = mapp.value
      (f, h(i.toString))
    })

    val input=documents.map(line => line.split(" ").toSeq)
    val dd1 = dd.distinct().sortBy(_._2, false)
    dd1.take(10).foreach(f = f => {
      print("Top TF IDF word:")
      println(f)

      getW2V(f._1, sc, documentseq)


    })

  }

  def TFIDF(): Unit =
  {

  }

  def TFIDFwithLemma(): Unit =
  {

  }

  def TFIDFwithNGrams(): Unit =
  {

  }

  def getW2V(word: String,sc:SparkContext, input: RDD[Seq[String]])
  {
    val modelFolder = new File("myModelPath")
    if (modelFolder.exists()) {
      val sameModel = Word2VecModel.load(sc, "myModelPath")
      //println("9999999999",word);
      val synonyms = sameModel.findSynonyms(word, 5)
      for ((synonym, cosineSimilarity) <- synonyms) {
        println(s"$synonym $cosineSimilarity")
      }
    }
    else {
      val word2vec = new Word2Vec().setVectorSize(1000).setMinCount(1)
      val model = word2vec.fit(input)

      /*model.wordIndex.contains(word)*/
      val synonyms = model.findSynonyms(word, 5)

      for ((synonym, cosineSimilarity) <- synonyms) {
        println("Synonyms are:")
        println(s"$synonym $cosineSimilarity")
      }

      //model.getVectors.foreach(f => println(f._1 + ":" + f._2.length))
      //model.getVectors.foreach(f => println(f._1 + ":" + f._2.mkString(" ")))
      // Save and load model
      model.save(sc, "myModelPath")

    }

  }
}
