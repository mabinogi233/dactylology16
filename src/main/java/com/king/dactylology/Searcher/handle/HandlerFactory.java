package com.king.dactylology.Searcher.handle;



import com.king.dactylology.Searcher.handle.interfaces.Handle1;
import com.king.dactylology.Searcher.handle.interfaces.Handle2;
import com.king.dactylology.Searcher.handle.interfaces.Handle3;
import com.king.dactylology.Searcher.handle.interfaces.Handle4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//用于获取过滤器的实例
@Component
public class HandlerFactory {

    @Autowired
    @Qualifier("simpleHandle1")
    private Handle1 handle1;

    @Autowired
    @Qualifier("simpleHandle2")
    private Handle2 handle2;

    @Autowired
    @Qualifier("simpleHandle3")
    private Handle3 handle3;

    @Autowired
    @Qualifier("simpleHandle4")
    private Handle4 handle4;

    public Handle1 getHandle1() {
        return handle1;
    }

    public Handle2 getHandle2() {
        return handle2;
    }

    public Handle3 getHandle3() {
        return handle3;
    }

    public Handle4 getHandle4() {
        return handle4;
    }
}
