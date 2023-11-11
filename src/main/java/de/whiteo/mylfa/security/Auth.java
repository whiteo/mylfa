package de.whiteo.mylfa.security;

import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    Class<? extends HandlerInterceptor> value();
}