package com.eastrobot.converter.web.controller;

import com.eastrobot.converter.exception.BusinessException;
import com.eastrobot.converter.model.ResponseMessage;
import com.eastrobot.converter.model.ResultCode;
import com.eastrobot.converter.plugin.AsyncMode;
import com.eastrobot.converter.service.ConvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * AsyncConvertController
 *
 * @author <a href="yogurt_lei@foxmail.com">Yogurt_lei</a>
 * @version v1.0 , 2018-03-29 12:01
 */
@Api(tags = "转换接口(异步)")
@Slf4j
@RestController
@ConditionalOnBean(AsyncMode.class)
public class AsyncConvertController {

    @Autowired
    private ConvertService converterService;

    @ApiOperation(value = "上传视频,音频,图片,转换为文本.(异步模式上传为压缩文件)", response = ResponseMessage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "待转换文件", dataType = "__file", required = true, paramType = "form")
    })
    @PostMapping(
            value = "/convertAsync",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseMessage convertAsync(@RequestParam(value = "file") MultipartFile file) {
        String sn = UUID.randomUUID().toString();
        if (!file.isEmpty()) {
            try {
                converterService.uploadFile(file, sn, true);
            } catch (BusinessException e) {
                return new ResponseMessage(ResultCode.PREPARE_UPLOAD_FILE_ERROR);
            } catch (Exception e) {
                return new ResponseMessage(ResultCode.FILE_UPLOAD_FAILED);
            }

            return new ResponseMessage(ResultCode.SUCCESS, sn);
        } else {
            return new ResponseMessage(ResultCode.PARAM_ERROR);
        }
    }

    @ApiOperation(value = "通过sn来获得解析结果.", response = ResponseMessage.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sn", value = "异步模式上传文件返回的sn", dataType = "string", required = true, paramType =
                    "path")
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

}
