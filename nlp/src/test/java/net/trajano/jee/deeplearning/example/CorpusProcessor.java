package net.trajano.jee.deeplearning.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CorpusProcessor {

    public static final String SPECIALS = "!\"#$;%^:?*()[]{}<>«»,.–—=+…";

    private final boolean countFreq;

    private Map<String, Double> dict = new HashMap<>();

    private final Set<String> dictSet = new HashSet<>();

    private final Map<String, Double> freq = new HashMap<>();

    private final InputStream is;

    private final int rowSize;

    public CorpusProcessor(final InputStream is,
        final int rowSize,
        final boolean countFreq) {
        this.is = is;
        this.rowSize = rowSize;
        this.countFreq = countFreq;
    }

    public CorpusProcessor(final String filename,
        final int rowSize,
        final boolean countFreq) throws FileNotFoundException {
        this(new FileInputStream(filename), rowSize, countFreq);
    }

    private void addWord(final Collection<String> coll,
        final String word) {

        if (coll != null) {
            coll.add(word);
        }
        if (countFreq) {
            final Double count = freq.get(word);
            if (count == null) {
                freq.put(word, 1.0);
            } else {
                freq.put(word, count + 1);
            }
        }
    }

    public Set<String> getDictSet() {

        return dictSet;
    }

    public Map<String, Double> getFreq() {

        return freq;
    }

    protected void processLine(final String lastLine) {

        tokenizeLine(lastLine, dictSet, false);
    }

    public void setDict(final Map<String, Double> dict) {

        this.dict = dict;
    }

    public void start() throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            String lastName = "";
            String lastLine = "";
            while ((line = br.readLine()) != null) {
                final String[] lineSplit = line.toLowerCase().split(" \\+\\+\\+\\$\\+\\+\\+ ", 5);
                if (lineSplit.length > 4) {
                    // join consecuitive lines from the same speaker
                    if (lineSplit[1].equals(lastName)) {
                        if (!lastLine.isEmpty()) {
                            // if the previous line doesn't end with a special symbol, append a comma and the current line
                            if (!SPECIALS.contains(lastLine.substring(lastLine.length() - 1))) {
                                lastLine += ",";
                            }
                            lastLine += " " + lineSplit[4];
                        } else {
                            lastLine = lineSplit[4];
                        }
                    } else {
                        if (lastLine.isEmpty()) {
                            lastLine = lineSplit[4];
                        } else {
                            processLine(lastLine);
                            lastLine = lineSplit[4];
                        }
                        lastName = lineSplit[1];
                    }
                }
            }
            processLine(lastLine);
        }
    }

    // here we not only split the words but also store punctuation marks
    protected void tokenizeLine(final String lastLine,
        final Collection<String> resultCollection,
        final boolean addSpecials) {

        final String[] words = lastLine.split("[ \t]");
        for (String word : words) {
            if (!word.isEmpty()) {
                boolean specialFound = true;
                while (specialFound && !word.isEmpty()) {
                    for (int i = 0; i < word.length(); ++i) {
                        final int idx = SPECIALS.indexOf(word.charAt(i));
                        specialFound = false;
                        if (idx >= 0) {
                            final String word1 = word.substring(0, i);
                            if (!word1.isEmpty()) {
                                addWord(resultCollection, word1);
                            }
                            if (addSpecials) {
                                addWord(resultCollection, String.valueOf(word.charAt(i)));
                            }
                            word = word.substring(i + 1);
                            specialFound = true;
                            break;
                        }
                    }
                }
                if (!word.isEmpty()) {
                    addWord(resultCollection, word);
                }
            }
        }
    }

    /**
     * Converts an iterable sequence of words to a list of indices. This will
     * never return {@code null} but may return an empty {@link java.util.List}.
     *
     * @param words
     *            sequence of words
     * @return list of indices.
     */
    protected final List<Double> wordsToIndexes(final Iterable<String> words) {

        int i = rowSize;
        final List<Double> wordIdxs = new LinkedList<>();
        for (final String word : words) {
            if (--i == 0) {
                break;
            }
            final Double wordIdx = dict.get(word);
            if (wordIdx != null) {
                wordIdxs.add(wordIdx);
            } else {
                wordIdxs.add(0.0);
            }
        }
        return wordIdxs;
    }

}
