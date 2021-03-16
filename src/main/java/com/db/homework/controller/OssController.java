package com.db.homework.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.utils.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class OssController {


//    @RequestMapping("/oss/policy")
//    public Result policy()
//    {
//
//// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//
//        String endpoint = "oss-cn-beijing.aliyuncs.com"; // 请填写您的 endpoint。
//        String bucket = "db-homework"; // 请填写您的 bucketname 。
//        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
//        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
////        String callbackUrl = "http://88.88.88.88:8888";
//        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String dir = format+"/"; // 用户上传文件时指定的前缀。
//
//        // 创建OSSClient实例。
//        Map<String, String> respMap = null;
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
//        try {
//            long expireTime = 30;
//            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
//            Date expiration = new Date(expireEndTime);
//            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
//            PolicyConditions policyConds = new PolicyConditions();
//            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
//            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
//
//            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
//            byte[] binaryData = postPolicy.getBytes("utf-8");
//            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
//            String postSignature = ossClient.calculatePostSignature(postPolicy);
//
//            respMap = new LinkedHashMap<String, String>();
//            respMap.put("accessid", accessId);
//            respMap.put("policy", encodedPolicy);
//            respMap.put("signature", postSignature);
//            respMap.put("dir", dir);
//            respMap.put("host", host);
//            respMap.put("expire", String.valueOf(expireEndTime / 1000));
//            // respMap.put("expire", formatISO8601Date(expiration));
//
//
//
//        } catch (Exception e) {
//            // Assert.fail(e.getMessage());
//            System.out.println(e.getMessage());
//        } finally {
//            ossClient.shutdown();
//        }
//        return ResultFactory.buildSuccessResult(respMap);
//    }
//47图片上传
   @PostMapping("/back/books/covers")
 public String coversUpload(MultipartFile file) throws Exception {

       // Endpoint以杭州为例，其它Region请按实际情况填写。
       String endpoint = "oss-cn-beijing.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
       String bucketName = "db-homework";
// 创建OSSClient实例。
//       OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
// String name = StringUtils.getRandomString(6) + file.getName();
//// 上传文件流。
//       InputStream inputStream = new FileInputStream(file);
//       ossClient.putObject("db-homework", name, file);
//
//// 关闭OSSClient。
//       ossClient.shutdown();
//       System.out.println("上传完成");
////    File imageFolder = new File(folder);
////    File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
////            .substring(file.getOriginalFilename().length() - 4));
////    if (!f.getParentFile().exists())
////        f.getParentFile().mkdirs();
//    try {
//        file.transferTo(f);
//        String imgURL = "http://localhost:8443/api/file/" + f.getName();
//        return imgURL;
//    } catch (IOException e) {
//        e.printStackTrace();
//        return "";
//    }
//       ReturnVo returnVo = new ReturnVo();
//       Meta meta = new Meta();
       ArrayList<String> strings = new ArrayList<>();
       strings.add("image/png");
       strings.add("image/gif");
       strings.add("image/jpeg");
       String realName;
       String url;
       if (strings.contains(file.getContentType())){
           try {
               //获取文件名
               String fileName = file.getOriginalFilename();
               //获取文件类型
               String fileType = fileName.substring(fileName.lastIndexOf("."));
               //uuid
               String uuid = UUID.randomUUID().toString().replace("-","");
               realName = uuid + fileType;
               OSS build = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
               PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, realName, file.getInputStream());
               build.putObject(putObjectRequest);
//               ImageUploadVo imageUploadVo = new ImageUploadVo();
//               imageUploadVo.setTmpPath(realName);
               url = bucketName+"."+endpoint+"/"+realName;
//               returnVo.setData(imageUploadVo);
//               meta.setMsg("上传成功");
//               meta.setStatus(200);
//               returnVo.setMeta(meta);
           } catch (IOException e) {
               //发生异常
               url = "";
               e.printStackTrace();
//               returnVo.setData(null);
//               meta.setMsg("上传失败");
//               meta.setStatus(500);
//               returnVo.setMeta(meta);
           }
       }else {
           url = "";
           //非jpg/png/gif
//           returnVo.setData(null);
//           meta.setMsg("上传失败");
//           meta.setStatus(500);
//           returnVo.setMeta(meta);
       }
       System.out.println("上传完成");
       return "https://"+url;
   }
}
