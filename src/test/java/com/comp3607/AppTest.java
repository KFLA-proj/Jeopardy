package com.comp3607;

import com.comp3607.core_game.*;
import com.comp3607.questions.*;
import com.comp3607.io.*;
import com.comp3607.actions.*;
import com.comp3607.logger.*;
import com.comp3607.report_gen.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.*;

public class AppTest {

    @Test
    public void testVaultLoadInvalidFile() {
        Vault vault = new Vault();

        Reader reader = new CSVReader("nonexistent_file.csv");
        vault.loadQuestions(reader);

        assertTrue("Vault should be empty for invalid file", vault.getAll().isEmpty());
    }

    // --- Test 2: Single player correct answer ---
    @Test
    public void testSinglePlayerCorrectAnswer() {
        Game game = new Game();
        Player player = new Player("TestPlayer");
        game.getPlayers().add(player);

        Map<Character, String> options = Map.of(
                'A', "Option A",
                'B', "Option B",
                'C', "Option C",
                'D', "Option D");

        Question q = new Question("Category", 10, "Sample?", options, 'A');
        game.getVault().addQuestion(q);

        ActionTaken answerAction = new AnswerQuestionAction(player, q, 'A');
        answerAction.execute(game);

        assertEquals(10, player.getScore());
        assertTrue(game.getVault().getAll().isEmpty());
    }

    // --- Test 3: Single player wrong answer ---
    @Test
    public void testSinglePlayerWrongAnswer() {
        Game game = new Game();
        Player player = new Player("TestPlayer");
        game.getPlayers().add(player);

        Map<Character, String> options = Map.of(
                'A', "Option A",
                'B', "Option B",
                'C', "Option C",
                'D', "Option D");

        Question q = new Question("Category", 10, "Sample?", options, 'A');
        game.getVault().addQuestion(q);

        ActionTaken answerAction = new AnswerQuestionAction(player, q, 'B');
        answerAction.execute(game);

        assertEquals(0, player.getScore());
        assertTrue(game.getVault().getAll().contains(q));
    }

    // --- Test 4: Full game with 4 players random answers ---
    @Test
    public void testFullGameWith4PlayersRandomAnswers() {
        // --- Setup game ---
        Game game = new Game();
        Vault vault = game.getVault();

        // Load questions from CSV
        Reader reader = new CSVReader("sample_game_CSV.csv");
        vault.loadQuestions(reader);
        assertTrue("Vault should have questions loaded", vault.getAll().size() > 0);

        // Create 4 players
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        Player p3 = new Player("Charlie");
        Player p4 = new Player("Diana");
        game.getPlayers().addAll(List.of(p1, p2, p3, p4));

        // Attach logger
        GameLogger logger = new GameLogger("GAME001");
        game.attach(logger);

        Random random = new Random();
        int currentPlayerIndex = 0;

        // --- Play game ---
        while (!vault.getAll().isEmpty()) {
            Player currentPlayer = game.getPlayers().get(currentPlayerIndex);
            Question selectedQuestion = vault.getAll().get(0);

            // Player selects question
            ActionTaken selectAction = new SelectQuestionAction(currentPlayer, selectedQuestion);
            selectAction.execute(game);

            // Random answer (A-D)
            char answer = (char) ('A' + random.nextInt(4));
            ActionTaken answerAction = new AnswerQuestionAction(currentPlayer, selectedQuestion, answer);
            answerAction.execute(game);

            // Always remove question so loop ends
            vault.removeQuestion(selectedQuestion);

            // Next player
            currentPlayerIndex = (currentPlayerIndex + 1) % game.getPlayers().size();
        }

        // --- Assertions ---
        assertTrue("Vault should be empty at end of game", vault.getAll().isEmpty());
        assertTrue("At least one player should have points",
                p1.getScore() > 0 || p2.getScore() > 0 || p3.getScore() > 0 || p4.getScore() > 0);

        // --- Generate reports ---
        String outputFolder = "reports";
        new File(outputFolder).mkdirs();

        TXTReportGenerator txtReport = new TXTReportGenerator(outputFolder + "/game_report.txt");
        CSVReportGenerator csvReport = new CSVReportGenerator(outputFolder + "/game_event_log.csv");
        DOCXReportGenerator docxReport = new DOCXReportGenerator(outputFolder + "/game_report.docx");
        PDFReportGenerator pdfReport = new PDFReportGenerator(outputFolder + "/game_report.pdf");

        txtReport.generate(logger);
        csvReport.generate(logger);
        docxReport.generate(logger);
        pdfReport.generate(logger);

        System.out.println("All reports generated in: " + outputFolder);
    }

    // --- Test 5: Report generation exception handling ---
    @Test
    public void testReportGenerationExceptionHandling() {
        GameLogger logger = new GameLogger("TEST01");

        try {
            TXTReportGenerator txtReport = new TXTReportGenerator("/invalid_path/report.txt");
            txtReport.generate(logger);
        } catch (Exception e) {
            fail("Report generation should not throw an exception, but got: " + e.getClass().getSimpleName());
        }
    }
}
