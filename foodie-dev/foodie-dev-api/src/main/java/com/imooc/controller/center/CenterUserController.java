package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Bo.center.CenterUserBO;
import com.imooc.pojo.Users;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoming.zhang on 2021/3/24.
 */

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RequestMapping("userInfo")
@RestController
public class CenterUserController extends BaseController{

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("/uploadFace")
    @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "POST")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        //定义头像保存地址
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为每一个用户增加一个userid， 用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        //开始文件上传
        if (file != null) {
            //文件输出保存到目录
            FileOutputStream fileOutputStream = null;
            try {
            // 获得文件上传的文件名称
            String fileName= file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                //文件重命名
                String[] fileNames = fileName.split("\\.");
                //获取文件后缀名
                String suffix = fileNames[fileNames.length - 1];
                //防止从后端直接恶意攻击
                if (!suffix.equalsIgnoreCase("png")
                        && !suffix.equalsIgnoreCase("jpg")
                        && !suffix.equalsIgnoreCase("jpeg")) {
                    return IMOOCJSONResult.errorMsg("图片格式不正确");
                }

                //文件名重组, 覆盖式上传，增量式（当前时间）
                String newFileName = "face-" + userId + "." + suffix;
                //上传的头像最终保存位置
                String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                //用于提供给web服务访问地址
                uploadPathPrefix += ("/" + newFileName);
                File outFile = new File(finalFacePath);
                if (outFile.getParentFile() != null) {
                    //创建文件夹
                    outFile.getParentFile().mkdirs();
                }

                fileOutputStream = new FileOutputStream(outFile);
                InputStream inputStream = file.getInputStream();
                IOUtils.copy(inputStream, fileOutputStream);
            }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return IMOOCJSONResult.errorMsg("用户头像文件不能为空");
        }
        //获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();
        //由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix + "?t=" +
                DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        centerUserService.updateUserFace(userId, finalUserFaceUrl.replaceAll("\\\\", "\\/"));
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) {
        //判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return IMOOCJSONResult.errorMap(errors);
        }
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        Users usersResult = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersResult), true);
        //TODO 后续要改，增加令牌token，会整合进redis, 分布式会话
        return IMOOCJSONResult.ok(usersResult);
    }

    private Map<String, String> getErrors(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError error: fieldErrors) {
            //发生验证错误所对应的某个属性
            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();
            map.put(field, defaultMessage);
        }
        return map;
    }
    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
