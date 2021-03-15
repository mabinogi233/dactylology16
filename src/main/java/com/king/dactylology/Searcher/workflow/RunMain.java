package com.king.dactylology.Searcher.workflow;



import com.king.dactylology.Searcher.entry.Items.Answer;
import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.entry.Items.SearchItem;
import com.king.dactylology.Searcher.entry.searchitems.SearchItemFactory;
import com.king.dactylology.Searcher.handle.HandlerFactory;
import com.king.dactylology.Searcher.handle.interfaces.Handle1;
import com.king.dactylology.Searcher.handle.interfaces.Handle2;
import com.king.dactylology.Searcher.handle.interfaces.Handle3;
import com.king.dactylology.Searcher.handle.interfaces.Handle4;
import com.king.dactylology.Searcher.workflow.interfaces.Searcher;
import com.king.dactylology.Searcher.workflow.interfaces.SelectCreator;
import com.king.dactylology.Searcher.workflow.interfaces.Sorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//主类，定义程序流程，需要执行run函数
@Service
public class RunMain {
    //查询语句生成
    @Autowired
    @Qualifier("mainSelectCreator")
    private SelectCreator selectCreator;

    //搜索器
    @Autowired
    @Qualifier("mainSearcher")
    private Searcher searcher;

    //排序器
    @Autowired
    @Qualifier("mainSorter")
    private Sorter sorter;

    //搜索元工厂
    @Autowired
    @Qualifier("searchItemFactory")
    private SearchItemFactory searchItemFactory;

    //拦截器工厂
    @Autowired
    @Qualifier("handlerFactory")
    private HandlerFactory handlerFactory;

    /**
     * 执行搜索
     * @param question 查询语句
     * @param parameterMap 搜索相关的参数
     * @return
     */
    public List<Answer> run(Question question, Map<String,Object> parameterMap){
        //获取拦截器
        Handle1 handle1 = handlerFactory.getHandle1();
        Handle2 handle2 = handlerFactory.getHandle2();
        Handle3 handle3 = handlerFactory.getHandle3();
        Handle4 handle4 = handlerFactory.getHandle4();

        //生成查询语句
        Question handleQuestion = handle1.handle(question,parameterMap);
        List<Question> questions = selectCreator.createSimSentence(handleQuestion);
        List<Question> handleQuestions = handle2.handle(questions,parameterMap);

        //获取搜索元
        String[] searchItemNames = (String[]) parameterMap.get("searchItemNames");
        List<SearchItem> searchItems = searchItemFactory.getSearchItems(searchItemNames);

        //执行查询过程
        List<Answer> unSortAnswers = searcher.search(handleQuestions,searchItems);

        //排序
        List<Answer> handleUnSortAnswers = handle3.handle(unSortAnswers,parameterMap);
        List<Answer> answers = sorter.sort(handleUnSortAnswers,question);
        List<Answer> handleAnswers = handle4.handle(answers,parameterMap);

        return handleAnswers;
    }

    /**
     * 采用默认参数进行查询
     * @param question 查询语句
     * @return
     */
    public List<Answer> run(Question question){
        Map<String,Object> paramMap = new HashMap<>();
        //搜索元采用dactylology搜索元
        paramMap.put("searchItemNames",new String[]{"dactylology"});
        return this.run(question,paramMap);
    }

}
