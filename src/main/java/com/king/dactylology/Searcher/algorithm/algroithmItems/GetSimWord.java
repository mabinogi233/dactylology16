package com.king.dactylology.Searcher.algorithm.algroithmItems;



import com.king.dactylology.Searcher.algorithm.algroithmItems.models.Word2vec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//近义词替换算法
@Component
public class GetSimWord {

    @Autowired
    @Qualifier("baiduWord2vec")
    private Word2vec word2vec;

    public List<Map.Entry<String, Float>> nearest(String word, int k){
        return null;
    }
}
