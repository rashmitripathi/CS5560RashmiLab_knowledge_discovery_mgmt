import java.io.File

import org.apache.spark.mllib.feature.{HashingTF, IDF, Word2Vec, Word2VecModel}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.HashMap

/**
  * Created by Rashmi on 6/27/2017.
  */
object QandA {

  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "E:\\winutils")
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val docText = sc.textFile("E:\\KDM\\Lab 2\\CS5560-Tutorial2B-SparkTransformationActions\\SparkTransformationActions\\input.txt")
    val documentseq = docText.map(f => {
      val splitString = f.split(" ")
      splitString.toSeq
    })

    val a = getNGrams(documentseq.toString(),3)
    a.foreach(f=>println(f.mkString(" ")))

    val nlp: NLPInputFIle = new NLPInputFIle();

    for (index <- 0 to 3) {
      println("Enter your question:")
      val ques = scala.io.StdIn.readLine().toLowerCase
      var qtype = ""

      if(ques.contains("topic") || ques.contains("summary"))
      {
        showtfanswer(documentseq,sc);

      }else if (ques.contains("who")){
        val answer = docText.map(line => {
          nlp.getLemmatizedorNERWord(line,"ner" ,"PERSON")
        })
        calculateAnswer(documentseq,sc,answer)
      }
      if (ques.contains("where")){
        val answer = docText.map(line => {
          nlp.getLemmatizedorNERWord(line,"ner" ,"LOCATION")
        })
        calculateAnswer(documentseq,sc,answer)
      }
      if (ques.contains("when")){
        val answer = docText.map(line => {
          nlp.getLemmatizedorNERWord(line,"ner" ,"DATE")
        })
        calculateAnswer(documentseq,sc,answer)
      }


    }
  }

  def calculateAnswer(documentseq:RDD[Seq[String]],sc:SparkContext,value: RDD[String])
  {
    println("answer can be through w2v :")
    val ans=value.flatMap(s=>{s.split(" ")}).map(w=>(w)).filter(f=>{f.length>2}).distinct()

    //get line containing that word
    val hashingTF = new HashingTF()
    val documentData = documentseq.flatMap(_.toList)
    val dd = documentData.map(f => {
     if(f.toLowerCase().contains(ans.take(1).toString))
     {
            //this line need to be searched for cosine similarity
              getW2V(ans.take(1).toString,sc, documentseq)
     }
    })

  }

  def showtfanswer(documentseq:RDD[Seq[String]],sc:SparkContext): Unit =
  {
   print("summary is:")
    val hashingTF = new HashingTF()
    val tf = hashingTF.transform(documentseq)
    tf.cache()
    val idf = new IDF().fit(tf)
    val tfidf = idf.transform(tf)

    val tfidfvalues = tfidf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val values = ff(2).replace("]", "").replace(")", "").split(",")
      values
    })

    val tfidfindex = tfidf.flatMap(f => {
      val ff: Array[String] = f.toString.replace(",[", ";").split(";")
      val indices = ff(1).replace("]", "").replace(")", "").split(",")
      indices
    })

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

    val dd1 = dd.distinct().sortBy(_._2, false)
    dd1.take(20).foreach(f => {
      print(f._1 + " ")
    })

  }

  def getNGrams(sentence: String, n:Int): Array[Array[String]] = {
    val words = sentence
    val ngrams = words.split(' ').sliding(n)
    ngrams.toArray
  }

  def getW2V(word: String,sc:SparkContext, input: RDD[Seq[String]])
  {
    val modelFolder = new File("myModelPath")
    if (modelFolder.exists()) {
      val sameModel = Word2VecModel.load(sc, "myModelPath")
      val synonyms = sameModel.findSynonyms(word, 5)
      for ((synonym, cosineSimilarity) <- synonyms) {
        println(s"$synonym $cosineSimilarity")
      }
    }
    else {
      val word2vec = new Word2Vec().setVectorSize(1000).setMinCount(1)
      val model = word2vec.fit(input)
      val synonyms = model.findSynonyms(word, 5)

      for ((synonym, cosineSimilarity) <- synonyms) {
        println("Synonyms are:")
        println(s"$synonym $cosineSimilarity")
      }

      model.save(sc, "myModelPath")

    }

  }

}
