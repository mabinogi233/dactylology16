package com.king.dactylology.UserDataBase;


import com.king.dactylology.LoginModule.MainService;
import com.king.dactylology.LoginModule.Utils.UtilsItems.Utils;
import com.king.dactylology.ResourceOperator.OSS.OSSservice;
import com.king.dactylology.UserDataBase.DAO.Mapper.userinformationMapper;
import com.king.dactylology.UserDataBase.DAO.entity.userinformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class UserInfomationService {

    @Autowired
    userinformationMapper mapper;


    @Autowired
    @Qualifier("OSSservice")
    OSSservice osSservice;

    @Autowired
    @Qualifier("utils")
    Utils utils;

    //临时存储空间
    private static final String rootPath = "/www/wwwroot/dactylology.frogking.cn/linshi";

    //分隔符
    private String sepa = java.io.File.separator;

    /**
     * 上传头像
     * @param file mutipart头像
     * @param houzhui 头像后缀名，比如'txt'
     * @param uid 用户id
     * @return 返回头像URL
     * @throws IOException
     */
    public String uploadFile(MultipartFile file, String houzhui,int uid) throws IOException {
        String Path = rootPath +sepa + String.valueOf(uid) + "." + houzhui;
        //输出到指定文件
        File outFile = new File(Path);
        file.transferTo(outFile);
        //上传至OSS
        osSservice.uploadHeadPic(Path,"UserHeadPicture"+String.valueOf(uid));
        //获取url
        String url = osSservice.getHeadPic("UserHeadPicture"+String.valueOf(uid));
        //更新数据库
        if(mapper.selectByPrimaryKey(uid)==null){
            userinformation uf = new userinformation();
            uf.setUid(uid);
            uf.setNickname("默认昵称");
            uf.setDeaddate(utils.addDateHour(new Date(),OSSservice.hours));
            uf.setHeadpicurl(url);
            mapper.insert(uf);
        }else{
            userinformation uf = mapper.selectByPrimaryKey(uid);
            uf.setHeadpicurl(url);
            uf.setDeaddate(utils.addDateHour(new Date(),OSSservice.hours));
            mapper.updateByPrimaryKey(uf);
        }
        //删除本地文件
        outFile.delete();
        return url;
    }

    /**
     * 获取头像url
     * @param uid
     * @return
     */
    public String getHeadPictureUrl(int uid){
        if(mapper.selectByPrimaryKey(uid)==null){
            return null;
        }
        if(mapper.selectByPrimaryKey(uid).getDeaddate()!=null){
            if(mapper.selectByPrimaryKey(uid).getDeaddate().before(new Date())){
                //更新URL
                String url = osSservice.getHeadPic("UserHeadPicture"+String.valueOf(uid));
                userinformation us2 = mapper.selectByPrimaryKey(uid);
                us2.setHeadpicurl(url);
                us2.setDeaddate(utils.addDateHour(new Date(),OSSservice.hours));
                mapper.updateByPrimaryKey(us2);
                return url;
            }else{
                return mapper.selectByPrimaryKey(uid).getHeadpicurl();
            }
            //更新url
        }else{
            return null;
        }
    }

    /**
     * 获取用户昵称
     * @param uid
     * @return
     */
    public String getNickName(int uid){
        userinformation uf = mapper.selectByPrimaryKey(uid);
        if(uf!=null){
            if(uf.getNickname()!=null){
                return uf.getNickname();
            }else {
                return "默认昵称";
            }
        }
        return "默认昵称";
    }


    /**
     * 更新用户昵称，返回是否成功修改
     * @param uid
     * @param nickName
     * @return
     */
    public boolean updateNickName(int uid,String nickName){
        try {
            userinformation uf = mapper.selectByPrimaryKey(uid);
            if (uf != null) {
                uf.setNickname(nickName);
                mapper.updateByPrimaryKey(uf);
                if(mapper.selectByPrimaryKey(uid).getNickname().equals(nickName)){
                    return true;
                }else {
                    return false;
                }
            } else {
                userinformation uf1 = new userinformation();
                uf1.setNickname(nickName);
                uf1.setUid(uid);
                mapper.insert(uf1);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
