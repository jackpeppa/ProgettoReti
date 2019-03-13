/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.view.Editing;
import common.Configuration;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author Giacomo
 */
public class ChatThread implements Runnable{
    
    private InetAddress group;
    private Editing editing;
    private byte[] buffer;
    private MulticastSocket socket;
    
    public ChatThread(String addr, Editing editing)
    {
        try{
        this.group = InetAddress.getByName(addr);
        }catch(UnknownHostException ex){ex.printStackTrace();}
        this.editing = editing;
        
        try{
        socket=new MulticastSocket(Configuration.CLIENT_CHAT_PORT);
        socket.joinGroup(group);
        }catch(Exception ex)
        {
            try{
            socket.leaveGroup(group);
            socket.close();
            }catch(Exception ex2){ex2.printStackTrace();}
        }
        
    }

    @Override
    public void run() 
    {
        try{
            while(!(Thread.currentThread().isInterrupted()))
            {
                buffer=new byte[1024];
                DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
                socket.receive(packet);
                String msg=new String(packet.getData(),packet.getOffset(),packet.getLength());
                editing.appendChatTextArea(msg);
            }  
        }catch(Exception ex){ex.printStackTrace();}
        finally
        {
            try{
            socket.leaveGroup(group);
            socket.close();
            }catch(Exception ex){ex.printStackTrace();}
        }
      
    }
    
}
