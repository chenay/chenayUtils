package com.chenay.platform.log;

public interface ILogger {

    void trace(String tag, String message);

    void debug(String tag, String message);

    void info(String tag, String message);

    void warning(String tag, String message);

    void error(String tag, String message);

    void asserts(String message);

    String getDefaultTag();
}
