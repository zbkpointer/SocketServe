package com.live.socketproject;

import java.util.HashSet;

public class TestHashSet extends HashSet<String>{

    public static void main(String[] args) {
        HashSet<String> hashSet = new HashSet<>();
        String[]  s1 = "1,".split(",",-1);
        hashSet.add("31,2,2,3");
        hashSet.add("21,2,2,3");
        hashSet.add("11,2,23,5");
        hashSet.add("1,3,4,5");
        hashSet.add("1,2,3,3");

        for (String s:hashSet
             ) {
            System.out.println(s);
        }

        System.out.println(s1.length);



        ToUpperTCPNonBlockServer.discoverHashSet.clear();
//        while(true){
//
//            System.out.println("年后");
//        }
    }
}
