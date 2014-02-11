package au.edu.unimelb.plantcell.gwtphylo.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Common baseclass to avoid code duplication
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
public abstract class AbstractHttpServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -73512843354097896L;
	
	/* data fields required to be able to serve something */
	public static final String FIELD_SUPERFAMILY = "superfamily";
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_TREE = "tree";
	
	private File base_folder;
	
	public AbstractHttpServlet(final File base_folder) {
		this.base_folder = base_folder;
	}
	
	protected File getBaseDirectory() {
		return base_folder;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Map<String,String> form_data = new HashMap<String,String>();
			form_data.put(FIELD_SUPERFAMILY, req.getParameter(FIELD_SUPERFAMILY));
			form_data.put(FIELD_CATEGORY, req.getParameter(FIELD_CATEGORY));
			form_data.put(FIELD_TREE, req.getParameter(FIELD_TREE));
			validateFormData(form_data);
			
			File            out = findFileToSend(form_data);
			FileInputStream rdr = new FileInputStream(out);
			
			ServletOutputStream stream = resp.getOutputStream();
		    initHttpHeaders(resp, out);
		    
		    resp.setContentLength((int) out.length());
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
			resp.setStatus(404);
		}
		
	}

	protected void validateFormData(Map<String, String> m) throws IOException {
		assert(m != null);
		
		if (!m.containsKey(FIELD_SUPERFAMILY) || !m.containsKey(FIELD_CATEGORY) || !m.containsKey(FIELD_TREE)) {
			throw new IOException("Missing form parameters: superfamily, category and tree expected!");
		}
		validate(m.get(FIELD_SUPERFAMILY), "^\\w+$");
		validate(m.get(FIELD_CATEGORY), "^\\w+$");
		validate(m.get(FIELD_TREE), "^\\S+$");
	}
	
	protected void validate(final String s, final String pattern_regexp) throws IOException {
		if (s == null || s.length() < 1)
			throw new IOException("No data supplied!");
		Pattern p = Pattern.compile(pattern_regexp);
		Matcher m = p.matcher(s);
		if (!m.matches())
			throw new IOException("Illegal content supplied!");
	}
	
	protected abstract void initHttpHeaders(final HttpServletResponse resp, final File file_to_send);
	
	/**
	 * Returns a {@link File} instance of the file to send to the client, otherwise throws an IOException denoting the problem
	 * @param form_data
	 * @return
	 * @throws IOException
	 */
	public File findFileToSend(Map<String,String> form_data) throws IOException {
		assert(form_data != null && form_data.size() > 0);
		
		File superfamily_folder = new File(getBaseDirectory(), form_data.get(FIELD_SUPERFAMILY));
		if (!superfamily_folder.isDirectory())
			throw new IOException("Invalid superfamily folder: "+superfamily_folder.getAbsolutePath());
		File category_folder = new File(superfamily_folder, form_data.get(FIELD_CATEGORY));
		if (!category_folder.isDirectory())
			throw new IOException("Invalid category folder: "+category_folder.getAbsolutePath());
		return new File(category_folder, form_data.get(FIELD_TREE));
	}
}
