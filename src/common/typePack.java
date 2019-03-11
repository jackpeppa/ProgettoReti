/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Giacomo
 */
public enum typePack 
{
    LOGIN,
    DEFAULT,
    LOGOUT,
    OP_ERR,
    CREATE,
    SHOW_DOC,
    SHOW_SEC,
    LIST,
    SHARE,
    EDIT,
    END_EDIT,
    OP_OK;
    public static typePack FromString(String s)
    {
        if(s.equals("LOGIN"))
            return LOGIN;
        else if(s.equals("LOGOUT"))
            return LOGOUT;
        else if(s.equals("DEFAULT"))
            return DEFAULT;   
        else if(s.equals("OP_ERR"))
            return OP_ERR;
        else if(s.equals("OP_OK"))
            return OP_OK;
        else if(s.equals("CREATE"))
            return CREATE;
        else if(s.equals("SHOW_SEC"))
            return SHOW_SEC;
        else if(s.equals("SHOW_DOC"))
            return SHOW_DOC;
        else if(s.equals("LIST"))
            return LIST;
        else if(s.equals("SHARE"))
            return SHARE;
        else if(s.equals("END_EDIT"))
            return END_EDIT;
        else if(s.equals("EDIT"))
            return EDIT;
        else
            return null;
    }
}

