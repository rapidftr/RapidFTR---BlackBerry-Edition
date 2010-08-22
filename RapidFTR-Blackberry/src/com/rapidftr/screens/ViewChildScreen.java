package com.rapidftr.screens;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import com.rapidftr.controls.TitleField;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.model.Child;
import com.rapidftr.utilities.ImageUtility;

public class ViewChildScreen extends CustomScreen {

	Child child;
	
	
	public ViewChildScreen() {

	}

	public void setChild(Child child) {
		this.child = child;
		clearFields();
		add(new TitleField());
		add(new LabelField("Child Details"));
		 add(new SeparatorField());
		renderChildFields(child);
		
	}

	private void renderChildFields(Child child) {
		
		Hashtable data = child.getKeyMap();
		HorizontalFieldManager hmanager = new HorizontalFieldManager(Field.FIELD_VCENTER); 
		renderBitmap(data,hmanager);
				
		hmanager.add(new LabelField(data.get(new String("name"))));
		add(hmanager);
	    ObjectListField childInfo = new ObjectListField();
		String info[];
		RichTextField richField[] = new RichTextField[data.size()];
		info = new String[data.size()];
		int i=0;
		for (Enumeration keyList = data.keys(); keyList.hasMoreElements();) {
			Object key = keyList.nextElement();
			Object value = data.get(key);
			//info[i++]=key + " : " + value;
			richField[i++] = new RichTextField(key + ":" + value);
			//add(new SeparatorField());
			add(richField[i-1]);
			
		}
		childInfo.set(info);
		

	}

	private void renderBitmap(Hashtable data,HorizontalFieldManager hmanager) {
		hmanager.setMargin(10, 10, 10, 10);
		String ImagePath = "res/default.jpg";

		Bitmap image =  Bitmap.getBitmapResource(ImagePath );
		
		
		if(ImagePath==null || ImagePath.equals(""))
		{
			ImagePath = "res/default.jpg";
		}
		else
		{
			ImagePath = "file://" + (String) data.get(new String("current_photo_key"));
			FileConnection fconn;
	
			try 
				{
					fconn = (FileConnection)Connector.open(ImagePath, Connector.READ);
					if (fconn.exists()) 
					  {           
							byte[] imageBytes = new byte[(int) fconn.fileSize()];
							InputStream inStream = fconn.openInputStream();
							inStream.read(imageBytes);
							inStream.close();
							EncodedImage eimg = EncodedImage.createEncodedImage(imageBytes, 0, (int) fconn.fileSize());
							image = eimg.getBitmap();
							fconn.close();

					  }
					  
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				} 
				 
		}
		// BitmapField			
		
		Bitmap resize = ImageUtility.resizeBitmap(image, 70,70);	
		BitmapField bf = new BitmapField(resize, BitmapField.FOCUSABLE);
		
		hmanager.add(bf);
	
	}

	private void clearFields() {
		int fieldCount = this.getFieldCount();
		if (fieldCount > 0)
			this.deleteRange(0, fieldCount);
	}

	public void setUp() {
		// TODO Auto-generated method stub

	}

	public void cleanUp() {
		// TODO Auto-generated method stub

	}
	
	protected void makeMenu(Menu menu, int instance) {
		MenuItem editChildMenu = new MenuItem("Edit Child Detail", 1, 1) {
			public void run() {
				controller.dispatcher().editChild(child);
			}
		};
		menu.add(editChildMenu);
	}
	

	
}
