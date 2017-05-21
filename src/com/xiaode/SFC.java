package com.xiaode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaode on 5/5/17.
 */
public class SFC {
    private ArrayList<VNF_TYPE> SFCDescriptor;
    private SFC(){}

    public static SFC builder(List<String> descriptor){
        SFC instance = new SFC();
        if(instance.init(descriptor)){
            return  instance;
        }
        return null;
    }

    public ArrayList<VNF_TYPE> getSFCDescriptor() {
        return SFCDescriptor;
    }


    private boolean init(List<String> descriptor){
        SFCDescriptor = new ArrayList<>();
        for ( String vnf: descriptor) {
            VNF_TYPE type = getType(vnf);
            SFCDescriptor.add(type);
        }
        return true;

    }

    private VNF_TYPE getType(String vnf){
        VNF_TYPE type = VNF_TYPE.DEFAULT;
        switch (vnf){
            case "Router":  type = VNF_TYPE.ROUTER; break;
            case "Switch":  type = VNF_TYPE.SWITCH; break;
            default: break;
        }
        return type;
    }
}
