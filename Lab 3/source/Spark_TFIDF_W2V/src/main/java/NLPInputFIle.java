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
import java.io.Serializable;
import java.util.*;

/**
 * Created by Rashmi on 6/18/2017.
 */
public class NLPInputFIle implements Serializable {
    Properties props;
    StanfordCoreNLP pipeline;

    StringBuilder str = new StringBuilder();

    public List<CoreMap> initialize(String text)
    {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        return sentences;

    }
    public String getLemmatizedorNERWord(String docText,String flag,String qType) throws IOException {

        List<CoreMap> sentences=initialize(docText);
        for (CoreMap sentence : sentences) {
            for (CoreLabel tokenWord : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String lemmatizedWord = tokenWord.get(CoreAnnotations.LemmaAnnotation.class);
                String ner = tokenWord.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String text = tokenWord.get(CoreAnnotations.TextAnnotation.class);
                if(flag.equals("lemma")) {
                    str.append(lemmatizedWord + " ");
                }else{
                    if(ner.toLowerCase().equals(qType.toLowerCase()))
                        str.append(text+" ");
                }

            }
        }
        return str.toString();
    }



}

