package net.softsword.cassandrabrowser.impl;

import java.util.List;

import net.softsword.cassandrabrowser.CassandraAccessor;
import net.softsword.cassandrabrowser.Output;
import net.softsword.cassandrabrowser.console.CliCompiler;
import net.softsword.cassandrabrowser.console.CliParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.yosemite.jcsadra.CassandraClient;
import org.yosemite.jcsadra.impl.SimpleCassandraClientPool;

public class JavaClientAccessor implements CassandraAccessor {

    private SimpleCassandraClientPool pool;
    private CassandraClient client;
    private Output out;

    public JavaClientAccessor(Output out) {
        this.out = out;
    }

    public void executeCLIStmt(String stmt) throws Exception {
        CommonTree ast = null;

        ast = CliCompiler.compileQuery(stmt);

        switch (ast.getType()) {
        case CliParser.NODE_EXIT:
            cleanupAndExit();
            break;
        case CliParser.NODE_THRIFT_GET:
            // executeGet(ast);
            break;
        case CliParser.NODE_HELP:
            printCmdHelp();
            break;
        case CliParser.NODE_THRIFT_SET:
            // executeSet(ast);
            break;
        case CliParser.NODE_THRIFT_DEL:
            // executeDelete(ast);
            break;
        case CliParser.NODE_THRIFT_COUNT:
            // executeCount(ast);
            break;
        case CliParser.NODE_SHOW_CLUSTER_NAME:
            // executeShowProperty(ast, "cluster name");
            break;
        case CliParser.NODE_SHOW_CONFIG_FILE:
            // executeShowProperty(ast, "config file");
            break;
        case CliParser.NODE_SHOW_VERSION:
            // executeShowProperty(ast, "version");
            break;
        case CliParser.NODE_SHOW_TABLES:
            // executeShowTables(ast);
            break;
        case CliParser.NODE_DESCRIBE_TABLE:
            // executeDescribeTable(ast);
            break;
        case CliParser.NODE_CONNECT:
            executeConnect(ast);
            break;
        case CliParser.NODE_NO_OP:
            // comment lines come here; they are treated as no ops.
            break;
        default:
            out.getOut().println(
                "Invalid Statement (Type: " + ast.getType() + ")");
            break;
        }
    }

    private void printCmdHelp() {
        out.getOut().println("List of all CLI commands:");
        out
            .getOut()
            .println(
                "?                                                                  Same as help.");
        out
            .getOut()
            .println(
                "help                                                          Display this help.");
        out
            .getOut()
            .println(
                "connect <hostname>/<port>                             Connect to thrift service.");
        out
            .getOut()
            .println(
                "describe keyspace <keyspacename>                              Describe keyspace.");
        out
            .getOut()
            .println(
                "exit                                                                   Exit CLI.");
        out
            .getOut()
            .println(
                "quit                                                                   Exit CLI.");
        out
            .getOut()
            .println(
                "show config file                                Display contents of config file.");
        out
            .getOut()
            .println(
                "show cluster name                                          Display cluster name.");
        out
            .getOut()
            .println(
                "show keyspaces                                           Show list of keyspaces.");
        out
            .getOut()
            .println(
                "show version                                                Show server version.");
        out
            .getOut()
            .println(
                "get <ksp>.<cf>['<key>']                                  Get a slice of columns.");
        out
            .getOut()
            .println(
                "get <ksp>.<cf>['<key>']['<super>']                   Get a slice of sub columns.");
        out
            .getOut()
            .println(
                "get <ksp>.<cf>['<key>']['<col>']                             Get a column value.");
        out
            .getOut()
            .println(
                "get <ksp>.<cf>['<key>']['<super>']['<col>']              Get a sub column value.");
        out
            .getOut()
            .println(
                "set <ksp>.<cf>['<key>']['<col>'] = '<value>'                       Set a column.");
        out
            .getOut()
            .println(
                "set <ksp>.<cf>['<key>']['<super>']['<col>'] = '<value>'        Set a sub column.");
        out
            .getOut()
            .println(
                "del <ksp>.<cf>['<key>']                                           Delete record.");
        out
            .getOut()
            .println(
                "del <ksp>.<cf>['<key>']['<col>']                                  Delete column.");
        out
            .getOut()
            .println(
                "del <ksp>.<cf>['<key>']['<super>']['<col>']                   Delete sub column.");
        out
            .getOut()
            .println(
                "count <ksp>.<cf>['<key>']                               Count columns in record.");
        out
            .getOut()
            .println(
                "count <ksp>.<cf>['<key>']['<super>']            Count columns in a super column.");
    }

    private void cleanupAndExit() {
        disconnect();
        System.exit(0);
    }

    private void executeConnect(CommonTree ast) throws Exception {
        int portNumber = Integer.parseInt(ast.getChild(1).getText());
        Tree idList = ast.getChild(0);

        StringBuilder hostName = new StringBuilder();
        int idCount = idList.getChildCount();
        for (int idx = 0; idx < idCount; idx++) {
            hostName.append(idList.getChild(idx).getText());
        }

        // disconnect current connection, if any.
        // This is a no-op, if you aren't currently connected.
        disconnect();

        // now, connect to the newly specified host name and port
        connect(hostName.toString(), portNumber);
    }

    @Override
    public void connect(String url, int port) throws Exception {
        pool = new SimpleCassandraClientPool(url, port);
        client = pool.getClient();
        out.getOut().printf("Connected to %s/%d\n", url, port);
    }

    @Override
    public void connect(String url, String port, String name, String password)
        throws Exception {
        // TODO Auto-generated method stub

    }

    public void disconnect() {
        pool.close();
    }

    @Override
    public void listKeyspaces() throws Exception {
        List<String> retList = client.getKeyspaces();
        out.printKeyspaces(retList);
    }

}
