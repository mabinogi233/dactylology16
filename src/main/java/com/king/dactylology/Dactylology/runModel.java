package com.king.dactylology.Dactylology;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;

//此类用于调用python的网络模型
@Service
public class runModel {

    //python模型主函数所在文件的绝对路径
    private static final String modelPath = "D:\\codes\\codes\\dactylologyNetWord\\run_main.py";

    //python解释器的文件路径
    private static final String pythonExe = "D:\\Anaconda\\python.exe";

    //python网络中时间步的长度
    private static final int time_step = 10;

    //文件分隔符
    private String sepa = java.io.File.separator;

    /**
     * 运行python模型
     * @param file 视频或图片文件对象
     * @param rootPath 此用户空间的绝对路径
     * @return
     */
    public String runPython(File file,String rootPath) {
        if (file.isFile()) {
            //预处理，图片或视频
            String newPackageName = null;
            if (file.getName().split("\\.")[1].equals("png")) {
                newPackageName = rootPath + sepa + "test";
                this.picToPic(file, newPackageName);
            }

            //运行python，newPackageName作为参数
            String filePath = newPackageName;
            String command = pythonExe + " " + modelPath + " " + filePath;
            StringBuilder result = new StringBuilder();
            System.out.println("开始运行py模型");
            try {
                //运行
                Process p = Runtime.getRuntime().exec(command);
                System.out.println("py模型执行完毕");
                //读取输出
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "gbk"));
                String buff;
                while ((buff = br.readLine()) != null) {
                    result.append(buff);
                }
                System.out.println("结束运行py模型");
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("py模型运行异常");
                return "";
            }
        }
        return "";
    }

    /**
     * 用于在视频中提取time_step个帧，并将这time_step个帧存储至rootPath内
     * @param file 视频或图片文件对象
     * @param rootPath
     */
    private void vedioToPicture(File file,String rootPath){
        //取视频的time_step帧，存至rootPath下

    }

    /**
     * 处理图片，将图片复制time_step张并存入Path内
     * @param file
     * @param Path
     */
    private void picToPic(File file,String Path){
        //目录不存在则先创建目录
        File pathFile = new File(Path);
        if(!pathFile.exists()){
            pathFile.mkdir();
        }
        //将图片复制time_step张作为一个静止的视频
        for(int i=0;i<time_step;i++){
            File file1 = new File(Path+sepa+String.valueOf(i)+file.getName());
            copyFileUsingJava7Files(file,file1);
        }
    }

    /**
     * 复制文件
     * @param source 源文件
     * @param dest 复制后的文件
     */
    private static void copyFileUsingJava7Files(File source, File dest) {
        try {
            Files.copy(source.toPath(), dest.toPath());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
