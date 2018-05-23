package com.example.b17annni.fksake;

/**
 * Created by b17annni on 2018-05-23.
 */

public class Qte_class {
    private String name;
    private String location;
    private int height;

    public Qte_class (String inName, String inLocation, int inHeight){
        name=inName;
        location=inLocation;
        height=inHeight;
    }

    public Qte_class(String inName){
        name=inName;
        location="";
        height=-1;
    }

    public String myStrings (){return name;}
    public String info(){
        String str=name;
        str+=" : ";
        str+=location;
        str+="  ";
        return str;

    }

    public void setHeight(int newHeight){
        height=newHeight;
    }
    @Override
    public String toString() {
        return name;
    }
}
