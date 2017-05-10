package com.xiaode;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
	// write your code here
        VirtualDeploymentUnit vdu = VirtualDeploymentUnit.builder();
        vdu.addVNFType(VNF_TYPE.ROUTER,3);
        vdu.addVNFType(VNF_TYPE.SWITCH,4);
        SFC sfc = SFC.builder(Arrays.asList("Router","Router","Switch"));
        System.out.println(sfc.getSFCDescriptor().toString());
        Server s = Server.build(vdu);

    }
}