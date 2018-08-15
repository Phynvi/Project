package com.overload.draw.chatbox.hierarchy;

import java.awt.Dimension;

public interface HierarchyOption {

	public Dimension getDimension();
	
	public String getName();
	
	public int getShortcutKey();
	
	public String getDescription();
	
	public int[] getIndex();

	public HierarchyOption[] getOptions();
	
}
