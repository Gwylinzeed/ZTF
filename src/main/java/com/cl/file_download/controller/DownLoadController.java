package com.cl.file_download.controller;


import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("download")
public class DownLoadController {

    @RequestMapping("{id}")
    @ResponseBody
    public String downLoad(@PathVariable("id") String id, HttpServletResponse res) {
        String filePath = getFilePath(id);
        if (StringUtils.isEmpty(filePath)) {
            return "file not found";
        }
        try {
            downLoadFile(filePath, res);
        } catch (Exception e) {
            System.out.println("--------file download failed---------" + filePath);
            e.printStackTrace();
        }
        return null;
    }

    private String getFilePath(String id) {
        String filePath = "";
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "doc";
        System.out.println(path);
        File[] files = new File(path).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
                if (prefixName.equals(id)) {
                    filePath = path + File.separator + id + ".docx";
                }
            }
        }
        return filePath;
    }

    private void downLoadFile(String filePath, HttpServletResponse res) throws Exception {
        File file = new File(filePath);
        res.setContentType("application/vnd.ms-excel;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(filePath, "UTF-8"));
        byte[] buffer = new byte[1024];
        //文件输入流
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        //输出流
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("----- file download success, filePath[" + filePath + "]");
    }
}
