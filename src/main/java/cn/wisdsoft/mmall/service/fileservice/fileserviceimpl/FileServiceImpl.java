package cn.wisdsoft.mmall.service.fileservice.fileserviceimpl;

import cn.wisdsoft.mmall.service.fileservice.FileService;
import cn.wisdsoft.mmall.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/20 21:21
* @Version:  1.0
*/
@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path){
        String filename = file.getOriginalFilename();
        String substring = filename.substring(filename.lastIndexOf(".") + 1);
        String s = UUID.randomUUID().toString();
        String uploadFileName = s+"."+substring;
        logger.info("开始上传文件，上传的文件名为：{}，上传的路径为：{}，新文件名为：{}",filename,path,uploadFileName);

        File filepath = new File(path);
        if(!filepath.exists()){
            filepath.setWritable(true);
            filepath.mkdirs();
        }
        File targetfile = new File(path, uploadFileName);
        try {
            //文件已经上传成功
            file.transferTo(targetfile);
            //已经上传到ftp服务器上了
            FTPUtil.uploadFile(Lists.newArrayList(targetfile));
            //删除本地图片
            targetfile.delete();
        } catch (IOException e) {
           logger.error("上传文件异常",e);
           return null;
        }
        return targetfile.getName();
    }

}
