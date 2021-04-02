package com.github.invictum.reportportal;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.utils.properties.PropertiesLoader;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    final ConcurrentHashMap<String, LinkedHashSet<String>> suites = new ConcurrentHashMap<>();
    final LogStorage logStorage = new LogStorage();
    final ListenerParameters rpParams = new ListenerParameters(PropertiesLoader.load());
    Date startTime;
}
