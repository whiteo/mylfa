package de.whiteo.mylfa.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Aspect
@Component
public class NoModifyDemoModeAspect {

    @Value("${app.demo-only}")
    private boolean demoOnly;

    @Pointcut("@annotation(NoModifyDemoMode)")
    public void noModifyMethods() {
    }

    @Around("noModifyMethods()")
    public Object validateDemoMode(ProceedingJoinPoint joinPoint) throws Throwable {
        if (demoOnly) {
            throw new UnsupportedOperationException("The service is in demo mode");
        }
        return joinPoint.proceed();
    }
}