package com.live.socketproject;

/**
 * Created by Administrator on 2018/6/11.
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ToUpperUDPServer {
    //服务器IP
    public static String SERVER_IP =CatchIp.getAllLocalHostIP();
    //服务器端口号
    public static final int SERVER_PORT = 60000;

    //最多处理1024个字符
    public static final int MAX_BYTES = 1024;

    //UDP使用DatagramSocket发送数据包
    private DatagramSocket serverSocket;

    /***
     * 启动服务器
     */
    public void startServer(String serverIp, int serverPort) {
        try {
            //创建DatagramSocket
            InetAddress serverAddr = InetAddress.getByName(serverIp);
            serverSocket = new DatagramSocket(serverPort, serverAddr);

            //创建接收数据的对象
            byte[] recvBuf = new byte[MAX_BYTES];
            DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);

            //死循环，一直运行服务器
            while (true) {
                //接收数据，会在这里阻塞，直到有数据到来
                try {
                    serverSocket.receive(recvPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());

                //拿到対端的ip和端口
                InetAddress clientAddr = recvPacket.getAddress();
                int clientPort = recvPacket.getPort();

                //回传数据
                String upperString = recvStr.toUpperCase();
                byte[] sendBuf = upperString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, clientAddr, clientPort);
                try {
                    serverSocket.send(sendPacket);
                    System.out.println("收到"+recvStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e2) {
            e2.printStackTrace();
        } finally {
            //记得关闭Socket
            if (null != serverSocket) {
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
    public static void main(String[] args) {

        ToUpperUDPServer server = new ToUpperUDPServer();
        server.startServer(SERVER_IP, SERVER_PORT);
    }
}
