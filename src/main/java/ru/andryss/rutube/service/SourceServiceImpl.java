package ru.andryss.rutube.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class SourceServiceImpl implements SourceService {
    @SneakyThrows
    @Override
    public String generateUploadLink(String sourceId) {
        return String.format("http://%s/api/source/%s", InetAddress.getLocalHost().getHostAddress(), sourceId);
    }
}
