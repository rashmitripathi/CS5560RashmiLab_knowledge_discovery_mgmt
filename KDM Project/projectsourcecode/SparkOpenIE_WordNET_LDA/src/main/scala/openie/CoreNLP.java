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


        String[] quesnew = ques.toLowerCase().split("\\s+");
        //System.out.println("quesnew"+quesnew[0]+"  "+quesnew[1]+"  "+quesnew[2]);

        for (Sentence question : quesDoc.sentences())
        {
            quesTri=question.openie();
            //System.out.println(" ques openie: ");
            Iterator it=quesTri.iterator();
            while(it.hasNext())
            {
                String value=it.next().toString();
                //System.out.println(" ques Value: "+value);
            }
        }
        String answer="";

        for (Sentence sent : doc.sentences())
        {
           // System.out.println(" check here: "+sentence);
            lTri=sent.openie();

            Iterator i=lTri.iterator();
            while(i.hasNext())
            {
                int count =0;
                String value=i.next().toString().toLowerCase();
                StringTokenizer st=new StringTokenizer(value,",");
                String[] s=value.substring(value.indexOf("(")+1,value.indexOf(")")+1).split(",");
                System.out.println("first : "+s[0]);
                System.out.println("second :"+s[1]);
                StringTokenizer st1=new StringTokenizer(s[0],",");
                //StringTokenizer quesnew=new StringTokenizer(ques,",");
                String[] ansnew = s[0].split("\\s+");
                //System.out.println("ansnew"+ansnew[0]);
                /*if(ques.contains(s[0]) && ques.contains(s[1]))
                {
                     answer = s[0]+ " "+s[1]+ " "+s[2];
               }*/

                if(s[0].contains(quesnew[1]) && s[1].contains(quesnew[2]))
                {
                    System.out.println("cond true");
                    answer = ansnew[0]+ " "+quesnew[1]+ " "+quesnew[2]+" ";
                    if(answer.length() >= 5)
                      System.out.println("answer is :"+answer);
                }
            }
           //System.out.println("answer :"+answer);
        }
        return answer;
    }
}
