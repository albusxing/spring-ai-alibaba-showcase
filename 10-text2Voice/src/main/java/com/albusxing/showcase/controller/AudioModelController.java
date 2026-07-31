package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeAudioSpeechApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.audio.tts.Speech;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

import static com.albusxing.showcase.controller.Text2VoiceController.BAILIAN_VOICE_MODEL;
import static com.albusxing.showcase.controller.Text2VoiceController.BAILIAN_VOICE_TIMBER;

/**
 * @author Albusxing
 * @created 2026/6/25
 */
@RestController
public class AudioModelController {

    private static final String TEXT = "床前明月光， 疑是地上霜。 举头望明月， 低头思故乡。";
    private static final String PATH = "./";

//    @Autowired
//    private DashScopeSpeechSynthesisModel speechSynthesisModel;

    @Resource
    private DashScopeAudioSpeechModel dashScopeAudioSpeechModel;


    @GetMapping("/tts")
    public void tts() {
        DashScopeAudioSpeechOptions options = DashScopeAudioSpeechOptions.builder()
            .model(BAILIAN_VOICE_MODEL)
            .voice(BAILIAN_VOICE_TIMBER)
            .seed(1)
            .pitch(0.9)
            .volume(60)
            .build();

        TextToSpeechResponse textToSpeechResponse = dashScopeAudioSpeechModel.call(new TextToSpeechPrompt(TEXT, options));

        File file = new File(PATH + "/output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {

            List<byte[]> byteList = textToSpeechResponse.getResults().stream().map(Speech::getOutput).toList();
            byte[] output = mergeByteArraysOptimized(byteList);
            fos.write(output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static byte[] mergeByteArraysOptimized(List<byte[]> list) {
        // 1. 计算总长度
        int totalLength = 0;
        for (byte[] bytes : list) {
            if (bytes != null) {
                totalLength += bytes.length;
            }
        }

        // 2. 创建目标数组并逐个拷贝
        byte[] result = new byte[totalLength];
        int currentPos = 0;
        for (byte[] bytes : list) {
            if (bytes != null) {
                System.arraycopy(bytes, 0, result, currentPos, bytes.length);
                currentPos += bytes.length;
            }
        }
        return result;
    }
}
