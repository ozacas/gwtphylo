package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.unimelb.plantcell.gwtphylo.shared.ConfigurationConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
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
		 resp.addHeader("Content-Type", "application/html");
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
			
			String url     = GWT.getModuleBaseURL()+"phyloxml?"+params;
			String config  = GWT.getModuleBaseURL()+"_aptx_configuration_file.txt";
			PrintWriter pw = new PrintWriter(resp.getOutputStream());
			pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
			pw.println("<head>");
			pw.println("</head>");
			pw.println("<body>");
			pw.println("<h1>System Requirements</h1>"+
					"To view this tree in the browser, you must have Java 7 (or later) JRE installed into the browser."
					);
			pw.println("<h1> "+form_data.get(FIELD_SUPERFAMILY) + " > "+form_data.get(FIELD_CATEGORY) + " > " +
								form_data.get("FIELD_TREE") + "</h1>");
			pw.println("<applet archive=\"archaeopteryx_applets.jar\">");
				pw.println("code=\"org.forester.archaeopteryx.ArchaeopteryA.class\");");
				pw.println("name=\"ArchaeopteryxA\");");
				pw.println("width=\"1000\"");
				pw.println("height=\"1000\"");
				pw.println("alt=\"ArchaeopteryxA is not working (please check your java configuration!)\"");
				pw.println("<param name=\"url_of_tree_to_load\" value=\""+url+"\"");
				pw.println("<param name=\"config_file\""+config+"\"");
				pw.println("<param name=\"java_arguments\" value=\"-Xmx512m\"");
			pw.println("</applet>");
			pw.println("</body>");
			pw.println("</html>");
			pw.close();
		} catch (Exception e) {
			
		}
	}

	private String makeEncodedURLParams(final Map<String, String> form_data) {
		StringBuilder sb = new StringBuilder();
		for ( String k: form_data.keySet() ) {
	        String vx = URL.encodeQueryString( form_data.get(k));
	        if ( sb.length() > 0 ) {
	            sb.append("&");
	        }
	        sb.append(k).append("=").append(vx);
	    }

		return sb.toString();
	}
}
