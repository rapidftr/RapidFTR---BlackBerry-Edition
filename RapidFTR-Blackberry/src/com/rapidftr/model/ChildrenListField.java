package com.rapidftr.model;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;

public class ChildrenListField extends ObjectListField {
	
	private int screenWidth;
	private int firstrowPostion;
	private int secondRowPosition;
	private Font titleFont;
	private Font rowFont;
	



	public ChildrenListField () {
    	screenWidth = Display.getWidth();
    	firstrowPostion = (screenWidth) - (screenWidth - 20);
    	secondRowPosition = (screenWidth)- (screenWidth - 40);
//    	try {
//    		FontFamily alphaSerifFamily = FontFamily.forName("BBMillbank");
//    		titleFont = alphaSerifFamily.getFont(Font.PLAIN,3,Ui.UNITS_mm);
//    		rowFont = alphaSerifFamily.getFont(Font.PLAIN,2,Ui.UNITS_mm);
//    	}
//    	catch (ClassNotFoundException e){
//    	}
    	Font defaultFont = Font.getDefault();
    	titleFont = defaultFont.derive(Font.PLAIN, 3, Ui.UNITS_mm);
    	rowFont = defaultFont.derive(Font.PLAIN, 3, Ui.UNITS_mm);
	}
	
    public void drawListRow ( ListField listField, Graphics graphics,int index, int y, int width) {
    	Child child = (Child) this.get(listField, index);
    	
    	listField.setRowHeight(this.titleFont.getHeight()*4);
    	
    	graphics.setFont(titleFont);
    	graphics.drawText((String) child.getField("name"),firstrowPostion, y, (DrawStyle.LEFT|DrawStyle.ELLIPSIS|DrawStyle.TOP), width);              

    	int yStartForText = y + (this.getFont()).getHeight() + 1;
    	graphics.setFont(rowFont);
    	graphics.drawText((String) child.getField("id"),secondRowPosition, yStartForText, (DrawStyle.LEFT|DrawStyle.ELLIPSIS|DrawStyle.TOP), width);

    	yStartForText = y + (this.getFont()).getHeight() + 1;
    	graphics.setFont(rowFont);
    	graphics.drawText((String) child.getField("last_known_location"),secondRowPosition, yStartForText, (DrawStyle.LEFT|DrawStyle.ELLIPSIS|DrawStyle.TOP), width);

    	yStartForText = yStartForText + (this.getFont()).getHeight() + 1;
    	graphics.setFont(rowFont);
    	graphics.drawText((String) child.getField("date_of_separation"),secondRowPosition, yStartForText, (DrawStyle.LEFT|DrawStyle.ELLIPSIS|DrawStyle.TOP), width);

    	yStartForText = yStartForText + (this.getFont()).getHeight() + 1;
        graphics.drawLine(0,(index* listField.getRowHeight()), width, (index* listField.getRowHeight()));
    }

}
