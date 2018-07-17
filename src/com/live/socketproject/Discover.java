package com.live.socketproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class Discover implements Runnable{
    private HashSet<String>  discoverHashSet;


    public Discover(HashSet<String> hashSet) {
        this.discoverHashSet = hashSet;
    }



    @Override
    public void run() {
      //检索整个数据库(MAC地址，状态 0)
      //HashSet上的状态改为1
        MySQLDB.update(discoverHashSet);

        ToUpperTCPNonBlockServer.discoverHashSet.clear();//清除过去获取的值
    }



}
