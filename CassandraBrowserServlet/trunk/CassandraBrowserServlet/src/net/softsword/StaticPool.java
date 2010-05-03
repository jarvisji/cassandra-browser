package net.softsword;
import java.io.IOException;

public class StaticPool {

    private static StaticPool instance = null;
    private static String url = "";

    private StaticPool() throws IOException {
        if (url.isEmpty()) {
            throw new IOException("Url is empty.");
        }

    }

    public static StaticPool getInstance(String _url) throws IOException {
        url = _url;
        return getInstance();
    }

    public static StaticPool getInstance() throws IOException {
        if (instance == null) {
            instance = new StaticPool();
        }
        return instance;
    }
}
