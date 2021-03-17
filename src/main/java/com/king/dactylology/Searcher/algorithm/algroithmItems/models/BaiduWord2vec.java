package com.king.dactylology.Searcher.algorithm.algroithmItems.models;

import com.baidu.aip.nlp.AipNlp;
import com.google.gson.internal.$Gson$Preconditions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
//使用百度API计算文本相似度
public class BaiduWord2vec extends Word2vec {

    private static final String APP_ID = "";
    private static final String API_KEY = "";
    private static final String SECRET_KEY = "";


    //计算文本相似度
    @Override
    public double getSentenceSimilarity(String a,String b){
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("model", "GRNN");
        // 文本相似度
        JSONObject res;
        do {
            res = client.simnet(a, b, options);
        }while (res.has("error_msg"));
        //System.out.println(res.toString(2));
        return res.getDouble("score");
    }

    /**
     * 分词算法
     * @param text
     * @return
     */
    @Override
    public List<String> wordCut(String text){

        List<String> wordStrs = new ArrayList<String>();

        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>();

        // 词法分析
        JSONObject res = client.lexer(text, options);
        JSONArray jsonArray = res.getJSONArray("items");
        for(int i=0;i<jsonArray.length();i++){
            wordStrs.add(jsonArray.getJSONObject(i).getString("item"));
        }
        return wordStrs;
    }

}
