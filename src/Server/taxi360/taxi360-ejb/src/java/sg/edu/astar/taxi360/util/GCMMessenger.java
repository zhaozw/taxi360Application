/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.util;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Althaf
 */
public class GCMMessenger {

    private static final Sender sender;

    static {
        sender = new Sender(Resources.get("SENDER_ID"));
    }

    public static void sendMessage(String userMessage, String collapseKey, String regId) throws IOException {
        Message message = new Message.Builder()
                .collapseKey(collapseKey)
                .timeToLive(2 * 60 * 60) // message dies after two hours
                .delayWhileIdle(true)
                .addData(Resources.get("gcm_message_title"), userMessage)
                .build();
        sender.sendNoRetry(message, regId);

    }

    public static void sendBroadcast(String userMessage, String collapseKey, ArrayList<String> regIds) throws IOException {
        Message message = new Message.Builder()
                .collapseKey(collapseKey)
                .timeToLive(2 * 60 * 60) // message dies after two hours
                .delayWhileIdle(true)
                .addData(Resources.get("gcm_message_title"), userMessage)
                .build();
        sender.sendNoRetry(message, regIds);

    }

}
