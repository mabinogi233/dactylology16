package com.king.dactylology.Searcher.handle.handlers;


import com.king.dactylology.Searcher.entry.Items.Answer;
import com.king.dactylology.Searcher.handle.interfaces.Handle3;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SimpleHandle3 implements Handle3 {
    @Override
    public List<Answer> handle(List<Answer> answers, Map<String, Object> parameter) {
        return answers;
    }
}
