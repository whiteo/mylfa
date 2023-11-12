package de.whiteo.mylfa.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Aspect
@Component
public class FileTypeCheckAspect {

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) && args(..,image)")
    public Object checkImageType(ProceedingJoinPoint joinPoint, MultipartFile image) throws Throwable {
        String contentType = image.getContentType();
        String originalFilename = image.getOriginalFilename();
        if (null == contentType || !contentType.equals("image/jpeg") ||
                null == originalFilename || !originalFilename.endsWith(".jpg")) {
            log.warn("File type check failed for: {}", originalFilename);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File must be a JPEG image.");
        }

        return joinPoint.proceed();
    }
}