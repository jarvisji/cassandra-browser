package net.softsword.cassandrabrowser.console;

public class ConsoleSessionState {
    public boolean timingOn = false;
    public String hostName; // cassandra server name
    public int thriftPort; // cassandra server's thrift port
    public boolean framed = false; // cassandra server's framed transport
}
