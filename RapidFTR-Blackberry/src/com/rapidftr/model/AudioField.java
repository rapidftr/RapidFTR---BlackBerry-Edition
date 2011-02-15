package com.rapidftr.model;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

import com.rapidftr.utilities.AudioStore;
import com.rapidftr.utilities.Logger;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controls.AudioControl;
import com.rapidftr.controls.AudioRecordListener;
import com.rapidftr.screens.ManageChildScreen;

import java.io.IOException;

public class AudioField extends FormField implements AudioRecordListener{
	private Player player;
	private RecordControl rcontrol;
    private AudioStore audioStore;
	private byte[] data = null;
	private String location = "";
	protected static final String TYPE = "audio_upload_box";
	private VerticalFieldManager manager;
	private ManageChildScreen manageChildScreen;

	protected AudioField(String name) {
		super(name, "Audio", TYPE);
	}
	
	public String getValue() {
		return location;
	}

    public void setValue(String value) {
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
			audioStore = new AudioStore();
			rcontrol.setRecordStream(audioStore.getOutputStream());
			rcontrol.startRecord();
			player.start();
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
            closeAudioStore();
        }
	}

    private void closeAudioStore() {
        try {
            if (audioStore != null) {
                audioStore.close();
            }
        } catch (IOException e) {
            Logger.log("Error closing audio file:" + e.toString());
        }
    }

    public void stop() {
		try {
			rcontrol.commit();
            location = audioStore.getFilePath();
			player.close();
		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException(e.getMessage());
		} finally {
           closeAudioStore(); 
        }
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
