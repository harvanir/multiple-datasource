package org.harvanir.demo.datasource.advice;

/**
 * @author Harvan Irsyadi
 */
public enum RouteContext {

    /**
     * Always recreate route context, and cleanup after completion.
     */
    ALWAYS_NEW,

    /**
     * Use existing route context, create newest one if empty / different and cleanup after completion.
     */
    REQUIRED,

    /**
     * Use existing route context.
     */
    REUSE
}
