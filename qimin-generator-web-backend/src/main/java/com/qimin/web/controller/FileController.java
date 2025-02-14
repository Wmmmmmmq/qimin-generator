package com.qimin.web.controller;

import cn.hutool.core.io.FileUtil;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qimin.web.annotation.AuthCheck;
import com.qimin.web.common.BaseResponse;
import com.qimin.web.common.ErrorCode;
import com.qimin.web.common.ResultUtils;
import com.qimin.web.constant.FileConstant;
import com.qimin.web.constant.UserConstant;
import com.qimin.web.exception.BusinessException;
import com.qimin.web.manager.CosManager;
import com.qimin.web.model.dto.file.UploadFileRequest;
import com.qimin.web.model.entity.User;
import com.qimin.web.model.enums.FileUploadBizEnum;
import com.qimin.web.service.UserService;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.device.BaseResp;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
            UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }

    /**
     * 测试文件上传
     *
     * @param multipartFile
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/file")
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile){
        // 文件目录
        String filename = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s",filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filePath,null);
            multipartFile.transferTo(file);
            cosManager.putObject(filePath,file);
            // 返回可访问的地址
            return ResultUtils.success(filePath);
        } catch (IOException e) {
            log.error("file upload error,filePath = ",filePath);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"上传失败");
        }finally {
            // 文件不管上传完成还是失败 都要记得把临时文件删除
            if(file!=null){
                boolean delete = file.delete();
                if (!delete){
                    log.error("file delete error,filePath = {}",filePath);
                }
            }
        }
    }

    /**
     * 测试文件下载
     *
     * @param filePath
     * @param httpServletResponse
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download/")
    public void testFileDownloadFile(String filePath, HttpServletResponse httpServletResponse) throws IOException {
        COSObjectInputStream cosObjectInputStream = null;
        try {
            COSObject cosObject = cosManager.getObject(filePath);
            cosObjectInputStream = cosObject.getObjectContent();
            // 处理下载的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            // 设置响应头
            httpServletResponse.setContentType("application/octet-stream;charset=UTF-8");
            httpServletResponse.setHeader("Content-Disposition","attachment; filename=" + filePath);
            // 写入响应
            httpServletResponse.getOutputStream().write(bytes);
            httpServletResponse.getOutputStream().flush();
        } catch (IOException e) {
            log.error("file download error,filepath = "+filePath,e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下载失败");
        } finally {
            if (cosObjectInputStream!=null){
                cosObjectInputStream.close();
            }
        }

    }
}
