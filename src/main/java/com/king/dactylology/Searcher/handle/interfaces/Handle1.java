package com.king.dactylology.Searcher.handle.interfaces;


import com.king.dactylology.Searcher.entry.Items.Question;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface Handle1 {

    //初始查询过滤器
    Question handle(Question question, Map<String, Object> parameter);
}
