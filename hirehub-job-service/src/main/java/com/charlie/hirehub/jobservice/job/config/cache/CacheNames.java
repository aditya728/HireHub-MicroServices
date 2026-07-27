package com.charlie.hirehub.jobservice.job.config.cache;

public final class CacheNames {

    private CacheNames() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String JOBS = "jobs";

    // Future caches
    public static final String COMPANIES = "companies";
    public static final String USERS = "users";
    public static final String REVIEWS = "reviews";
}
