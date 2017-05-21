package com.xiaode;

/**
 * Created by xiaode on 5/5/17.
 */
public class VNF {
    //the identity of the vnf
    int id;

    //the physical number this vnf has
    int vcpuNumber;

    // if the VNF is running for some SFC
    boolean isIdle  = true;

    //the type
    VNF_TYPE type;

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVcpuNumber() {
        return vcpuNumber;
    }

    public void setVcpuNumber(int vcpuNumber) {
        this.vcpuNumber = vcpuNumber;
    }

    public VNF_TYPE getType() {
        return type;
    }

    public void setType(VNF_TYPE type) {
        this.type = type;
    }




}


