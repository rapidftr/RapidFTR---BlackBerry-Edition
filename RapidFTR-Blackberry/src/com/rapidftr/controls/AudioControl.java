package com.rapidftr.controls;


import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;

public class AudioControl extends ButtonField {

	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;
	private State state;
	private final AudioRecordListener listener;

	public AudioControl(AudioRecordListener listener) {
		super(Field.FOCUSABLE | ButtonField.CONSUME_CLICK);
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
		public static final State PLAYING = new State() {
			public void drawControl(Graphics graphics) {
				graphics.setColor(Color.RED);
				graphics.fillRect(0, 0, WIDTH, HEIGHT);
			}

			public State nextState(AudioRecordListener listener) {
				listener.stop();
				return State.STOPPED;
			}
			
		};
		public static final State STOPPED = new State() {
			public void drawControl(Graphics graphics) {
				graphics.setColor(Color.GREEN);
				graphics.fillRoundRect(0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
			}

			public State nextState(AudioRecordListener listener) {
				return listener.start() ? PLAYING : STOPPED;
			}
		};
	}
}
