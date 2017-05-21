package com.xiaode;

import sun.applet.resources.MsgAppletViewer;

import java.util.*;

/**
 * Created by xiaode on 5/5/17.
 */
public class Server {
    public enum VNF_STRATEGY{
        RANDOM,
        INTERLEAVE,
        PREFFER
    }

    private int nodes;
    private int coresPerNode;
    private VNF_STRATEGY deployStrategy = VNF_STRATEGY.INTERLEAVE;
    private ArrayList<ArrayList<CPU>> physicalMachineCPUs;
    private Map<Integer,VNF> runtimeVNFMap = new HashMap<>();
    private Map<VNF,CPU> runtimeBindMap = new HashMap<>();
    private List<VNF> runningVNFs = new ArrayList<>();
    private VirtualDeploymentUnit vdu;




    final int [][] latancyMatrix = new int[][]{
        {157,253,249,253},
        {246,158,245,246},
        {249,246,155,245},
        {251,247,246,155}
    };
    final int [][] bandwidthMatrix = new int[][]{
            {3190,2781,2775,2763},{3198,2764,2760,2746},{3187,2768,2761,2748},{3194,2758,2749,2732},{3188,2761,2760,2744},{3186,2750,2749,2733},{3190,2767,2768,2750},{3186,2757,2748,2709},
            {2739,3045,2766,2671},{2764,3149,2751,2723},{2763,3141,2717,2716},{2719,3148,2713,2701},{2722,3106,2679,2693},{2753,2998,2689,2706},{2710,3049,2730,2697},{2740,3153,2745,2735},
            {2749,2747,3163,2752},{2750,2756,3161,2748},{2742,2748,3153,2744},{2737,2729,3152,2741},{2737,2750,3155,2739},{2747,2734,3153,2742},{2747,2738,3156,27465},{2754,2759,3160,2750},
            {2739,2729,2746,3164},{2734,2739,2746,3165},{2725,2731,2740,3161},{2726,2733,2717,3157},{2724,2729,2739,3157},{2707,2733,2741,3159},{2734,2739,2747,3166},{2740,2739,2747,3167}
    };

    private Server(VirtualDeploymentUnit vdu_decriptor){
        this.vdu = vdu_decriptor;
    }

    public ArrayList<ArrayList<CPU>> getPhysicalMachine() {
        return physicalMachineCPUs;
    }

    public int[][] getBandwidthMatrix() {
        return bandwidthMatrix;
    }

    public int[][] getLatancyMatrix() {return latancyMatrix;}

    public static Server build(VirtualDeploymentUnit vdu_decriptor){
        return build(8,4,vdu_decriptor);
    }

    public static Server build(int cores, int nodes,VirtualDeploymentUnit vdu_decriptor){
        Server s = new Server(vdu_decriptor);
        s.init(nodes,cores);
        return s;
    }

    public void init(int nodes, int cores){

        this.coresPerNode = cores;
        this.nodes = nodes;

        physicalMachineCPUs = new ArrayList<>();
        for (int i =  0 ; i < nodes; i++){
            ArrayList<CPU> socket = new ArrayList<>();
            for (int j = 0 ; j < cores ; j++){
                socket.add(new CPU(j + i*cores));
            }
            physicalMachineCPUs.add(socket);
        }
        System.out.println("The machine inited with " + String.valueOf(physicalMachineCPUs.size()) +
                " nodes and " + String.valueOf(this.nodes * this.coresPerNode)+ " cores");
        deployVNfs(VNF_STRATEGY.RANDOM);
        //initMatrix(nodes,cores);
    }

    /**
     *
     * @param strategy
     */
    final public void deployVNfs(VNF_STRATEGY strategy){
        switch (strategy){
            case RANDOM:
                deployRandom();
                break;
            case INTERLEAVE:
                deployInterleave();
                break;
            case PREFFER:
                deployPreffer();
                break;
        }
    }

    /**
     *
     * @param sfc
     * @return
     */
    final public  Map<Integer,VNF> getOptimumMapping(SFC sfc){
        ArrayList<VNF_TYPE> descriptor = sfc.getSFCDescriptor();
        Map<Integer, VNF> result = new HashMap<>();

        Integer nextCpuId = -1;
        Integer preCpuId = -1;
        Integer curCpuId = -1;
        for (VNF_TYPE type:descriptor) {
            int optimalChose = Integer.MAX_VALUE;
            VNF nextVNF = null;
            for (VNF vnf : runningVNFs) {
                if (vnf.getType() == type && vnf.isIdle) {
                    //random pick the first vnf
                    if(curCpuId < 0) {
                        curCpuId = vnf.getVcpuNumber();
                        preCpuId = curCpuId;
                        vnf.setIdle(false);
                        result.put(curCpuId,vnf);
                        break;
                    }
                    curCpuId = vnf.getVcpuNumber();
                    if (latancyMatrix[preCpuId/8][curCpuId/8] < optimalChose){
                        optimalChose = latancyMatrix[preCpuId/8][curCpuId/8];
                        nextCpuId = curCpuId;
                        nextVNF = vnf;
                    }
                }
            }
            if(nextCpuId != -1) result.put(nextCpuId, nextVNF);
        }
        return result;
    }

    private void deployRandom(){
        int totalCores = coresPerNode * nodes;
        Random seed = new Random();
        for (VNF_TYPE vnf: this.vdu.getVDU_Descriptor().keySet()) {
            int amount = this.vdu.getVDU_Descriptor().get(vnf);
            for(int i = 0; i < amount; i++){
                VNF tempVNF = new VNF();
                tempVNF.setId(i);
                tempVNF.setType(vnf);
                int cpuNo;
                CPU cpu;
                do {
                    cpuNo = seed.nextInt(totalCores);
                     cpu = getCPUById(cpuNo);
                }while(this.runtimeVNFMap.containsKey(cpuNo));
                tempVNF.setVcpuNumber(cpuNo);
                this.runningVNFs.add(tempVNF);
                this.runtimeVNFMap.put(cpu.getId(),tempVNF);
            }
        }
        //init the binding map
//        for (CPU cpu :runtimeVNFMap.keySet()) {
//            runtimeBindMap.put(runtimeVNFMap.get(cpu), cpu);
//        }

        for (VNF vnf: this.runningVNFs){
            System.out.println("VNF: "+vnf.getType()+ vnf.getId() +" running on the CPU "+ vnf.getVcpuNumber());
        }
        for (Integer id : this.runtimeVNFMap.keySet()){
            System.out.println("VNF: "+this.runtimeVNFMap.get(id).getType()+this.runtimeVNFMap.get(id).getId()+" running on CPU:"+ id);
        }
    }

    private void deployInterleave(){

    }

    private void deployPreffer(){
    }

    private void initMatrix(int nodes, int cores){
        // should init from file
    }

    private CPU getCPUById(int id){
        if(id >= nodes * coresPerNode) throw new IllegalArgumentException();
        return this.physicalMachineCPUs.get(id/coresPerNode).get(id%nodes);
    }


}
