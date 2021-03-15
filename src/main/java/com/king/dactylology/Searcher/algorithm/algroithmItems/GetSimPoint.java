package com.king.dactylology.Searcher.algorithm.algroithmItems;



import com.king.dactylology.Searcher.algorithm.algroithmItems.models.Word2vec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//用于计算文本相似度
@Component
public class GetSimPoint {

    @Autowired
    @Qualifier("baiduWord2vec")
    private Word2vec word2vec;

    /**
     * 计算文本相似度
     * @param a
     * @param b
     * @return
     */
    public double getSentenceSimilarity(String a,String b){
        return word2vec.getSentenceSimilarity(a,b);
    }

}
