package com.rapidftr.screens;

import java.io.IOException;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.controls.Button;

import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class SnapshotScreen extends Screen implements FieldChangeListener {

	private Button snapButton;
	private VideoControl videoControll;
	private Button fullScreenButton;

	public SnapshotScreen() {
		super();
		HorizontalFieldManager manager = new HorizontalFieldManager(
				FIELD_HCENTER);
		manager.add(new LabelField("Take Picture"));
		add(manager);
		add(new SeparatorField());
		try {
			Player player = Manager.createPlayer("capture://video");
			player.realize();
			player.prefetch();
			player.start();

			videoControll = (VideoControl) player.getControl("VideoControl");
			Field viewFinder = (Field) videoControll.initDisplayMode(
					VideoControl.USE_GUI_PRIMITIVE,
					"net.rim.device.api.ui.Field");
			add(viewFinder);
			add(new BlankSeparatorField(20));
			HorizontalFieldManager buttonManager = new HorizontalFieldManager(
					FIELD_HCENTER);

			snapButton = new Button("Snap");
			snapButton.setChangeListener(this);
			buttonManager.add(snapButton);
			
			fullScreenButton = new Button("Full Screen");
			buttonManager.add(fullScreenButton);
			fullScreenButton.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					if(videoControll!=null)
					{
						try {
							videoControll.setDisplayFullScreen(true);
						} catch (MediaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			add(buttonManager);
			videoControll.setVisible(true);
		} catch (IOException e) {

			Dialog.alert(e.toString());

		} catch (MediaException e) {

			Dialog.alert(e.toString());
		}

	}

	protected void makeContextMenu(ContextMenu contextMenu) {
		
		MenuItem snapMenu = new MenuItem("Snap", 1, 1) {
			
			public void run() {
				takePicture();
			}
		};
		contextMenu.addItem(snapMenu);
		MenuItem fullScreenMenuItem = new MenuItem("Full Screen",1,2) {
			
			public void run() {
				if(videoControll !=null)
				{
					try {
						videoControll.setDisplayFullScreen(true);
					} catch (MediaException e) {
						// TODO Auto-genera;
					}
				}
			}
		};
		contextMenu.addItem(fullScreenMenuItem);
		super.makeContextMenu(contextMenu);
	}
	

	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	public void setUp() {

	}

	private void takePicture()
	{
		
	}
	public void fieldChanged(Field field, int context) {
		if (field.equals(snapButton)) {
			takePicture();
		}

	}

}
