/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.softsword.cassandrabrowser.console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

// copy from org.apache.cassandra.cli.CliOptions
public class ConsoleOptions {

    private static Options options = null; // Info about command line options
    private CommandLine cmd = null; // Command Line arguments

    // Command line options
    private static final String HOST_OPTION = "host";
    private static final String PORT_OPTION = "port";
    private static final String FRAME_OPTION = "framed";

    // Default values for optional command line arguments
    private static final int DEFAULT_THRIFT_PORT = 9160;

    // Register the command line options and their properties (such as
    // whether they take an extra argument, etc.
    static {
        options = new Options();
        options.addOption(HOST_OPTION, true, "cassandra server's host name");
        options.addOption(PORT_OPTION, true, "cassandra server's thrift port");
        options.addOption(FRAME_OPTION, false,
            "cassandra server's framed transport");
    }

    private static void printUsage() {
        System.err.println("");
        System.err
            .println("Usage: cassandra-cli --host hostname [--port <portname>] [--framed]");
        System.err.println("");
    }

    public void processArgs(ConsoleSessionState css, String[] args) {
        CommandLineParser parser = new PosixParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            printUsage();
            e.printStackTrace();
            System.exit(1);
        }

        if (!cmd.hasOption(HOST_OPTION)) {
            // host name not specified in command line.
            // In this case, we don't implicitly connect at CLI startup. In this
            // case,
            // the user must use the "connect" CLI statement to connect.
            //
            css.hostName = null;

            // HelpFormatter formatter = new HelpFormatter();
            // formatter.printHelp("java com.facebook.infrastructure.cli.CliMain ",
            // options);
            // System.exit(1);
        } else {
            css.hostName = cmd.getOptionValue(HOST_OPTION);
        }

        // Look to see if frame has been specified
        if (cmd.hasOption(FRAME_OPTION)) {
            css.framed = true;
        }

        // Look for optional args.
        if (cmd.hasOption(PORT_OPTION)) {
            css.thriftPort = Integer.parseInt(cmd.getOptionValue(PORT_OPTION));
        } else {
            css.thriftPort = DEFAULT_THRIFT_PORT;
        }
    }
}
