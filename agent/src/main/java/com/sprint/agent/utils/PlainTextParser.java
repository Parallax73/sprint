package com.sprint.agent.utils;

import java.util.HashMap;
import java.util.Map;

public class PlainTextParser implements LogParser {

    @Override
    public Map<String, String> parse(String line) {
        Map<String, String> result = new HashMap<>();
        result.put("message", line);
        return result;
    }
}