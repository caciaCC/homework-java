package com.db.homework;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class HomeworkApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void upLoad() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4FyYihUvcJEEuoRPTDYe";
        String accessKeySecret = "UcXmUKQY2gzRXtQYkiyVpj3eAgRc4c";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\Hasee\\Desktop\\111.png");
        ossClient.putObject("db-homework", "log.png", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传完成");
    }

}
