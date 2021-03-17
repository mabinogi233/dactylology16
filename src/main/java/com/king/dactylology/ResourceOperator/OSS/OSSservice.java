package com.king.dactylology.ResourceOperator.OSS;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import com.king.dactylology.ResourceOperator.Dao.Mapper.ossFileMapper;
import com.king.dactylology.ResourceOperator.Dao.Mapper.resourceMapper;
import com.king.dactylology.ResourceOperator.Dao.Mapper.wordTextMapper;
import com.king.dactylology.ResourceOperator.Dao.entity.ossFile;
import com.king.dactylology.ResourceOperator.Dao.entity.wordText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class OSSservice {

    @Autowired
    @Qualifier("utils")
    Utils utils;

    @Autowired
    ossFileMapper OSSMapper;

    @Autowired
    resourceMapper filelMapper;

    @Autowired
    wordTextMapper wordMapper;

    //OSS服务器参数
    private static final String endpoint ="oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId= "LTAIqE1ewqnPJPg7";
    private static final String secretAccessKey="zPiapnIblodMLQkEG0pufOMQLNsC7E";
    private static final String bucketName="dactylology";
    //URL失效时间
    private static final int hours = 365*24;

    /**
     * 从OSS服务器获取OSS文件的URL
     * @param filePath 本地文件绝对路径
     * @return url
     */
    public String getURLFromOSSService(String filePath){
        try {
            // 创建OSSClient实例。
            OSS ossClient = this.createOSSclient();
            //失效时间
            Date expiration = utils.addDateHour(new Date(),hours);
            // 生成URL，第一个参数为bucketName，第二个参数key为上传的文件路径名称，第三个为过期时间
            String filePathWindows = this.replaceOp(filePath);
            URL url = ossClient.generatePresignedUrl(bucketName, filePathWindows, expiration);
            //关闭
            ossClient.shutdown();
            return url.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传文件至OSS服务器
     * @param filePath 文件绝对路径
     */
    public void upLoadFileToOSSService(String filePath,int id){
        try{
            OSS ossClient = this.createOSSclient();
            InputStream inputStream = new FileInputStream(filePath);
            //上传图片，第一个参数为bucketName,第二个参数key为上传的文件路径名称，第三个为InputStream
            String filePathWindows = this.replaceOp(filePath);
            ossClient.putObject(bucketName,filePathWindows, inputStream);
            //关闭
            ossClient.shutdown();
            //更新数据库

            String url = this.getURLFromOSSService(filePathWindows);
            ossFile os1 = new ossFile();
            os1.setDeaddate(utils.addDateHour(new Date(), hours));
            os1.setUrl(url);
            os1.setId(id);
            if(OSSMapper.selectByPrimaryKey(id)==null) {
                OSSMapper.insert(os1);
            }else {
                OSSMapper.updateByPrimaryKey(os1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 生成OSSclient （OSS服务端）对象
     * @return
     */
    private OSS createOSSclient(){
        // 创建ClientConfiguration实例，您可以按照实际情况修改默认参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
        conf.setSupportCname(true);

        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey, conf);
    }

    /**
     * 验证fid对应的文件（包含本地与OSS），返回操作是否成功，若为false，后续请终止操作
     * @param fid
     * @return
     */
    public boolean check(int fid){
        try {
            ossFile of1 = OSSMapper.selectByPrimaryKey(fid);
            if (of1 != null) {
                Date deadDate = of1.getDeaddate();
                //失效时间小于当前时间，表示已失效
                if (deadDate.before(new Date())) {
                    //重新获取URL
                    String newUrl = this.getURLFromOSSService(filelMapper.selectByPrimaryKey(fid).getFilepath());
                    //更新数据条目
                    of1.setUrl(newUrl);
                    of1.setDeaddate(utils.addDateHour(new Date(), hours));
                    //更新数据库
                    OSSMapper.updateByPrimaryKey(of1);
                    return true;
                } else {
                    //未失效,验证本地文件是否存在
                    if (filelMapper.selectByPrimaryKey(fid) != null && filelMapper.selectByPrimaryKey(fid).getFilepath() != null) {
                        return true;
                    }else{
                        return false;
                    }
                }
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据id获取资源的URL
     * @param fid 文件fid
     * @return
     */
    public String getUrlById(int fid){
        try {
            if (check(fid)) {
                //验证成功
                ossFile of1 = OSSMapper.selectByPrimaryKey(fid);
                if (of1 != null) {
                    //返回URL
                    return of1.getUrl();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询资源附加属性
     * @param fid
     * @return
     */
    public wordText getWordText(int fid){
        return wordMapper.selectByPrimaryKey(fid);
    }

    /**
     * 修改文件路径格式，不能出现“/”，采用""替代
     * @param unixFilePath
     * @return
     */
    private String replaceOp(String unixFilePath){
        return unixFilePath.replace("/","");
    }

}



