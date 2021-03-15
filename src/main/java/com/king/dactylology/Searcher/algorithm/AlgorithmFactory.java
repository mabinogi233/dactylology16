package com.king.dactylology.Searcher.algorithm;



import com.king.dactylology.Searcher.algorithm.algroithmItems.GetKeyword;
import com.king.dactylology.Searcher.algorithm.algroithmItems.GetSimPoint;
import com.king.dactylology.Searcher.algorithm.algroithmItems.GetSimWord;
import com.king.dactylology.Searcher.algorithm.algroithmItems.WordCut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//算法factory用于获取算法的一个实例
@Component
public class AlgorithmFactory {

    @Autowired
    @Qualifier("wordCut")
    private WordCut wordCut;

    @Autowired
    @Qualifier("getKeyword")
    private GetKeyword getKeyword;

    @Autowired
    @Qualifier("getSimPoint")
    private GetSimPoint getSimPoint;

    @Autowired
    @Qualifier("getSimWord")
    private GetSimWord getSimWord;


    public GetSimWord getGetSimWord() {
        return getSimWord;
    }

    public GetKeyword getGetKeyword() {
        return getKeyword;
    }

    public GetSimPoint getGetSimPoint() {
        return getSimPoint;
    }

    public WordCut getWordCut() {
        return wordCut;
    }
}
