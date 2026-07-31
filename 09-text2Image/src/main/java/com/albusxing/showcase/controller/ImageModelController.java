package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
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
 * @author Albusxing
 * @created 2026/6/25
 */
@RestController
public class ImageModelController {


    @Resource
    private DashScopeImageModel imageModel;

    public static final String IMAGE_MODEL = "wanx2.1-t2i-turbo";

    @GetMapping("/genImage")
    public void genImage(@RequestParam(value = "msg", defaultValue = "生成一只小猫") String msg,
                         HttpServletResponse response) {

        // 图片选项
        DashScopeImageOptions imageOptions = DashScopeImageOptions.builder()
            .withModel(IMAGE_MODEL)
            .withN(1)
            .withHeight(1024)
            .withWidth(1024)
            .build();

        ImageResponse imageResponse = imageModel.call(new ImagePrompt(msg, imageOptions));

        //获取生成图像的地址
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        //在浏览器输出
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
