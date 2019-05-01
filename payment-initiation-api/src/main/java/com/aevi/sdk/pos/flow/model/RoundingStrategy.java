package com.aevi.sdk.pos.flow.model;

/**
 * Strategies for rounding decimal numbers.
 */
public enum RoundingStrategy {
    /**
     * Always round down. Both 15.2 and 15.9 is rounded to 15.
     */
    DOWN,
    /**
     * Round to nearest. 15.2 is rounded to 15, and 15.9 is rounded to 16.
     */
    NEAREST,
    /**
     * Always round up. Both 15.2 and 15.9 is rounded to 16.
     */
    UP
}
