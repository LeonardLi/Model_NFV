package com.xiaode;

/**
 * Created by xiaode on 5/5/17.
 */
public class CPU {
    int id;
    boolean isIdle;


    public CPU(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }
}
