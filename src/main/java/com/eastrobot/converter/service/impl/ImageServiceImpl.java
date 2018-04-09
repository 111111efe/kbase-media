package com.eastrobot.converter.service.impl;

import com.eastrobot.converter.config.ConvertConfig;
import com.eastrobot.converter.model.Constants;
import com.eastrobot.converter.model.OcrParseResult;
import com.eastrobot.converter.model.ResultCode;
import com.eastrobot.converter.service.ImageService;
import com.eastrobot.converter.service.YouTuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ImageServiceImpl
 *
 * @author <a href="yogurt_lei@foxmail.com">Yogurt_lei</a>
 * @version v1.0 , 2018-03-26 18:54
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ConvertConfig convertConfig;

    @Autowired
    private YouTuService youTuService;

    @Override
    public Boolean runFfmpegParseImagesCmd(final String videoPath) {/*
        String folder = ResourceUtil.getFolder(videoPath, "");

        FFmpeg fFmpeg = new FFmpeg("D:\\ffmpeg\\bin");
        fFmpeg.addParam("-y");
        fFmpeg.addParam("-i");
        fFmpeg.addParam(videoPath);
        fFmpeg.addParam("-r");
        fFmpeg.addParam("0.2");
        fFmpeg.addParam(folder + File.separator + "%005d.jpg");

        try {
            fFmpeg.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return true;
    }

    @Override
    public OcrParseResult handle(String imageFilePath) {
        String imageTool = (String) convertConfig.getDefaultImageConfig().get(Constants.DEFAULT_TOOL);

        if (Constants.YOUTU.equals(imageTool)) {
            try {
                String result = youTuService.ocr(imageFilePath);

                if (StringUtils.isBlank(result)) {
                    return new OcrParseResult(ResultCode.PARSE_EMPTY.getCode(), "");
                }

                return new OcrParseResult(ResultCode.SUCCESS.getCode(), result);
            } catch (Exception e) {
                log.error("ocr {} failed : {}", imageFilePath, e.getMessage());

                return new OcrParseResult(ResultCode.OCR_FAILURE.getCode(), e.getMessage());
            }
        } else {
            return new OcrParseResult(ResultCode.CFG_ERROR.getCode(), "");
        }
        // TODO by Yogurt_lei : 后续添加别的解析工具如tesseract
    }
}
