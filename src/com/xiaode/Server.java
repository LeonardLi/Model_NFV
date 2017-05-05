package com.xiaode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaode on 5/5/17.
 */
public class Server {
    public enum VNF_STRATEGY{
        RANDOM,
        INTERLEAVE,
        PREF

    }

    private ArrayList<ArrayList<CPU>> physicalMachine;
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

    private Server(){}

    public ArrayList<ArrayList<CPU>> getPhysicalMachine() {
        return physicalMachine;
    }

    public int[][] getBandwidthMatrix() {
        return bandwidthMatrix;
    }

    public int[][] getLatancyMatrix() {

        return latancyMatrix;
    }

    public static Server build(){
        return build(4,8);

    }

    public static Server build(int cores, int nodes){
        Server s = new Server();
        s.init(nodes,cores);
        return s;
    }

    public void init(int nodes, int cores){
        physicalMachine = new ArrayList<>();
        for (int i =  0 ; i < nodes; i++){
            ArrayList<CPU> socket = new ArrayList<>();
            for (int j = 0 ; j < cores ; j++){
                socket.add(new CPU(j*(i+1)));
            }
            physicalMachine.add(socket);
        }
        System.out.println("The machine inited with " + String.valueOf(physicalMachine.size()) +
                " nodes and " + String.valueOf(physicalMachine.size()* physicalMachine.get(0).size())+ " cores");

        initMatrix(nodes,cores);
    }

    public void deployVNfs(){

    }


    private void initMatrix(int nodes, int cores){
        // should init from file
    }


}
