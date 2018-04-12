package com.eastrobot.converter.service;

import com.eastrobot.converter.model.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * FileListener
 *
 * @author <a href="yogurt_lei@foxmail.com">Yogurt_lei</a>
 * @version v1.0 , 2018-04-11 14:25
 */
@Slf4j
@Component
public class FileMonitor {

    @Value("${convert.outputFolder}") //文件路径:${convert.outputFolder}/sn.extension
    private String OUTPUT_FOLDER;

    @Autowired
    private ConvertService convertService;

    @PostConstruct
    public void init() throws Exception {
        log.warn("*************file monitor is starting*************");
        // 默认轮询5s
        long interval = TimeUnit.SECONDS.toMillis(5);
        IOFileFilter filter = FileFilterUtils.and(FileFilterUtils.fileFileFilter());
        FileAlterationObserver observer = new FileAlterationObserver(OUTPUT_FOLDER, filter);
        observer.addListener(new FileListener());
        // 开始监控
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        monitor.start();
        log.warn("*************file monitor is running*************");
    }

    private class FileListener extends FileAlterationListenerAdaptor {
        @Override
        public void onFileCreate(File file) {
            log.info("onFileCreate >>>>>" + file.getAbsolutePath());
            String absolutePath = file.getAbsolutePath();
            ResponseMessage responseMessage = convertService.driver("", absolutePath);
        }
    }
}
