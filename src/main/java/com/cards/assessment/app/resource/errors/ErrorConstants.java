package com.cards.assessment.app.resource.errors;

import java.net.URI;

public final class ErrorConstants {
    private static final String PROBLEM_BASE_URL = "https://cards.app/problem";
    static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
}
