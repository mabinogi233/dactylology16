package com.king.dactylology.Searcher.entry.searchitems;



import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import com.king.dactylology.Searcher.entry.Items.SearchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchItemFactory {

    @Autowired
    @Qualifier("utils")
    Utils utils;

    /**
     * 返回名称指定的搜索元
     * @param searchItemName
     * @return
     */
    public SearchItem getSearchItem(String searchItemName){
        if(searchItemName.equals("dactylology")){
            return (SearchItem) utils.getBean("dactylologySearchItem");
        }else {
            return null;
        }
    }

    /**
     * 返回一组名称对应的一组搜索元
     * @param searchItemNames
     * @return
     */
    public List<SearchItem> getSearchItems(String[] searchItemNames){
        List<SearchItem> searchItems= new ArrayList<SearchItem>();
        for(int i=0;i<searchItemNames.length;i++){
            searchItems.add(getSearchItem(searchItemNames[i]));
        }
        return searchItems;
    }

}
