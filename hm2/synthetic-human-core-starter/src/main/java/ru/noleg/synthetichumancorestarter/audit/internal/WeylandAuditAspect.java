package ru.noleg.synthetichumancorestarter.audit.internal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.noleg.synthetichumancorestarter.audit.api.AuditPublisher;
import ru.noleg.synthetichumancorestarter.configuration.SyntheticHumanProperties;

import java.util.Arrays;
import java.util.List;

@Aspect
public class WeylandAuditAspect {

    private static final Logger logger = LoggerFactory.getLogger(WeylandAuditAspect.class);
    private static final String AUDIT_MESSAGE_TEMPLATE = "Audit: method=%s, args=%s, result=%s";

    private final AuditPublisher kafkaAuditPublisher;
    private final SyntheticHumanProperties syntheticHumanProperties;

    public WeylandAuditAspect(AuditPublisher kafkaAuditPublisher,
                              SyntheticHumanProperties syntheticHumanProperties) {
        this.kafkaAuditPublisher = kafkaAuditPublisher;
        this.syntheticHumanProperties = syntheticHumanProperties;
    }

    @Around("@annotation(ru.noleg.synthetichumancorestarter.audit.api.WeylandWatchingYou)")
    public Object audit(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodName = signature.getMethod().getName();
        List<Object> args = Arrays.asList(pjp.getArgs());

        try {
            Object result = pjp.proceed();
            publishAuditMessage(methodName, args, result);
            return result;
        } catch (Throwable throwable) {
            logger.error("Audit failed for method: {}", methodName, throwable);
            throw throwable;
        }
    }

    private void publishAuditMessage(String methodName, List<Object> args, Object result) {
        String message = String.format(
                AUDIT_MESSAGE_TEMPLATE,
                methodName,
                args,
                result
        );

        switch (syntheticHumanProperties.auditMode()) {
            case CONSOLE:
                logger.info(message);
                break;
            case KAFKA:
                if (kafkaAuditPublisher != null) {
                    kafkaAuditPublisher.send(message);
                } else {
                    logger.warn("Kafka audit publisher is not available");
                }
                break;
            default:
                logger.warn("Unknown audit mode: {}", syntheticHumanProperties.auditMode());
        }
    }
}
