package com.king.dactylology.Searcher.algorithm.algroithmItems;



import com.king.dactylology.Searcher.algorithm.algroithmItems.models.Word2vec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//分词算法
@Component
public class WordCut {

    @Autowired
    @Qualifier("baiduWord2vec")
    private Word2vec word2vec;


    /**
     * 分词
     * @param sentence
     * @return
     */
    public List<String> wordCut(String sentence){

        return word2vec.wordCut(sentence);
    }
}
