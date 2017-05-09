package com.xiaode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leonard on 09/05/2017.
 */
public class VirtualDeploymentUnit {
    private Map<VNF_TYPE,Integer> VDU_Descriptor;

    private VirtualDeploymentUnit(){
        VDU_Descriptor = new HashMap<>();
    }
    public static VirtualDeploymentUnit builder(){
        return new VirtualDeploymentUnit();
    }

    public void addVNFType(VNF_TYPE type, int amount){
        this.VDU_Descriptor.put(type,amount);
    }
    public Map<VNF_TYPE, Integer> getVDU_Descriptor() {
        return VDU_Descriptor;
    }

    public void setVDU_Descriptor(Map<VNF_TYPE, Integer> VDU_Descriptor) {
        this.VDU_Descriptor = VDU_Descriptor;
    }
}
