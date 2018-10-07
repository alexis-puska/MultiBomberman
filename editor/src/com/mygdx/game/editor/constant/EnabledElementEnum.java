package com.mygdx.game.editor.constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum EnabledElementEnum implements Serializable{
    DECOR,
    DOOR,
    ENNEMIE,
    ITEM,
    LOCK,
    PICK,
    PLATFORM,
    RAYON,
    TELEPORTER,
    VORTEX;
    
    public static String[] getValues(){
        List<String> val = new ArrayList<>();
        val.add(null);
        for(EnabledElementEnum e : values()){
            val.add(e.name());
        }
        return val.toArray(new String[0]);
    }
}
