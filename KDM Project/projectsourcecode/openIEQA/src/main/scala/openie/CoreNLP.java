package openie;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by Mayanka on 27-Jun-16.
 */
public class CoreNLP {

    public static String returnTriplets(String sentence) {

        Document doc = new Document(sentence);
        String lemma="";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences


            Collection<Quadruple<String, String, String, Double>> l=sent.openie();

            lemma+= l.toString();
            System.out.println("lemma"+lemma);
        }
        return lemma;
    }
    public static String calculateAnswer(String sentence,String ques)
    {
        Document doc = new Document(sentence);
        Document quesDoc = new Document(ques);
        Collection<Quadruple<String, String, String, Double>> lTri;
        Collection<Quadruple<String, String, String, Double>> quesTri = null;

        for (Sentence question : quesDoc.sentences())
        {
            quesTri=question.openie();
            Iterator it=quesTri.iterator();
            while(it.hasNext())
            {
                String value=it.next().toString();
                System.out.println(" ques Value: "+value);
            }
        }
        String answer="";
        for (Sentence sent : doc.sentences())
        {
            lTri=sent.openie();
            Iterator i=lTri.iterator();
            while(i.hasNext())
            {
                int count =0;
                String value=i.next().toString().toLowerCase();
                //System.out.println("Value: "+value);
                StringTokenizer st=new StringTokenizer(value,",");
                String[] s=value.substring(value.indexOf("(")+1,value.indexOf(")")+1).split(",");
               // System.out.println("Value 0000: "+s[0]+" 1: "+s[1]);
                if(ques.contains(s[0]) && ques.contains(s[1]))
                {
                     answer = s[0]+ " "+s[1]+ " "+s[2];
               }
            }
           //System.out.println("answer :"+answer);
        }
        return answer;
    }
}
