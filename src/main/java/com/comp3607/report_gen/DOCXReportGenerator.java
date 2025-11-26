package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;

public class DOCXReportGenerator implements ReportGenerator {
    private GameLogger logger;

    public DOCXReportGenerator(GameLogger logger) {
        this.logger = logger;
    }

    @Override
    public void generate(GameLogger logger) {
        // figure out format later
    }
}
