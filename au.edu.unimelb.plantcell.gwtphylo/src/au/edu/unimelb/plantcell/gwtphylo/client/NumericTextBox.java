package au.edu.unimelb.plantcell.gwtphylo.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Permits numeric input only up to a maximum of <code>getLength()</code> characters.
 * The box will be sized accordingly. Based on discussion at: http://stackoverflow.com/questions/8036561/allow-only-numbers-in-textbox-in-gwt
 * 
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 *
 */
public class NumericTextBox extends TextBox implements KeyPressHandler {

	public NumericTextBox() {
		super();
		setMaxLength(getLength());
		setVisibleLength(getMaxLength());
	}

	public NumericTextBox(int default_value) {
		this();
		setText(String.valueOf(default_value));
	}

	public int getLength() {
		return 6;
	}
	
	 @Override
     public void onKeyPress(KeyPressEvent event) {
         if (!Character.isDigit(event.getCharCode()) 
                 && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE){
             ((TextBox)event.getSource()).cancelKey();
         }
     }
	
}
