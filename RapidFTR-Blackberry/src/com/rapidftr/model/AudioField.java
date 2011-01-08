package com.rapidftr.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controls.AudioControl;
import com.rapidftr.controls.AudioRecordListener;
import com.rapidftr.screens.ManageChildScreen;

public class AudioField extends FormField implements AudioRecordListener{
	private static final String FORMAT_EXT = ".amr";
	private Player player;
	private RecordControl rcontrol;
	private ByteArrayOutputStream output = null;
	private byte[] data = null;
	private String location = "";
	protected static final String TYPE = "audio_upload_box";
	private static final String DIRECTORY_NAME = "file:///store/rapidftr/";
	private VerticalFieldManager manager;
	private ManageChildScreen manageChildScreen;

	protected AudioField(String name) {
		super(name, "Audio", TYPE);
	}
	
	public String getValue() {
		return location;
	}

    public void setValue(String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void initializeLayout(final ManageChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		manager.add(new LabelField("Record Audio: "));
		manager.add(new AudioControl(this));
		this.manageChildScreen =newChildScreen;
	}
	
	public boolean start() {
		if(data == null || manageChildScreen.confirmOverWriteAudio()){
			record();
			return true;
		}
		return false;
	}

	private void record() {
		try {
			player = Manager.createPlayer("capture://audio?encoding=amr");
			player.realize();
			rcontrol = (RecordControl) player.getControl("RecordControl");
			output = new ByteArrayOutputStream();
			rcontrol.setRecordStream(output);
			rcontrol.startRecord();
			player.start();
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void stop() {
		try {
			rcontrol.commit();
			data = output.toByteArray();
			output.close();
			player.close();
			writeAudio(data);
		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException(e.getMessage());
		}
	}


	private void writeAudio(byte [] data) throws IOException {
		createDirectoryIfNotExists();
		location=generateLocation();
		FileConnection fconn = (FileConnection) Connector.open(location, Connector.READ_WRITE);
        if (!fconn.exists())
                fconn.create();
        OutputStream os = fconn.openDataOutputStream();
        os.write(data);
		os.close();
		fconn.close();
	}

	private void createDirectoryIfNotExists() throws IOException {
		FileConnection directory = (FileConnection)Connector.open(DIRECTORY_NAME);
		if(!(directory).exists()){
			directory.mkdir();
		}
	}

	private String generateLocation() {
		return DIRECTORY_NAME+"audio"+new Date().getTime()+FORMAT_EXT;
	}

    public net.rim.device.api.ui.Manager getLayout() {
		return manager;
	}
	
	public static AudioField createdFormField(String name, String type) {
		if (type.equals(TYPE)) {
			return new AudioField(name);
		}
		return null;
	}
}
