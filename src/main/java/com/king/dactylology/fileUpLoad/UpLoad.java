package com.king.dactylology.fileUpLoad;

import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


@Component
public class UpLoad {

    //工具类
    @Autowired
    @Qualifier("utils")
    Utils utils;

    //存储file的绝对路径-有效期
    private static Map<String, Date> dateMap;

    //x分钟内有效
    private static final int deadTimeMin = 10;

    //初始化静态对象
    static {
        dateMap = new Hashtable<>();
    }

    //存储空间根目录
    String rootPath = UpLoadConfig.rootPath;

    //分隔符
    private String sepa = java.io.File.separator;

    /**
     * 上传文件
     * @param file 前端发送的文件
     * @param uid 用户ID
     * @param fileName 文件名（带后缀）
     * @return
     */
    public Map<String,String> uploadFile(MultipartFile file, String uid , String fileName){
        //清理垃圾
        removeRubbish();
        Map<String, String> map = new HashMap<String,String>();
        try {
            //存储至用户空间
            String Path = rootPath +sepa+ uid +sepa+fileName;
            File dir = new File(rootPath +sepa+ uid);
            if(!dir.exists()){
                dir.mkdirs();
            }
            //输出到指定文件
            File outFile = new File(Path);
            file.transferTo(outFile);
            map.put("code", Code.UpFileSuccess.getCode());
            //添加有效期
            Date deadTime = utils.addDateMin(new Date(),deadTimeMin);
            if(dateMap.containsKey(Path)){
                dateMap.replace(Path,deadTime);
            }else{
                dateMap.put(Path,deadTime);
            }

        }catch (Exception e) {
            e.printStackTrace();
            map.put("code",Code.UpFileFail.getCode());
        }
        return map;
    }

    /**
     * 删除该用户上传的全部文件
     * @param uid
     */
    public void deleteAllFilesById(String uid){
        System.out.println("删除");
        String path = rootPath + sepa + uid;
        File baseFile = new File(path);
        if (baseFile.isFile() || !baseFile.exists()) {
            return;
        }
        File[] files = baseFile.listFiles();
        if(files!=null) {
            for (File deleteFile : files) {
                if(deleteFile.isFile()) {
                    boolean fl = deleteFile.delete();
                    if (!fl) {
                        //删除失败
                        //重试
                        this.deleteAllFilesById(uid);
                    }
                }else{
                    this._delete(deleteFile);
                }
            }
        }
        removeRubbish();
    }

    /**
     * 删除文件夹内的全部文件
     * @param packagefile
     */
    private void _delete(File packagefile){
        if(packagefile==null || packagefile.isFile()){
            return;
        }
        for(File file: Objects.requireNonNull(packagefile.listFiles())){
            if(file.isFile()){
                file.delete();
            }else{
                this._delete(file);
            }
        }
    }

    /**
     * 获取该用户上传的全部文件
     * @param uid
     * @return
     */
    public File[] getAllFilesById(String uid){
        String path = rootPath + sepa + uid;
        File baseFile = new File(path);
        if (baseFile.isFile() || !baseFile.exists()) {
            return null;
        }
        removeRubbish();
        return baseFile.listFiles();
    }


    /**
     * 获取该用户的用户空间目录
     * @param uid
     * @return
     */
    public String getRootPathById(String uid){
        return rootPath + sepa + uid;
    }


    /**
     * 清除过期的文件
     */
    private void removeRubbish(){
        try {
            for (String key : dateMap.keySet()) {
                if(dateMap.get(key).before(new Date())){
                    //清除，已过期
                    File deadFile = new File(key);
                    if(deadFile.exists()){
                        boolean fl = deadFile.delete();
                        if(fl){
                            dateMap.remove(key);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
