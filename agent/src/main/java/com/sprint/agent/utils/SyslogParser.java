package com.sprint.agent.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex. Matcher;
import java.util. regex.Pattern;

public class SyslogParser implements LogParser {

    private static final Pattern SYSLOG_PATTERN = Pattern.compile(
            "^(\\w{3}\\s+\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+(\\S+)\\s+(\\S+?)(? :\\[(\\d+)\\])?:\\s+(.*)$"
    );

    @Override
    public Map<String, String> parse(String line) {
        Map<String, String> result = new HashMap<>();

        Matcher matcher = SYSLOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            result.put("timestamp", matcher.group(1));
            result.put("hostname", matcher.group(2));
            result.put("process", matcher.group(3));
            result.put("pid", matcher.group(4));
            result.put("message", matcher.group(5));
        }

        return result;
    }
}