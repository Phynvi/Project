package com.overload.draw.chatbox;

public interface ChatboxDrawing {
	
	public void draw(int chatboxWidth, int chatboxHeight, int offsetY);
	
	public boolean pressKey(int key);
	
}