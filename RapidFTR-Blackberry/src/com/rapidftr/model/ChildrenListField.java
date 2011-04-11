package com.rapidftr.model;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;

import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.datastore.Children;

public abstract class ChildrenListField extends ObjectListField{

	private int screenWidth;
	private int firstRowPosition;
	private int secondRowPosition;
	private Font titleFont;
	private Font rowFont;
	private Children children;
	private int selectedIndex = -1;

	public ChildrenListField() {
		super();
		Font defaultFont = Font.getDefault();
		titleFont = defaultFont.derive(Font.PLAIN, 3, Ui.UNITS_mm);
		rowFont = defaultFont.derive(Font.PLAIN, 3, Ui.UNITS_mm);

		screenWidth = Display.getWidth();
		firstRowPosition = (screenWidth)
				- (screenWidth - 4 - ((titleFont.getHeight() * 4) - 4));
		secondRowPosition = firstRowPosition + 20;
	}

	protected boolean navigationClick(int i, int i1) {
		if (this.getSelectedIndex() > -1) {
			Child child = getSelectedChild();
			if (child != null) {
				setSelectedChild();
				getViewChildController().viewChild(child);
			}
		}
		return false;
	}

	public void drawListRow(ListField listField, Graphics graphics, int index,
			int y, int width) {
		Object[] childImagePair = (Object[]) this.get(listField, index);
		Child child = (Child) childImagePair[0];
		Bitmap image = (Bitmap) childImagePair[1];

		drawStatusBox(listField, graphics, index, child);

		drawChildImage(graphics, listField, index, image);

		graphics.setColor(Color.BLACK);

		graphics.setFont(titleFont);
		
        // Takes 5 params 1:display text, 2:horizontal position, 
		// 3: vertical position, 4: flags, 5: text display width
        graphics.drawText((String) child.getField("name"), firstRowPosition, y,
				(DrawStyle.LEFT | DrawStyle.ELLIPSIS | DrawStyle.TOP),
				screenWidth - firstRowPosition - 4);

		int yStartForText = y + (this.getFont()).getHeight() + 1;
		drawFieldRow(graphics, width, child, yStartForText, "age");

		yStartForText = yStartForText + (this.getFont()).getHeight() + 1;
		drawFieldRow(graphics, width - secondRowPosition - 4, child, yStartForText,
				"last_known_location");

		yStartForText = yStartForText + (this.getFont()).getHeight() + 1;
		graphics.drawLine(0, (index * listField.getRowHeight()), width,
				(index * listField.getRowHeight()));

	}

	private void drawChildImage(Graphics graphics, ListField listField,
			int index, Bitmap image) {
		if (image == null) {
			return;
		}
		try {
			graphics.drawBitmap(2, ((index) * listField.getRowHeight() + 2),
					(listField.getRowHeight() - 4),
					(listField.getRowHeight() - 4), image, 0, 0);
		} catch (NullPointerException n) {
			return;
		}
	}

	private void drawFieldRow(Graphics graphics, int width, Child child,
			int yStartForText, String field) {
		graphics.setFont(rowFont);
		graphics.drawText((String) child.getField(field), secondRowPosition,
				yStartForText,
				(DrawStyle.LEFT | DrawStyle.ELLIPSIS | DrawStyle.TOP), width);
	}

	private void drawStatusBox(ListField listField, Graphics graphics,
			int index, Child child) {

		String childStatusString = child.childStatus().getStatusString();
		int boxHeight = getFont().getHeight() + 4;
		int boxWidth = getFont().getAdvance(childStatusString) + 4;
		int boxX = screenWidth - 10 - boxWidth;
		int boxY = ((index) * listField.getRowHeight()) + getFont().getHeight() + 2;

		graphics.setColor(child.childStatus().getStatusColor());
		graphics.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
		graphics.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

		graphics.setColor(16777215);
		graphics.setFont(rowFont);
		graphics.drawText(childStatusString, boxX, boxY + 2, (DrawStyle.HCENTER
				| DrawStyle.ELLIPSIS | DrawStyle.TOP), boxWidth);
	}

	public Child getSelectedChild() {
		setSelectedChild();
		return getChildAtIndex(this.selectedIndex);
	}

	private Child getChildAtIndex(int index) {
			Object[] selectedChildImagePair = (Object[]) this.get(this, index);
			return (Child) selectedChildImagePair[0];
	}

	public abstract ViewChildrenController getViewChildController();

	public void addChild() {
		if (insertPosition() >= this.getSize()
				&& getSelectedIndex() <= children.count()) {
			insert(insertPosition(), children
					.getImagePairAt(childIndexToBeFetched()));
		}
	}

	private int childIndexToBeFetched() {
		return getSelectedIndex() - 1;
	}

	private int insertPosition() {
		return getSelectedIndex() + 1;
	}

	public void displayChildren(Children children) {
		this.children = children;
		set(children.getChildrenAndImages());
		selectChild();
	}
	
	public void setSelectedChild(){
		this.selectedIndex = this.getSelectedIndex();
	}
	
	private void selectChild() {
		if(this.selectedIndex != -1){
			this.setSelectedIndex(this.selectedIndex);
		}	
	}

	
	public void refresh(){
		this.selectedIndex = -1;
	}

}
