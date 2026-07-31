package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 *
 * https://help.aliyun.com/zh/model-studio/text-to-image?spm=a2c4g.11186623.help-menu-2400256.d_0_5_0.1a457d9dv6o7Kc&accounttraceid=6ec3bf09599f424a91a2a88b27b31570nrdd
 *
 * @author Albusxing
 * @created 2026/6/22
 */
@RestController
public class Text2ImageController {


    // img model
    public static final String IMAGE_MODEL = "wanx2.1-t2i-turbo";

    @Resource
    private ImageModel imageModel;


    /**
     * /t2i/image
     *
     * @param message
     * @return
     */
    @GetMapping(value = "/t2i/image")
    public void image(@RequestParam(name = "message", defaultValue = "龙") String message,
                        HttpServletResponse response) {

        // 1. 构建图像生成参数
        DashScopeImageOptions options = DashScopeImageOptions.builder()
            .model(IMAGE_MODEL) // 指定图像生成模型
            .n(1)                      // 生成图片数量
            .height(1024)              // 图像高度
            .width(1024)               // 图像宽度
            .build();
        // 2. 调用模型生成图像
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(message, options));

        // 3. 获取生成图像的 URL 并在浏览器输出
        String imageUrl = imageResponse.getResult().getOutput().getUrl();

        try {
            URL url = URI.create(imageUrl).toURL();
            InputStream in = url.openStream();
            response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream().write(in.readAllBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
