package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * ymy
 * 2023/8/4 - 21 : 24
 **/

@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    private static Map<String, Session> sessionMap = new HashMap<>();

    /*
    * 连接建立成功时调用的方法
    * */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessionMap.put(sid, session);
        System.out.println("和\t" + sid + "\t建立连接");
    }


    /*
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("收到消息：" + message);
    }


    /*
     * 连接关闭调用的方法
     * @param sid
     * */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("关闭和\t" + sid + "\t的连接");
    }


    /*
    * 群发，服务端向所有连接在自己身上的客户端发送消息
    * @param message
    * */
    public void sendToAllClients(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
