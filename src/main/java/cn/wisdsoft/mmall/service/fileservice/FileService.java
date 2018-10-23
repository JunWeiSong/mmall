package cn.wisdsoft.mmall.service.fileservice;

import cn.wisdsoft.mmall.common.ServerRespose;
import org.springframework.web.multipart.MultipartFile;

/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/20 21:21
* @Version:  1.0
*/
public interface FileService {

    String upload(MultipartFile file,String path);
    
}
