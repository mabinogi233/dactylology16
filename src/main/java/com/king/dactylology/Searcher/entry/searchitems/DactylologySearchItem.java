package com.king.dactylology.Searcher.entry.searchitems;

import com.king.dactylology.ResourceOperator.ResourceService;
import com.king.dactylology.Searcher.algorithm.AlgorithmFactory;
import com.king.dactylology.Searcher.algorithm.algroithmItems.GetSimWord;
import com.king.dactylology.Searcher.entry.Items.Answer;
import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.entry.Items.SearchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 核心搜索元
 */
@Component
public class DactylologySearchItem extends SearchItem {

    @Autowired
    ResourceService resourceService;

    @Autowired
    @Qualifier("algorithmFactory")
    AlgorithmFactory algorithmFactory;

    //查询失败时生成的近义查询词的个数
    private static final int k = 5;

    /**
     * 执行搜索，输入文本，返回与文本匹配的资源的id
     * @param question
     * @return
     */
    @Override
    public List<Answer> search(Question question) {
        String word = question.getSelectSentence();
        List<Integer> fids = resourceService.selectResouceIdByWord(word);
        if(fids!=null) {
            List<Answer> answers = new ArrayList<>();
            Answer answer = new Answer();
            answer.setIntroduction(word);

            StringBuilder fidStr = new StringBuilder();
            boolean hasKeyWord = false;
            for(int fid:fids) {
                //拥有关键词
                if (resourceService.selectNameById(fid)!=null && resourceService.selectNameById(fid).equals(word)){
                    hasKeyWord = true;
                }
                fidStr.append(fid);
                fidStr.append(",");
            }
            fidStr.deleteCharAt(fidStr.length()-1);
            if(hasKeyWord) {
                answer.setUrl(String.valueOf(fidStr));
            }else {
                answer.setUrl("-1");
            }
            answers.add(answer);
            return answers;
        }else {
            //查询失败
            //可通过生成近义词再次进行查询
            GetSimWord getSimWord = algorithmFactory.getGetSimWord();
            List<Map.Entry<String, Float>> simWords = getSimWord.nearest(word,k);
            if(simWords!=null){
                for(Map.Entry<String, Float> map:simWords) {
                    //近义词查询成功
                    List<Integer> sim_fids = resourceService.selectResouceIdByWord(map.getKey());
                    if(sim_fids!=null){
                        List<Answer> answers = new ArrayList<>();

                        Answer answer = new Answer();
                        answer.setIntroduction(word);

                        StringBuilder fidStr = new StringBuilder();
                        boolean hasKeyWord = false;

                        for(int sim_fid:sim_fids) {
                            //拥有关键词
                            if (resourceService.selectNameById(sim_fid)!=null && resourceService.selectNameById(sim_fid).equals(map.getKey())){
                                hasKeyWord = true;
                            }
                            fidStr.append(sim_fid);
                            fidStr.append(",");
                        }
                        fidStr.deleteCharAt(fidStr.length()-1);

                        if(hasKeyWord) {
                            answer.setUrl(String.valueOf(fidStr));
                        }else {
                            answer.setUrl("-1");
                        }
                        return answers;
                    }
                }
            }
            //失败
            List<Answer> answers = new ArrayList<>();
            Answer answer = new Answer();
            answer.setIntroduction(word);
            answer.setUrl(String.valueOf(-1));
            answers.add(answer);
            return answers;
        }
    }
}
