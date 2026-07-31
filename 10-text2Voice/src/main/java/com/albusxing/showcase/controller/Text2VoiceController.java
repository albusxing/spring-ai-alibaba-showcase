package com.albusxing.showcase.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeAudioSpeechApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author Albusxing
 * @created 2026/6/22
 */
@RestController
public class Text2VoiceController {


    @Resource
    private DashScopeAudioSpeechModel dashScopeAudioSpeechModel;

    // voice model
    public static final String BAILIAN_VOICE_MODEL = "cosyvoice-v2";
    // voice timber 音色列表：https://help.aliyun.com/zh/model-studio/cosyvoice-voice-list?spm=a2c4g.11186623.help-menu-2400256.d_0_3_5_5_0.66d148b2QKJ0Rt&scm=20140722.H_2997333._.OR_help-T_cn~zh-V_1
    public static final String BAILIAN_VOICE_TIMBER = "longanwen";


    /**
     * /t2v/voice
     *
     * @param message
     * @return
     */
    @GetMapping("/t2v/voice")
    public String voice(@RequestParam(name = "message", defaultValue = "温馨提醒，支付宝到账100元请注意查收") String message) {
        String filePath = "./" + UUID.randomUUID() + ".mp3";

        //1 语音参数设置
        DashScopeAudioSpeechOptions options = DashScopeAudioSpeechOptions.builder()
            .model(BAILIAN_VOICE_MODEL)
            .voice(BAILIAN_VOICE_TIMBER)
            .responseFormat(DashScopeAudioSpeechApi.ResponseFormat.MP3)
            .build();

        //2 调用大模型语音生成对象
        //TextToSpeechResponse response = dashScopeAudioSpeechModel.call(new TextToSpeechPrompt(message, options));
        Flux<TextToSpeechResponse> responseFlux = dashScopeAudioSpeechModel.stream(new TextToSpeechPrompt(message, options));

        //3 字节流语音转换
        byte[] audioBytes = collectStreamBytes(responseFlux);


        //4 文件生成
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(audioBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //5 生成路径OK
        return filePath;
    }


    private byte[] collectStreamBytes(Flux<TextToSpeechResponse> stream) {

        List<byte[]> chunks = stream
            .filter(r -> {
                if (r == null) return false;
                r.getResult();
                r.getResult();
                return true;
            })
            .map(r -> r.getResult().getOutput())
            .collectList()
            .block();

        if (chunks == null || chunks.isEmpty()) {

            return new byte[0];
        }

        int total = chunks.stream().mapToInt(b -> b.length).sum();
        byte[] result = new byte[total];
        int offset = 0;

        for (byte[] chunk : chunks) {

            System.arraycopy(chunk, 0, result, offset, chunk.length);
            offset += chunk.length;
        }

        return result;
    }
}
