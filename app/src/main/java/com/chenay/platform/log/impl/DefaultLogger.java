package com.chenay.platform.log.impl;

import com.chenay.platform.log.ILogger;

import org.slf4j.Logger;

/**
 * @author Y.Chen5
 */
public class DefaultLogger implements ILogger {


    public static final int OFF = 0x00;
    public static final int ASSERT = 0x01;
    public static final int ERROR = 0x02;
    public static final int WARN = 0x03;
    public static final int INFO = 0x04;
    public static final int DEBUG = 0x05;
    public static final int TRACE = 0x06;


    private String defaultTag = this.getClass().getName();
    private Logger logger;


    private int level = DEBUG;

    public void setLevel(int level) {
        this.level = level;
    }

    public DefaultLogger(Logger logger) {
        this.logger = logger;
    }


    public DefaultLogger(String defaultTag) {
        this.defaultTag = defaultTag;
    }

    @Override
    public void trace(String tag, String message) {
        if (level >= TRACE) {
            logger.debug(tag + " :{}", message);
        }
    }

    @Override
    public void debug(String tag, String message) {
        if (level >= DEBUG) {
            logger.debug(tag + " :{}", message);
        }
    }

    @Override
    public void info(String tag, String message) {
        if (message == null) {
            return;
        }
        if (level >= INFO) {
            logger.info(tag + " :{}", message);
        }
    }

    @Override
    public void warning(String tag, String message) {
        if (level >= WARN) {
            logger.warn(tag + " :{}", message);
        }
    }

    @Override
    public void error(String tag, String message) {
        if (level >= ERROR) {
            logger.error(tag + " :{}", message);
        }
    }

    @Override
    public void asserts(String message) {
        if (level >= ASSERT) {
            logger.trace("{}", message);
        }
    }

    @Override
    public String getDefaultTag() {
        return defaultTag;
    }

    public Logger getLogger() {
        return logger;
    }
}
