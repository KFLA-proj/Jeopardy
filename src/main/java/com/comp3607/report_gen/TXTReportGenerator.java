package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;

public class TXTReportGenerator implements ReportGenerator {
    private GameLogger logger;

    public TXTReportGenerator(GameLogger logger) {
        this.logger = logger;
    }

    @Override
    public void generate(GameLogger logger) {
        // figure out format later
    }
}
