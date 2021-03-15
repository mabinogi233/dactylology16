package com.king.dactylology.Searcher.handle.handlers;


import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.handle.interfaces.Handle2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SimpleHandle2 implements Handle2 {
    @Override
    public List<Question> handle(List<Question> questionsIn, Map<String, Object> parameter) {
        return questionsIn;
    }
}
