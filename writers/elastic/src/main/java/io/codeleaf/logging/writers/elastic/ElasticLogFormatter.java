package io.codeleaf.logging.writers.elastic;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import io.codeleaf.logging.LogInvocation;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ElasticLogFormatter {

    public TypeMapping getMapping() {
        return TypeMapping.of(b -> b
                .properties("logger", p -> p.keyword(q -> q.index(true)))
                .properties("log_time", p -> p.date(q -> q.format("epoch_millis").index(true)))
                .properties("log_level", p -> p.keyword(q -> q.index(true)))
                .properties("source", p -> p.object(q -> q
                        .properties("class_name", r -> r.keyword(s -> s.index(true)))
                        .properties("class_name_package", r -> r.keyword(s -> s.index(true)))
                        .properties("class_name_short", r -> r.keyword(s -> s.index(true)))
                        .properties("file_name", r -> r.keyword(s -> s.index(true)))
                        .properties("line_nr", r -> r.integer(s -> s.index(true)))
                        .properties("method_name", r -> r.keyword(s -> s.index(true)))))
                .properties("host_name", p -> p.text(q -> q.index(true)))
                .properties("thread_name", p -> p.text(q -> q.index(true)))
                .properties("message", p -> p.text(q -> q.index(true))));
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
