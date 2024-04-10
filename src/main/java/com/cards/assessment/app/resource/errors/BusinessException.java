package com.cards.assessment.app.resource.errors;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.zalando.problem.*;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An implementation similar to {@link AbstractThrowableProblem} but with a shorter inheritance tree
 */
public class BusinessException extends RuntimeException implements Exceptional {
    private final ThrowableProblem throwableProblem;

    BusinessException(@NonNull URI type, Status status, @NonNull String defaultMessage, @Nullable Map<String, Object> parameters) {
        final ProblemBuilder builder = Problem.builder()
            .withType(type)
            .withTitle(defaultMessage)
            .withStatus(status);
        if (!CollectionUtils.isEmpty(parameters)) {
            parameters.forEach(builder::with);
        }
        throwableProblem = builder.build();
    }


    @Override
    public URI getType() {
        return throwableProblem.getType();
    }

    @Nullable
    @Override
    public String getTitle() {
        return throwableProblem.getTitle();
    }

    @Nullable
    @Override
    public StatusType getStatus() {
        return throwableProblem.getStatus();
    }

    @Nullable
    @Override
    public String getDetail() {
        return throwableProblem.getDetail();
    }

    @Nullable
    @Override
    public URI getInstance() {
        return throwableProblem.getInstance();
    }

    @Override
    public Map<String, Object> getParameters() {
        return throwableProblem.getParameters();
    }

    @Override
    public synchronized ThrowableProblem getCause() {
        return throwableProblem.getCause();
    }

    @Override
    public String toString() {
        return Problem.toString(this);
    }

    @Override
    public String getMessage() {
        return Stream.of(this.getTitle(), this.getDetail()).filter(Objects::nonNull).collect(Collectors.joining(": "));
    }
}
