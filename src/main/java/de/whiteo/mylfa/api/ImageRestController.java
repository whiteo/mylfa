package de.whiteo.mylfa.api;

import de.whiteo.mylfa.security.AuthInterceptor;
import de.whiteo.mylfa.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageRestController {

    private final AuthInterceptor authInterceptor;
    private final ImageService service;

    @PostMapping("/parse")
    public ResponseEntity<String> parseImage(@RequestParam("image") MultipartFile image) {
        String parsedText = service.getSumFromImage(authInterceptor.getUserName(), image);
        return ResponseEntity.ok(parsedText);
    }
}