package com.overload.io.packets.outgoing.impl;

import com.overload.Client;
import com.overload.draw.chatbox.hierarchy.HierarchyChatboxDrawing;
import com.overload.draw.chatbox.hierarchy.HierarchyOption;
import com.overload.io.packets.outgoing.OutgoingPacket;
import com.overload.io.packets.outgoing.PacketBuilder;

public class SendHierarchyPacket implements OutgoingPacket {
	
private HierarchyOption hierarchyOptions;
	
	private final HierarchyChatboxDrawing hierarchychatboxDrawing;
	
	public SendHierarchyPacket(HierarchyOption hierarchyOption, HierarchyChatboxDrawing hierarchychatboxDrawing1) {
		hierarchyOptions = hierarchyOption;
		hierarchychatboxDrawing = hierarchychatboxDrawing1;
	}
	
	@Override
	public PacketBuilder create() {
		PacketBuilder buf = new PacketBuilder(160);
		final int[] index = hierarchyOptions.getIndex();
		if (index.length == 2) {
			buf.putByte(index[0]);
			buf.putByte(index[1]);
		} else {
			Client.instance.sendMessage("Could not teleport to this area! Please report this to an administrator.", 0, "");
		}
		hierarchychatboxDrawing.selectedHierarchy = null;
		Client.chatboxDrawing = null;
		return buf;
	}
	

	

}
