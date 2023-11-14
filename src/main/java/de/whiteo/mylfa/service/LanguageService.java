package de.whiteo.mylfa.service;

import de.whiteo.mylfa.exception.AppRuntimeException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageService {

    @Value("${app.lang-directory}")
    private String directory;
    @Value("${app.lang-repository}")
    private String repository;
    @Value("${app.lang-file-extension}")
    private String fileExtension;

    @Async
    public void download(String lang) {
        String fileName = lang + fileExtension;

        if (fileExist(directory + fileName)) {
            log.info("File '{}' is already exist", fileName);
            return;
        }

        try {
            log.info("Downloading file '{}' has been started", fileName);
            URL url = new URL(repository + fileName);
            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(directory + fileName)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
            log.info("Downloading file '{}' has benn completed", fileName);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }

    @PostConstruct
    private void init() {
        download("osd");
    }

    private boolean fileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}