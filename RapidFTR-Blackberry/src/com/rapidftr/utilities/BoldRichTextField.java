package com.rapidftr.utilities;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.RichTextField;

public class BoldRichTextField extends RichTextField {

	public static RichTextField field;

	public BoldRichTextField(String text) {
		super(text, RichTextField.STATUS_MOVE_FOCUS_VERTICALLY);
		this.setFont(Font.getDefault().derive(Font.BOLD));
	}

	public BoldRichTextField(String text, int[] offsets, byte[] attributes,
			Font[] fonts, long style) {
		super(text, offsets, attributes, fonts, style);
	}

	public static RichTextField getSemiBoldRichTextField(String normalText,
			String boldText) {
		int[] offset = new int[3];
		byte[] attribute = new byte[2];
		Font fonts[] = new Font[2];

		fonts[0] = Font.getDefault();
		fonts[1] = Font.getDefault().derive(Font.BOLD);

		offset[0] = 0;
		attribute[0] = 0;

		offset[1] = normalText.length();
		attribute[1] = 1;

		offset[2] = normalText.length() + boldText.length() + 1;

		field = new BoldRichTextField(normalText + " " + boldText, offset,
				attribute, fonts, RichTextField.STATUS_MOVE_FOCUS_VERTICALLY);
		return field;
	}

}
