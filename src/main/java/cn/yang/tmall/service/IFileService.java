package cn.yang.tmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Yangtz
 * @ClassName: IFileService
 * @Description:
 * @create 2020-02-25 21:27
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
