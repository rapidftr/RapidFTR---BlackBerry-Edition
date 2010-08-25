package com.rapidftr.utilities;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.RichTextField;

public class BoldRichTextField extends RichTextField{


	
   
	public BoldRichTextField(String text) {
		super(text);
		this.setFont(Font.getDefault().derive(Font.BOLD));
	}
	
	public static RichTextField getSemiBoldRichTextField(String normalText,String boldText)
	{ 
		int[] offset = new int[3];
        byte[] attribute = new byte[2];
        Font fonts[] = new Font[2];
        
        fonts[0] = Font.getDefault();
        fonts[1] = Font.getDefault().derive(Font.BOLD);
       
        offset[0] = 0;
        attribute[0] = 0;
        
        offset[1] = normalText.length();
	    attribute[1] = 1;
	    
	    offset[2] = normalText.length()+boldText.length()+1;
	    
	    return new RichTextField(normalText +" " +  boldText, offset, attribute, fonts,RichTextField.USE_TEXT_WIDTH);
	}
}
