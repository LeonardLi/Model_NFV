package com.xiaode;

public class Main {
    public static void main(String[] args) {
	// write your code here
        VirtualDeploymentUnit vdu = VirtualDeploymentUnit.builder();
        vdu.addVNFType(VNF_TYPE.ROUTER,3);
        vdu.addVNFType(VNF_TYPE.SWITCH,4);

        Server s = Server.build(vdu);

    }
}