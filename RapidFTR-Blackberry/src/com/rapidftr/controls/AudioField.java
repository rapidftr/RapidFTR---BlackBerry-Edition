package com.rapidftr.controls;

import java.io.IOException;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.form.FormField;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.utilities.AudioStore;
import com.rapidftr.utilities.Logger;

public class AudioField extends CustomField implements AudioRecordListener{
	private Player player;
	private RecordControl rcontrol;
    private AudioStore audioStore;
	private String location = null;
	protected static final String TYPE = "audio_upload_box";
	private LabelField locationField;
	
	public AudioField(FormField field) {
		add(new LabelField("Record Audio: "));
		HorizontalFieldManager recordControl = new HorizontalFieldManager();
		recordControl.add(new AudioControl(this));
		locationField = new LabelField("");
		recordControl.add(locationField);
		add(recordControl);
	}
	
	public boolean start() {
		if (null == this.location || confirmOverWriteAudio()) {
			record();
			return true;
		}
		return false;
	}
	
	private boolean confirmOverWriteAudio() {
        return Dialog.ask(Dialog.D_YES_NO,
                "This will overwrite previously recorded audio. Are you sure?") == Dialog.YES;
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
            closeAudioStore();
			throw new RuntimeException(e.getMessage());
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
    
    protected void onDisplay() {
    	Child child = getChild();
    	if(null != child){
    		String field = child.getField("recorded_audio");
    		String audioLocation = field;
    		audioLocation = audioLocation == null ? "" : audioLocation;
    		locationField.setText(audioLocation);
    	}
    	super.onDisplay();
    }

	private Child getChild() {
		return ((ManageChildScreen)getScreen()).getChild();
	}

}
