package net.softsword.cassandrabrowser.impl;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import net.softsword.cassandrabrowser.Output;

public class ConsoleOutput implements Output {

    public InputStream in;
    public PrintStream out;
    public PrintStream err;

    public ConsoleOutput() {
        in = System.in;
        out = System.out;
        err = System.err;
    }

    @Override
    public void printKeyspaces(List<String> keyspaces) {
        for (String ks : keyspaces) {
            out.println(ks);
        }
        out.printf("%s keyspaces.\n", keyspaces.size());
    }

    @Override
    public PrintStream getOut() {
        return out;
    }
}
