package com.omfgdevelop.privatebookshelf.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VersionService {
    @Value("${app.version}")
    private String version;

    public String getVersion() {
        return version;
    }
}
