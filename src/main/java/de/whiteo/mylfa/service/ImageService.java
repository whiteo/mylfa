package de.whiteo.mylfa.service;

import de.whiteo.mylfa.domain.User;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserService userService;

    @Value("${app.lang-directory}")
    private String directory;

    public String getSumFromImage(String userName, MultipartFile image) {
        User user = userService.findByEmail(userName);
        String lang = userService.getLangProperty(user);

        CompletableFuture<String> ocrResultFuture = parseImage(lang, image);

        try {
            String ocrResult = ocrResultFuture.join();
            return extractSumFromString(ocrResult);
        } catch (Exception e) {
            System.out.println("OCR processing error: " + e.getMessage());
            return "0";
        }
    }

    @Async
    protected CompletableFuture<String> parseImage(String lang, MultipartFile image) {
        Tesseract tesseract = new Tesseract();
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        tesseract.setVariable("user_defined_dpi", "300");
        tesseract.setVariable("debug_file", getNullDevicePath());
        tesseract.setLanguage(lang);
        tesseract.setDatapath(directory);

        try {
            byte[] bytes;
            bytes = image.getBytes();
            InputStream is = new ByteArrayInputStream(bytes);
            return CompletableFuture.completedFuture(tesseract.doOCR(ImageIO.read(is)));
        } catch (TesseractException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractSumFromString(String text) {
        Pattern pattern = Pattern.compile("\\b\\d+[.,]\\d{2}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "0";
    }

    private String getNullDevicePath() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "nul";
        } else {
            return "/dev/null";
        }
    }
}