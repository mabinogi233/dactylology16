package com.king.dactylology.Searcher.workflow.mainutils;


import com.king.dactylology.Searcher.algorithm.AlgorithmFactory;
import com.king.dactylology.Searcher.algorithm.algroithmItems.GetKeyword;
import com.king.dactylology.Searcher.algorithm.algroithmItems.GetSimWord;
import com.king.dactylology.Searcher.algorithm.algroithmItems.WordCut;
import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.workflow.interfaces.SelectCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MainSelectCreator implements SelectCreator {

    @Autowired
    @Qualifier("algorithmFactory")
    private AlgorithmFactory algorithmFactory;

    /**
     * 对输入句子进行分词，后续会对每个词进行查询
     * @param question
     * @return
     */
    @Override
    public List<Question> createSimSentence(Question question) {

        List<Question> questions = new ArrayList<>();

        String str = question.getSelectSentence();
        //对question进行分词
        WordCut wordCut = algorithmFactory.getWordCut();
        List<String> words = wordCut.wordCut(str);

        //每个分词结果为一个查询语句
        for(String word:words){
            Question question1 = new Question();
            question1.setSelectSentence(word);
            questions.add(question1);
        }

        //返回替换后的句子结果
        return questions;
    }
}
