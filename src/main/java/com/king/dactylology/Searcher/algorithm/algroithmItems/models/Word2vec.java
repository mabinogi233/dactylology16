package com.king.dactylology.Searcher.algorithm.algroithmItems.models;



import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//word2vec算法
@Component
public class Word2vec {


    //计算两个词之间的相似度
    public double getWordSimilarity(String a,String b){
        return 0;
    }

    //获取词的k个相似的词
    public List<Map.Entry<String, Float>> nearest(String word, int k){
        return null;
    }

    //计算文本相似度
    public double getSentenceSimilarity(String a,String b){
        return 0;
    }
    public List<String> wordCut(String text){
        return null;
    }
}
