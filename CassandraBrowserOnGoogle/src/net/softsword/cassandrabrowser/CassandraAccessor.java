package net.softsword.cassandrabrowser;


/**
 * The accessor may depends on different implementation. Cassandra java client,
 * thrift call, cassandra dao, etc.
 * 
 * @author foxfocus
 * 
 */
public interface CassandraAccessor {
    
    /**
     * Execute command.
     * @param stmt String. Command statement.
     * @throws Exception
     */
    void executeCLIStmt(String stmt) throws Exception;

    /**
     * Connect to Cassandra server
     * 
     * @url String. Cassandra server url.
     * @port int. Cassandra server port.
     * @throws Exception
     */
    void connect(String url, int port) throws Exception;

    /**
     * Connect to Cassandra server with authentication.
     * 
     * @param url
     *            String. Cassandra server url.
     * @param port
     *            String. Cassandra server port.
     * @param name
     *            String. Login user name.
     * @param password
     *            String. Login password.
     * @throws Exception
     */
    void connect(String url, String port, String name, String password)
        throws Exception;
    
    void disconnect();

    /**
     * List all of keyspaces.
     * 
     * @throws Exception
     */
    void listKeyspaces() throws Exception;

}
