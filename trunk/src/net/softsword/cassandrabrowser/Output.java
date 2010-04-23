package net.softsword.cassandrabrowser;

import java.io.PrintStream;
import java.util.List;

/**
 * Output the result to console, web page, etc.
 * 
 * @author foxfocus
 * 
 */
public interface Output {

    /**
     * Get output stream.
     * @return
     */
    PrintStream getOut();

    /**
     * Print keyspace name.
     * 
     * @param keyspaces
     */
    void printKeyspaces(List<String> keyspaces);

}
