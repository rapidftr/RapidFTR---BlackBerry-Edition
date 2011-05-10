package com.rapidftr.model;

import java.util.Enumeration;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.utilities.BoldRichTextField;
import com.rapidftr.utilities.ChildFieldIgnoreList;

public class Tab {
    private Form form;
    private Child child;
    private final String label;

    public Tab(String label, Form form, Child child) {
        this.form = form;
        this.child = child;
        this.label = label;
    }

    public void RenderOn(Manager renderingArea) {
        Enumeration fieldList = this.form.getFieldList().elements();
        while(fieldList.hasMoreElements()){
              FormField field = (FormField)fieldList.nextElement();
              String key = field.getName();
              child.updateField(key);
              String value = (String)child.getField(key);
              if (ChildFieldIgnoreList.isInIgnoreList(key)) {
				  continue;
			  }
              Field detail = null;
              if(value.trim().equals("")){
            	 detail = new LabelField(field.displayLabel()+" : ", LabelField.FOCUSABLE){
            		  protected void paint(Graphics graphics) {
            			graphics.setColor(Color.GRAY);
            			super.paint(graphics);
            		}
            	  };
              }else{
            	  detail = BoldRichTextField.getSemiBoldRichTextField(field.displayLabel()+" : ", value);
              }
              renderingArea.add(detail);
        	  renderingArea.add(new SeparatorField());
        }
    }
    
    public String getLabel() {
		return label;
	}
}
