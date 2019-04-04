package io.codeleaf.logging.writers.elastic;

import io.codeleaf.logging.LogInvocation;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.codeleaf.logging.writers.elastic.utils.ElasticMappings.*;

public final class ElasticLogFormatter {

    public static final String FORMAT_VERSION = "1";

    public String getFormatVersion() {
        return FORMAT_VERSION;
    }

    private Map<String, Object> getSourceMapping() {
        return object("class_name", keyword(),
                "class_name_package", keyword(),
                "class_name_short", keyword(),
                "file_name", keyword(),
                "line_nr", number(),
                "method_name", keyword());
    }

    public Map<String, Object> getMapping() {
        return object(
                "logger", keyword(),
                "log_time", timestamp(),
                "log_level", keyword(),
                "source", getSourceMapping(),
                "host_name", text(),
                "thread_name", text(),
                "message", text());
    }

    public Map<String, Object> formatLog(LogInvocation invocation, String loggerName) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("logger", loggerName);
        map.put("log_time", invocation.getInvocationTime());
        map.put("log_level", invocation.getLogLevel().name());
        map.put("source", createSourceMap(invocation.getSource()));
        map.put("host_name", invocation.getHostName());
        map.put("thread_name", invocation.getThreadName());
        map.put("message", invocation.getMessage());
        return map;
    }

    private Map<String, Object> createSourceMap(StackTraceElement source) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (source != null) {
            map.put("class_name", source.getClassName());
            map.put("class_name_package", getPackageName(source.getClassName()));
            map.put("class_name_short", getShortName(source.getClassName()));
            map.put("file_name", source.getFileName());
            map.put("line_nr", source.getLineNumber());
            map.put("method_name", source.getMethodName());
        }
        return map;
    }

    private static String getPackageName(String className) {
        int index = className.lastIndexOf('.');
        return index > 0 ? className.substring(0, index) : "";
    }

    private static String getShortName(String className) {
        int index = className.lastIndexOf('.');
        return index > 0 ? className.substring(index + 1) : className;
    }

}
