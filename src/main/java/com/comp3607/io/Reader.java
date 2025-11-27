package com.comp3607.io;

import com.comp3607.questions.Question;
import java.util.List;

/**
 * Represents a reader capable of reading questions from a data source
 */
public interface Reader {
    /**
     * Reads questions from the data source
     * @return A list of questions read from the data source
     */
    List<Question> read();
}
