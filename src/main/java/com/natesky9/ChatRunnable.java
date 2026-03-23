package com.natesky9;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;

public class ChatRunnable implements Runnable {
    final String string;
    final Client client;
    public ChatRunnable(Client client, String string)
    {
        this.string = string;
        this.client = client;
    }
    @Override
    public void run() {
        client.addChatMessage(ChatMessageType.OBJECT_EXAMINE,"vitality",string,"");
    }
}
