package de.whiteo.mylfa.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/version")
public class VersionRestController {

    @Value("${app.version}")
    private String appVersion;

    @GetMapping()
    public String getAppVersion() {
        return appVersion;
    }
}