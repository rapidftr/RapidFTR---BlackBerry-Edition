package com.rapidftr.controls;


import net.rim.blackberry.api.mail.SupportedAttachmentPart;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class AudioControl extends Field {

	private static final int WIDTH = 60;
	private static final int HEIGHT = 60;
	private State state;
	private final AudioRecordListener listener;

	public AudioControl(AudioRecordListener listener) {
		super(Field.FOCUSABLE);
		this.listener = listener;
		this.state=State.STOPPED;
	}

	protected void layout(int width, int height) {
		this.setExtent(WIDTH, HEIGHT);
	}

	protected boolean navigationClick(int status, int time){
		state=state.nextState(listener);
		this.invalidate();
		return true;
	}
	
	protected void paint(Graphics graphics) {
		state.drawControl(graphics);
	}

	public static abstract class State {
		public abstract void drawControl(Graphics graphics);
		public abstract State nextState(AudioRecordListener listener);
		public static Bitmap stop=Bitmap.getBitmapResource("res/audio_stop.png");
		public static Bitmap start=Bitmap.getBitmapResource("res/audio_start.png");
		public static final State PLAYING = new State() {
			public void drawControl(Graphics graphics) {
				graphics.drawBitmap(2,2,WIDTH,HEIGHT,stop, 0, 0);
			}

			public State nextState(AudioRecordListener listener) {
				listener.stop();
				return State.STOPPED;
			}
			
		};
		public static final State STOPPED = new State() {
			public void drawControl(Graphics graphics) {
				graphics.drawBitmap(2,2,WIDTH,HEIGHT,start, 0, 0);
			}

			public State nextState(AudioRecordListener listener) {
				return listener.start()?PLAYING:STOPPED;
			}
		};
	}
}

