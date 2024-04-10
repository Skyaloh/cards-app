package com.cards.assessment.app.resource.errors;

import org.springframework.util.CollectionUtils;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final BusinessException problem;

    private final String errorKey;

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        this(type, defaultMessage, entityName, errorKey, null);
    }

    public BadRequestAlertException(@Nonnull URI type, @Nonnull String defaultMessage, @Nonnull String entityName, @Nonnull String errorKey, @Nullable Map<String, Object> parameters) {
        problem = new BusinessException(type, Status.BAD_REQUEST, defaultMessage, buildParameters(entityName, errorKey, parameters));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> buildParameters(@Nonnull String entityName, @Nonnull String errorKey, @Nullable Map<String, Object> parameters) {
        Map<String, Object> all = new HashMap<>();
        all.put("params", entityName);
        all.put("message", "error." + errorKey);
        if (!CollectionUtils.isEmpty(parameters)) {
            all.putAll(parameters);
        }
        return all;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    @Override
    public URI getType() {
        return problem.getType();
    }

    @Nullable
    @Override
    public String getTitle() {
        return problem.getTitle();
    }

    @Nullable
    @Override
    public StatusType getStatus() {
        return problem.getStatus();
    }

    @Nullable
    @Override
    public String getDetail() {
        return problem.getDetail();
    }

    @Nullable
    @Override
    public URI getInstance() {
        return problem.getInstance();
    }

    @Override
    public Map<String, Object> getParameters() {
        return problem.getParameters();
    }

    @Override
    public synchronized ThrowableProblem getCause() {
        return problem.getCause();
    }

    @Override
    public String toString() {
        return problem.toString();
    }

    @Override
    public String getMessage() {
        return problem.getMessage();
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
