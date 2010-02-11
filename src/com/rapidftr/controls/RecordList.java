package com.rapidftr.controls;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordList extends ListField {
	// Array of managers, with length equal to the number of rows,
	// each of which lays out the fields in a row.
	private RowManager[] _rows;

	// Integer array with length equal to the number of columns that
	// specifies the width, in pixels, of each column.
	private int[] _columnWidths;

	// Integer array with length equal to the number of columns that
	// specifies a number of pixels that will be reserved as blank
	// space following each column.
	private int[] _horizontalPaddings;
	
	public void setModel(ChildRecordItem[] records) {
		Field[][] tableContent = new Field[records.length][];

		for (int curRecord = 0; curRecord < records.length; curRecord++) {

			tableContent[curRecord] = new Field[3];
			ChildRecordItem record = records[curRecord];

			EncodedImage encodedImage = Utilities.getEncodedImageFromBytes(record.getPhoto());
			
			tableContent[curRecord][0] = new BitmapField(Utilities
					.getScaledBitmap(encodedImage, 40));

			tableContent[curRecord][1] = new LabelField(record.getRecordId());
			tableContent[curRecord][1].setFont(Styles.getSecondaryFont());

			tableContent[curRecord][2] = new LabelField(record.getName());
			tableContent[curRecord][2].setFont(Styles.getDefaultFont());
		}

		int[] widths = { 40, 130, 160 };
		int[] paddings = { 5, 2, 2 };	
		
		initialize(tableContent, widths, paddings);
	}
	
	public RecordList() {	
	}
	
	public RecordList(ChildRecord[] records) {
		setModel(records);
	}
	
	public void initialize(Field[][] contents, int[] columnWidths,
			int[] horizontalPaddings) {
		int numRows = contents.length;

		this.setRowHeight(42);

		// Create a row manager for each row.
		_rows = new RowManager[numRows];
		for (int curRow = 0; curRow < numRows; curRow++) {
			_rows[curRow] = new RowManager(contents[curRow]);
		}

		// Store the layout data.
		_columnWidths = columnWidths;
		_horizontalPaddings = horizontalPaddings;

		// Configure this ListField to operate with TableListField semantics.
		setSize(numRows);
		setCallback(RENDERER);
	}

	// Calculates the horizontal position at which the indicated
	// column should begin, based on the column widths and paddings.
	private int getColumnStart(int col) {
		int columnStart = 0;
		for (int i = 0; i < col; i++) {
			columnStart += _columnWidths[i];
			columnStart += _horizontalPaddings[i];
		}
		return columnStart;
	}

	// Manager that lays out the fields of a table row horizontally,
	// within the columns of its enclosing TableListField.
	private class RowManager extends Manager {

		public RowManager(Field[] rowContents) {
			super(0);
			for (int col = 0; col < rowContents.length; col++) {
				add(rowContents[col]);
			}
		}

		public void drawRow(Graphics g, int x, int y, int width, int height) {
			// Arrange the cell fields within this row manager.
			layout(width, height);

			// layout(width, height);

			// Place this row manager within its enclosing list.
			setPosition(x, y);

			// Apply a translating/clipping transformation to the graphics
			// context so that this row paints in the right area.
			g.pushRegion(getExtent());

			// Paint this manager's controlled fields.
			subpaint(g);

			// Restore the graphics context.
			g.popContext();
		}

		protected void sublayout(int width, int height) {
			for (int col = 0; col < getFieldCount(); col++) {
				// Set the size and position of the current cell.
				Field curCellField = getField(col);

				layoutChild(curCellField, _columnWidths[col],
						getPreferredHeight());

				int y = (col == 0) ? 0 : 10;
				
				setPositionChild(curCellField, getColumnStart(col), y);
			}

			setExtent(getPreferredWidth(), getPreferredHeight());
		}

		// The preferred width of a row is defined by the list renderer.
		public int getPreferredWidth() {
			return RENDERER.getPreferredWidth(RecordList.this);
		}

		// The preferred height of a row is the "row height" as defined in the
		// enclosing list.
		public int getPreferredHeight() {
			return getRowHeight();
		}
	}

	// The ListFieldCallback object that renders the rows of a TableListField.
	private static final ListFieldCallback RENDERER = new ListFieldCallback() {
		// A TableListField's row is drawn by delegating to the TableRowManager
		// for that row.
		public void drawListRow(ListField listField, Graphics graphics,
				int index, int y, int width) {
			RecordList recordList = (RecordList) listField;

			RowManager rowManager = recordList._rows[index];
			// rowManager.drawRow(graphics, 0, y, width, tableListField
			// .getRowHeight());

			rowManager.drawRow(graphics, 0, y, width, 100);
		}

		// The preferred width of a table list field is the sum of the widths of
		// its columns.
		public int getPreferredWidth(ListField listField) {
			RecordList recordList = (RecordList) listField;
			int numColumns = recordList._columnWidths.length;
			return recordList.getColumnStart(numColumns);
		}

		// there is no meaningful "row object"
		public Object get(ListField listField, int index) {
			return null;
		}

		// prefix searching is not supported
		public int indexOfList(ListField listField, String prefix, int start) {
			return -1;
		}
	};
}
