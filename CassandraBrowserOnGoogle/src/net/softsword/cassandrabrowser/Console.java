package net.softsword.cassandrabrowser;

import jline.ConsoleReader;
import net.softsword.cassandrabrowser.console.CliCompleter;
import net.softsword.cassandrabrowser.impl.ConsoleOutput;
import net.softsword.cassandrabrowser.impl.JavaClientAccessor;

/**
 * The entry of CassandraBrowserConsole.
 * 
 * @author foxfocus
 * 
 */
public class Console {

    public final static String PROMPT = "cassandra";

    private static Output out = new ConsoleOutput();
    private static CassandraAccessor accessor = new JavaClientAccessor(out);
    private static CliCompleter completer = new CliCompleter();

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        printBanner();

        ConsoleReader reader = new ConsoleReader();
        reader.addCompletor(completer);
        reader.setBellEnabled(false);
        String line;
        while ((line = reader.readLine(PROMPT + "> ")) != null) {
            accessor.executeCLIStmt(line);
        }
    }

    private static void printBanner() {
        out.getOut().println("Welcome to cassandra browser console.\n");
        out.getOut().println(
            "Type 'help' or '?' for help. Type 'quit' or 'exit' to quit.");
    }
}
