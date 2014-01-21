package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for returning the phyloxml of the chosen tree (see form parameters) to the caller
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 * 
 */
public class GetPhyloXMLServlet extends HttpServlet {
	/**
	 * where to read phyloxml files from...
	 */
	private final static File BASE_DIR = new File("c:/www/1kp");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -898375516378104977L;


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Map<String,String> form_data = new HashMap<String,String>();
			form_data.put("superfamily", req.getParameter("superfamily"));
			form_data.put("category", req.getParameter("category"));
			form_data.put("tree", req.getParameter("tree"));
			validateFormData(form_data);
			
			File superfamily_folder = new File(BASE_DIR, form_data.get("superfamily"));
			if (!superfamily_folder.isDirectory())
				throw new IOException("Invalid superfamily folder: "+superfamily_folder.getAbsolutePath());
			File category_folder = new File(superfamily_folder, form_data.get("category"));
			if (!category_folder.isDirectory())
				throw new IOException("Invalid category folder: "+category_folder.getAbsolutePath());
			File phyloxml = new File(category_folder, form_data.get("tree"));
			
			FileInputStream rdr = new FileInputStream(phyloxml);
			ServletOutputStream stream = null;

		    stream = resp.getOutputStream();
		    resp.setContentType("application/xml");
		    resp.addHeader("Content-Type", "application/xml");
		    resp.addHeader("Content-Disposition", "inline; filename=" + phyloxml.getName());
		    resp.setContentLength((int) phyloxml.length());
		    byte[] buf = new byte[128 * 1024];
		    int len;
		    while ((len = rdr.read(buf, 0, buf.length)) >= 0) {
			    stream.write(buf, 0, len);
		    }
		    stream.close();
			rdr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void validateFormData(Map<String, String> m) throws IOException {
		assert(m != null);
		
		if (!m.containsKey("superfamily") || !m.containsKey("category") || !m.containsKey("tree")) {
			throw new IOException("Missing form parameters: superfamily, category and tree expected!");
		}
	}
	
	
}
