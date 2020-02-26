package cn.yang.tmall.service.impl;

import cn.yang.tmall.service.IFileService;
import cn.yang.tmall.utils.FTPUtil;
import cn.yang.tmall.utils.FileHandlerUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Yangtz
 * @ClassName: FileServiceImpl
 * @Description:
 * @create 2020-02-25 21:27
 */
@Service
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        String filePath = null;
        try {
            filePath = FileHandlerUtil.upload(file.getInputStream(), path, uploadFileName);
            logger.info("文件上传路径;{}", filePath);
        } catch (IOException e) {
            logger.error("文件获取异常", e);
        }
        try {
            File targetFile = new File(ResourceUtils.getURL("classpath:").getPath() + filePath);
            file.transferTo(targetFile);
            //上传FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return filePath;
    }
}
