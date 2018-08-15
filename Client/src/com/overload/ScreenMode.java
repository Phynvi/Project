package com.overload;

public enum ScreenMode {
	
	FIXED(1, 765, 503, false, false,512,334), 
	RESIZABLE(2, 902, 702, true, false,902,702), 
	FULLSCREEN(3, 902, 702, false, true,902,702);

	private int numericalValue;

	private int width;

	private int height;

	private boolean resizable;

	private boolean undecorated;
	
	private int screenAreaWidth;

	private int screenAreaHeight;

	public int getScreenAreaWidth() {
		return screenAreaWidth;
	}

	public int getScreenAreaHeight() {
		return screenAreaHeight;
	}

	public int getNumericalValue() {
		return numericalValue;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isResizable() {
		return resizable;
	}

	public boolean isUndecorated() {
		return undecorated;
	}

	ScreenMode(int numericalValue, int width, int height, boolean resizable, boolean undecorated,int screenAreaWidth,int screenAreaHeight) {
		this.numericalValue = numericalValue;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
		this.undecorated = undecorated;
		this.screenAreaWidth = screenAreaWidth;
		this.screenAreaHeight = screenAreaHeight;  
	}

	public static ScreenMode getByName(String name) {
		return ScreenMode.valueOf(name.toUpperCase());
	}

}
