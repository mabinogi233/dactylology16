package com.king.dactylology.ResourceOperator;


import com.king.dactylology.ResourceOperator.Dao.Mapper.resourceMapper;
import com.king.dactylology.ResourceOperator.Dao.Mapper.wordTextMapper;
import com.king.dactylology.ResourceOperator.Dao.entity.ossFile;
import com.king.dactylology.ResourceOperator.Dao.entity.resource;
import com.king.dactylology.ResourceOperator.Dao.entity.wordText;
import com.king.dactylology.ResourceOperator.OSS.OSSservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * 管理资源
 */
@Service
public class ResourceService {

    @Autowired
    resourceMapper mapper;

    @Autowired
    wordTextMapper textMapper;

    @Autowired
    OSSservice osSservice;

    //图片资源存储路径
    private static final String rootPath = "D:\\试验田\\javaproject1";

    //视频资源路径
    private static final String rootPathMov = "D:\\试验田\\javaproject1";

    //词意解释文件txt的文件夹路径
    private static final String rootPathTxt = "D:\\试验田\\javaproject1";

    //分隔符
    private static final String sepa = java.io.File.separator;

    /**
     * 根据txt文件录入词义解释
     * @return
     */
    public void insertWordText() {
        textMapper.deleteAll();
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File file = new File(rootPathTxt);
            if(file.isDirectory()){

                for(File file1 : Objects.requireNonNull(file.listFiles())){
                    StringBuilder text = new StringBuilder();
                    String word = "";
                    //读取文件名
                    String fileName = file.getName();
                    word = fileName.split("\\.")[0];
                    String houzhui = fileName.split("\\.")[1];
                    if(!houzhui.equals("txt")){
                        continue;
                    }
                    //读取词义
                    InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file1));
                    BufferedReader bf = new BufferedReader(inputReader);
                    // 按行读取字符串
                    String str;
                    while ((str = bf.readLine()) != null) {
                        text.append(str);
                    }
                    bf.close();
                    inputReader.close();

                    //加入列表
                    String line = word + "," + text.toString();
                    arrayList.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对ArrayList中存储的字符串进行处理
        for(String s:arrayList){
            String[] reStr = s.split(",");
            assert reStr.length == 2;
            String word = reStr[0];
            String text = reStr[1];
            List<resource> l = mapper.selectResourceByWord(word);
            for(resource r:l){
                int rId = r.getId();
                wordText w = new wordText();
                w.setId(rId);
                w.setPos("暂无");
                w.setText(text);
                textMapper.insert(w);
                break;
            }
        }

    }

    /**
     * 根据名称获取资源的id
     * @param word
     * @return
     */
    public List<Integer> selectResouceIdByWord(String word){

        try {
            List<Integer> rList = new ArrayList<>();
            if (word != null) {
                List<resource> resources = mapper.selectResourceByWord(word);
                if (resources != null && resources.size() > 0) {
                    for(resource rs:resources){
                        rList.add(rs.getId());
                    }
                    return rList;
                    /*
                    if (resources.size() > 1) {
                        //同名资源随机选择
                        Random random = new Random();
                        int index = random.nextInt(resources.size());
                        return resources.get(index).getId();
                    } else {
                        //只有一个资源时返回
                        return resources.get(0).getId();
                    }
                    */
                }else{
                    rList.add(-1);
                    return rList;
                }
            }else{
                rList.add(-1);
                return rList;
            }
        }catch (Exception e){
            e.printStackTrace();
            List<Integer> rList = new ArrayList<>();
            rList.add(-1);
            return rList;
        }
    }

    /**
     * 根据id获取图片资源的绝对路径
     * @param id
     * @return
     */
    public String selectResoucePicPathById(int id){
        resource res = mapper.selectByPrimaryKey(id);
        if(res!=null && res.getFilepathpic()!=null){
            return res.getFilepathpic();
        }
        return null;
    }

    /**
     * 根据id获取视频资源的绝对路径
     * @param id
     * @return
     */
    public String selectResouceMovPathById(int id){
        resource res = mapper.selectByPrimaryKey(id);
        if(res!=null && res.getFilepathmovie()!=null){
            return res.getFilepathmovie();
        }
        return null;
    }

    /**
     * 更新rootPath下的资源，资源文件名即为word，filepath为其绝对路径
     */
    public void updateResouce(){
        //查询rootPath下的全部文件
        File rootFile = new File(rootPath);
        if(rootFile.isFile() && !rootFile.exists()){
            return;
        }
        File[] fs =rootFile.listFiles();
        if(fs==null){
            return;
        }
        //清空数据库
        mapper.deleteAll();
        //添加
        for(File file:fs){
            System.out.println(file.getName()+"收集中");
            if(file.isFile()){
                //加入数据库
                int fid = 1;
                if(mapper.MaxId()!=null){
                    fid = mapper.MaxId();
                    do {
                         fid += 1;
                    }while (mapper.selectByPrimaryKey(fid)!=null);
                }
                String fileName = file.getName();
                String realPath = rootFile + sepa + fileName;
                String name = fileName.split("\\.")[0];
                String houzhui = fileName.split("\\.")[1];

                resource res = new resource();
                res.setId(fid);
                res.setWord(name);
                res.setFilepathpic(realPath);

                boolean fl = false;
                //处理视频数据
                File rootFileMov = new File(rootPathMov);
                if(rootFileMov.isFile() && !rootFileMov.exists()){
                    return;
                }
                File[] files =rootFileMov.listFiles();
                if(files!=null) {
                    for (File file1 : files) {
                        System.out.println("查询到"+file1.getName()+"视频");
                        if (file1.isFile()) {
                            String fileName1 = file1.getName();
                            String realPath1 = rootFileMov + sepa + fileName1;
                            String name1 = fileName1.split("\\.")[0];
                            String houzhui1 = fileName1.split("\\.")[1];
                            if (name1.equals(name)) {
                                res.setFilepathmovie(realPath1);
                                res.setType(houzhui+","+houzhui1);
                                fl = true;
                                break;
                            }
                        }
                    }
                }
                if(fl) {
                    mapper.insert(res);
                    //上传到OSS
                    System.out.println("上传至服务器");
                    osSservice.upLoadFileToOSSService(res.getFilepathpic(), fid, "pic");
                    osSservice.upLoadFileToOSSService(res.getFilepathmovie(), fid, "movie");
                }

            }

        }
    }





    /**
     * 获取全部资源主键
     * @return
     */
    public List<Integer> getAllResourceId(){
        return mapper.getAll();
    }


    /**
     * 根据id获取资源名称
     * @param id
     * @return
     */
    public String selectNameById(int id){
        if(mapper.selectByPrimaryKey(id)!=null){
            return mapper.selectByPrimaryKey(id).getWord();
        }else{
            return null;
        }
    }

}
