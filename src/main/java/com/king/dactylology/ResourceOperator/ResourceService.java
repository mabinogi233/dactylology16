package com.king.dactylology.ResourceOperator;


import com.king.dactylology.ResourceOperator.Dao.Mapper.resourceMapper;
import com.king.dactylology.ResourceOperator.Dao.entity.resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * 管理资源
 */
@Service
public class ResourceService {

    @Autowired
    resourceMapper mapper;

    //资源存储路径
    private static final String rootPath = "D:\\试验田\\resources";

    //分隔符
    private static final String sepa = java.io.File.separator;

    /**
     * 根据名称获取资源的id
     * @param word
     * @return
     */
    public int selectResouceIdByWord(String word){
        try {
            if (word != null) {
                List<resource> resources = mapper.selectResourceByWord(word);
                if (resources != null && resources.size() > 0) {

                    if (resources.size() > 1) {
                        //同名资源随机选择
                        Random random = new Random();
                        int index = random.nextInt(resources.size());
                        return resources.get(index).getId();
                    } else {
                        //只有一个资源时返回
                        return resources.get(0).getId();
                    }
                }
            }
            return -1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据id获取资源的绝对路径
     * @param id
     * @return
     */
    public String selectResoucePathById(int id){
        resource res = mapper.selectByPrimaryKey(id);
        if(res!=null && res.getFilepath()!=null){
            return res.getFilepath();
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
                res.setType(houzhui);
                res.setWord(name);
                res.setFilepath(realPath);
                mapper.insert(res);
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

}
