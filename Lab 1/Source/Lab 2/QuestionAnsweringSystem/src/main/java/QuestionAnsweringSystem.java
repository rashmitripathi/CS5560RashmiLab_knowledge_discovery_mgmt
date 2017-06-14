import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Rashmi on 13-Jun-16.
 */
public class QuestionAnsweringSystem {


    static String question;

    public static String readFromFile(String fName) throws IOException
    {
        String text = null;
        File f = new File(fName); //for ex foo.txt
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(f);
            char[] chars = new char[(int) f.length()];
            fileReader.read(chars);

            text = new String(chars);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileReader !=null){fileReader.close();}
        }
        return text;
    }


    public static String parseQuestion()
    {   System.out.println("Welcome to Question Answering System, Please enter your question:");
        Scanner scanner = new Scanner(System. in);
        question = scanner. nextLine().toLowerCase();
        System.out.println("Question is:"+question);

        String qType="Misc";

        HashMap<String,String[]> qMap=new HashMap<String,String[]>();
        qMap.put("LOCATION",new String[]{"where"});
        qMap.put("PERSON",new String[]{"who","whose","whom"});
        qMap.put("TIME",new String[]{"when"});
        qMap.put("QUANTITY",new String[]{"how+few","how+liitle","how+much","how+many"});
        qMap.put("TIME",new String[]{"how+young","how+old","how+long"});


        String[] qWords = question.split("\\s+");

        for (String key : qMap.keySet())
        {	//System.out.println("Key : " + key);
            for (String value : qMap.get(key))
            {   if(question.contains(value))
            {	qType=key;
                break;
            }else if(value.contains("+"))
            {
                String value1=value.substring(0,value.indexOf("+"));
                String value2=value.substring(value.indexOf("+")+1);

                if(question.contains(value1) && question.contains(value2))
                {
                    qType=key;
                    break;
                }
            }
            }
        }
        System.out.println("qType : " + qType);

        return qType;
    }

    public static String parseAnswer()
    {
        String answer="sd";
        return answer;

    }

    public static void main(String args[]) throws IOException {


        String qType=parseQuestion();
        String answer=parseAnswer();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String text=readFromFile("E:\\KDM\\Day 2\\CS5560 - T1B(Introduction to CoreNLP) SourceCode (1)\\CS5560 - T2 (Introduction to CoreNLP) SourceCode\\Tutorial-3-CoreNLP\\src\\main\\java\\002.txt");

        HashSet<String> answerSet=new HashSet<String>();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel tokenWord : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                 String word = tokenWord.get(CoreAnnotations.TextAnnotation.class);
                 String lemma = tokenWord.get(CoreAnnotations.LemmaAnnotation.class);
                 String pos = tokenWord.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                 String ner = tokenWord.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                 if(qType.toLowerCase().equals(ner.toLowerCase()))
                 {
                          answerSet.add(word);
                 }
                 //System.out.println(token+" Text Annotation"+":" + word+" Lemma : "+lemma+" pos: "+pos+" NER:"+ne+"\n");
                //System.out.println( word+" -> "+lemma+" -> "+pos+" ->"+ner+"\n");
            }
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            //System.out.println("Tree"+tree);
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            //System.out.println("Dependencies:"+dependencies.toString());
            Map<Integer, CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
            //System.out.println("Corref"+graph.values().toString());
        }

        System.out.println("Possible Answers are:");

        // create an iterator
        Iterator iterator = answerSet.iterator();

        // check values
        while (iterator.hasNext()){
            System.out.println(iterator.next() + "\n");
        }

    }
}
