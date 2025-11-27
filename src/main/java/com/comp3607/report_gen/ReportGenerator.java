package com.comp3607.report_gen;

import com.comp3607.logger.*;

/**
 * Interface for generating reports from game logs
 */
public interface ReportGenerator {
    /**
     * Generates a report from the provided GameLogger
     * @param logger the GameLogger containing the game events to log
     */
    void generate(GameLogger logger);
}
