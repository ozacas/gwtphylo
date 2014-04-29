package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.unimelb.plantcell.gwtphylo.shared.ConfigurationConstants;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This servlet is responsible for generating a suitable HTML page for the user
 * which loads the current tree (as specified by the input parameters) and loads archaeopteryx
 * with the built-in configuration file. The HTML page must also contain adequate instruction
 * for the user so that they correctly configure their browser.
 * 
 * Uses the phyloxml URL space to get the appropriate URL
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
@RemoteServiceRelativePath("applet")
public class AppletServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6235641410268504268L;

	public AppletServlet() {
		super(ConfigurationConstants.PHYLOXML_ROOT_FOLDER);
	}
	
	@Override
	protected void initHttpHeaders(HttpServletResponse resp, File file_to_send) {
		 resp.setContentType("application/html");
		 resp.addHeader("Content-Type", "content-disposition: inline; application/html");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			
			Map<String,String> form_data = new HashMap<String,String>();
			form_data.put(FIELD_SUPERFAMILY, req.getParameter(FIELD_SUPERFAMILY));
			form_data.put(FIELD_CATEGORY, req.getParameter(FIELD_CATEGORY));
			form_data.put(FIELD_TREE, req.getParameter(FIELD_TREE));
			validateFormData(form_data);
			
			String params  = makeEncodedURLParams(form_data);
			initHttpHeaders(resp, null);	// null == inline html not attachment download
			
			String base    = "http://127.0.0.1:8888/";		// MUST end with /
			String url     = base+"gwtphylo/phyloxml?"+params;
			String config  = base+"_aptx_configuration_file.txt";
			PrintWriter pw = new PrintWriter(resp.getOutputStream());
			pw.println("<!DOCTYPE HTML>");
			pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
			pw.println("<head>");
			pw.println("	<link type=\"text/css\" rel=\"stylesheet\" href=\"/index.css\">");
			pw.println("	<title>1kp AGP &amp; GT2 genes</title>");
			pw.println("</head>");
			pw.println("<body>");
			pw.println("<h1>System Requirements</h1>");
			pw.println("<p>To view this tree in the browser, you must have Java 7 (or later) JRE installed into the browser."
					+ "For security reasons, it is very important to keep your Java installation up-to-date. Check your installed programs.</p>"
			);
			pw.println("<p>A new window will be created, with Archaeopteryx running it is displaying the tree you have requested."
					+ " Please wait a few moments as it can take a while to download and start the software.... ");
			
			pw.println("<h1> "+form_data.get(FIELD_SUPERFAMILY) + " &gt; "+form_data.get(FIELD_CATEGORY) + " &gt; " +
								form_data.get(FIELD_TREE) + "</h1>");
			
			// the jar file which is served via this tag has been signed by the author, to avoid all the problems
			// with current java security. We load a configuration file, also part of this GWT module,
			// which displays all the necessary buttons to enable visualisation of the computed data in the various phyloxml trees
			
			pw.println("<object type=\"application/x-java-applet\" height=\"1000\" width=\"1000\" data=\""+base+"archaeopteryx_applets.jar\">");
			pw.println("	<param name=\"url_of_tree_to_load\" value=\""+url+"\" />");
			pw.println("	<param name=\"config_file\" value=\""+config+"\" />");
			pw.println("	<param name=\"code\" value=\"org.forester.archaeopteryx.ArchaeopteryxA.class\" />");
			pw.println("	<param name=\"archive\" value=\""+base+"archaeopteryx_applets.jar\" />");
			pw.println("	<param name=\"java_arguments\" value=\"-Xmx512m\" />");
			pw.println("	ArchaeopteryxA is not working (please check your java configuration!)");
			pw.println("</object>");
			
			/*pw.println("<applet archive=\""+base+"archaeopteryx_applets.jar\">");
				pw.println("code=\"\"");
				pw.println("codebase=\""+base+"\"");
				pw.println("name=\"ArchaeopteryxA\"");
				pw.println("width=\"1000\"");
				pw.println("height=\"1000\"");
				pw.println("alt=\"ArchaeopteryxA is not working (please check your java configuration!)\"");
				pw.println("<param name=\"url_of_tree_to_load\" value=\""+url+"\" />");
				pw.println("<param name=\"config_file\" value=\""+config+"\" />");
				pw.println("<param name=\"java_arguments\" value=\"-Xmx512m\" />");
			pw.println("</applet>");*/
			pw.println("</body>");
			pw.println("</html>");
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the base64 encoded representation of the the tree to display
	 * @param form_data
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private String makeEncodedURLParams(final Map<String, String> form_data) throws UnsupportedEncodingException {
		// implementation must match TreeViewModel.makeEncodedURLParams() or the servlet
		// must support both encodings
		StringBuilder sb = new StringBuilder();
		for (String k : form_data.keySet()) {
			String s = URLEncoder.encode(form_data.get(k), "UTF-8");
			if (sb.length() > 0) {
				sb.append('&');
			}
			sb.append(k);
			sb.append('=');
			sb.append(s);
		}
		return sb.toString();
	}
}
