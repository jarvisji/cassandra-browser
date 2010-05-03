package net.softsword;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.webex.jcsandra.CassandraClient;
import com.webex.jcsandra.impl.SimpleCassandraClientPool;

/**
 * Servlet implementation class CassandraBrowser
 */
public class CassandraBrowser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String CONST_CMD = "cmd";
	public static final String CMD_CONNECT = "connect";
	public static final String CMD_SHOW_CONFIGFILE = "showConfigFile";
	public static final String CMD_SHOW_CLUSTERNAME = "showClusterName";
	public static final String CMD_SHOW_KEYSPACES = "showKeyspaces";
	public static final String CMD_SHOW_COLUMNFAMILY = "showColumnFamily";
	public static final String CMD_HELP = "help";

	public static final String PARM_CONNECT_SEVR = "url";
	public static final String PARM_CONNECT_PORT = "port";

	private HttpServletRequest request;
	private HttpServletResponse response;
	private CassandraClient cli;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CassandraBrowser() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("init");
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		System.out.println("destory");
	}

	private void doAction() throws IOException {
		String strCmd = request.getParameter(CONST_CMD);
		String outStr = "Wrong cmd.";

		if (CMD_CONNECT.equalsIgnoreCase(strCmd)) {
			String serverUrl = request.getParameter(PARM_CONNECT_SEVR);
			String port = request.getParameter(PARM_CONNECT_PORT);
			outStr = doConnect(serverUrl, port);
		} else if (CMD_SHOW_CLUSTERNAME.equalsIgnoreCase(strCmd)) {
			outStr = getClusterName();
		} else if (CMD_SHOW_CONFIGFILE.equalsIgnoreCase(strCmd)) {
			outStr = StringEscapeUtils.escapeHtml(getConfigFile());
		}
		doOutPut(outStr);
	}

	private void doOutPut(String outStr) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		outStr = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /></head><body>"
				+ outStr + "</body></html>";
		out.print(outStr);
		out.close();
	}

	private String getKeyspaces() throws IOException {
		checkConnection();
		List<String> lstKs = cli.getKeyspaces();
		StringBuffer listboxHtml = new StringBuffer();
		listboxHtml
				.append("<select size='10' name='keyspaces' id='listKs' onclick='javascript: showColumnFamily(this.form, this);'>");
		for (String ksName : lstKs) {
			listboxHtml.append("<option value='" + ksName + "'>" + ksName
					+ "</option>");
		}
		listboxHtml.append("</select>");
		StringBuffer ret = getJSHeader4GetCondDoc();
		ret.append("theDoc.getElementById('divKs').innerHTML="
				+ listboxHtml.toString());
		ret.append("</script>");
		return ret.toString();
	}

	private StringBuffer getJSHeader4GetCondDoc() {
		StringBuffer sb = new StringBuffer("<script language=\"JavaScript\">");
		sb.append("var theDoc = window.parent.window.frames['cond'].document;");
		return sb;
	}

	private String getConfigFile() throws IOException {
		checkConnection();
		return "<pre>" + cli.getConfigFile() + "</pre>";
	}

	private String getClusterName() throws IOException {
		checkConnection();
		return cli.getClusterName();
	}

	/**
	 * Connect to server.
	 * 
	 * @param params
	 * @throws IOException
	 */
	private String doConnect(String serverUrl, String port) throws IOException {
		SimpleCassandraClientPool pool = new SimpleCassandraClientPool(
				serverUrl, Integer.valueOf(port));
		try {
			cli = pool.getClient();
		} catch (Exception e) {
			throw new IOException(e);
		}
		return getConnectMsg(serverUrl, port);
	}

	/**
	 * Generate javascript to print connect message on page.
	 * 
	 * @param serverUrl
	 * @param port
	 * @return
	 * @throws IOException
	 */
	private String getConnectMsg(String serverUrl, String port)
			throws IOException {
		String clusterName = getClusterName();
		String msg = "Connected to {0} on {1}:{2}";
		Object[] args = { clusterName, serverUrl, port };
		msg = MessageFormat.format(msg, args);

		StringBuffer outSb = this.getJSHeader4GetCondDoc();
		outSb.append("theDoc.getElementById('divConnectMsg').innerHTML=");
		outSb.append(msg);
		outSb.append("</script>");
		return outSb.toString();
	}

	private void checkConnection() throws IOException {
		if (cli == null) {
			throw new IOException("Servlet was reset, connect again.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		doAction();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		doAction();
	}
}
