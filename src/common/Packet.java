/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Giacomo
 */

public class Packet 
{
    private JSONObject jsonObj;
    
    public Packet(typePack tipo)
    {
        jsonObj = new JSONObject();
        jsonObj.put("type", tipo.toString());
    }
    
    public Packet()
    {
        jsonObj = new JSONObject();
        jsonObj.put("type", (typePack.DEFAULT).toString());
    }
    
    public Packet(JSONObject obj)
    {
        this.jsonObj = obj;
    }
    
    public void setType(typePack tipo)
    {
        jsonObj.put("type", tipo.toString());
    }
    
    public typePack getType()
    {
        return typePack.FromString((String)jsonObj.get("type"));
    }
    
    public void addCampo(String key, String value)
    {
        jsonObj.put(key, value);
    }
    
    public String getCampo(String key)
    {
        return (String)jsonObj.get(key);
                 
    }
    
    private static JSONObject fromStringToJson(String s)
    {
        JSONObject res=null;
        JSONParser parser = new JSONParser();
        try {
            res= (JSONObject)parser.parse(s);
        } catch (ParseException ex) {ex.printStackTrace();}
        
        return res;
    }
    
    
    
    public void readPacket(DataInputStream stream) throws IOException
    {
        
        int len = stream.readInt();
        byte[] buf = new byte[len];
        stream.readFully(buf);
        String s = new String(buf);
        this.jsonObj = fromStringToJson(s);
    }
    
    public void writePacket(DataOutputStream stream) throws IOException
    {
        String s = this.jsonObj.toJSONString();
        byte[] payload = s.getBytes();
        int len = payload.length;
        stream.writeInt(len);
        stream.write(payload);
        stream.flush();
    }
    
    
    
}
