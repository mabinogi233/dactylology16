package com.king.dactylology.Searcher.handle.handlers;


import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.handle.interfaces.Handle1;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class SimpleHandle1 implements Handle1 {

    @Override
    public Question handle(Question question, Map<String, Object> parameter) {
        return question;
    }
}
