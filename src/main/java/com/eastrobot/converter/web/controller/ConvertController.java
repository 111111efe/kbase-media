package com.eastrobot.converter.web.controller;

import com.eastrobot.converter.model.ResponseMessage;
import com.eastrobot.converter.model.ResponseMessageAsync;
import com.eastrobot.converter.model.ResultCode;
import com.eastrobot.converter.plugin.RocketMQProducer;
import com.eastrobot.converter.service.ConvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * ConvertController
 *
 * @author <a href="yogurt_lei@foxmail.com">Yogurt_lei</a>
 * @version v1.0 , 2018-03-29 12:01
 */
@Api(tags = "视频音频图片转换接口")
@Slf4j
@RestController
public class ConvertController {

    @Autowired
    private ConvertService converterService;

    @ApiOperation(value = "上传视频,音频,图片,转换为文本.", response = ResponseMessage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "待转换文件", dataType = "__file", required = true, paramType = "form")
    })
    @PostMapping(
            value = "/convert",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseMessage convert(@RequestParam(value = "file") MultipartFile file) {
        if (!file.isEmpty()) {
            String sn = UUID.randomUUID().toString();
            String targetFile;
            try {
                targetFile = converterService.doUpload(file, sn, false);
            } catch (Exception e) {
                log.error("file upload error!", e);

                return new ResponseMessage(ResultCode.FILE_UPLOAD_FAILED);
            }

            return converterService.driver(targetFile, false);
        } else {
            return new ResponseMessage(ResultCode.PARAM_ERROR);
        }
    }

    @ApiOperation(value = "上传视频,音频,图片,转换为文本.(异步)", response = ResponseMessageAsync.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "待转换文件", dataType = "__file", required = true, paramType = "form")
    })
    @PostMapping(
            value = "/convertAsync",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseMessageAsync convertAsync(@RequestParam(value = "file") MultipartFile file) {
        String sn = UUID.randomUUID().toString();
        if (!file.isEmpty()) {
            String targetFile = "";
            try {
                converterService.doUpload(file, sn, true);
            } catch (Exception e) {
                log.error("file upload error!", e);

                return new ResponseMessageAsync(ResultCode.FILE_UPLOAD_FAILED, sn);
            }

            return new ResponseMessageAsync(ResultCode.ASYNC_FILE_UPLOAD_SUCCESS, sn);
        } else {
            return new ResponseMessageAsync(ResultCode.PARAM_ERROR, sn);
        }
    }

    @ApiOperation(value = "通过sn来获得解析结果.(异步)", response = ResponseMessage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sn", value = "异步上传返回的sn", dataType = "string", required = true, paramType = "path")
    })
    @GetMapping(
            value = "/convertAsync/{sn}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
    )
    public ResponseMessage convertAsync(@PathVariable String sn) {
        if (StringUtils.isNotBlank(sn)) {
            return converterService.findAsyncParseResult(sn);
        } else {
            return new ResponseMessage(ResultCode.PARAM_ERROR);
        }
    }

    @Autowired
    private RocketMQProducer producer;

    @ApiOperation(value = "测试发送消息.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "topic", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tag", value = "tag", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "message", value = "message", dataType = "string", required = true, paramType = "query")
    })
    @GetMapping("/pushMessage")
    public void pushMessage(@RequestParam(name = "topic") String topic,
                            @RequestParam(name = "tag") String tag,
                            @RequestParam(name = "message") String message) {
        producer.sendMessage(topic, tag, message);
    }

}
