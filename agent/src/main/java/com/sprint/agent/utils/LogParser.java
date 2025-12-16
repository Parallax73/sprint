package com.sprint.agent.utils;

import java.util.Map;

public interface LogParser {
    Map<String, String> parse(String line);
}
