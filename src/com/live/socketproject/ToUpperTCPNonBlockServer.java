package com.live.socketproject;

/**
 * Created by Administrator on 2018/6/11.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ToUpperTCPNonBlockServer {
    //服务器IP
    public static final String SERVER_IP =CatchIp.getAllLocalHostIP();

    //服务器端口号
    public static final int SERVER_PORT = 60000;

    public List<Byte> list=new ArrayList<Byte>();

    public String[] readMessageSplit=null;

   // public List<String> discoverList = new ArrayList<>();

    public long lastDiscoverTime = System.currentTimeMillis();

    private static final long TimeInterval = 10*1000;

    public static HashSet<String> discoverHashSet = new HashSet<>();


//    AlertInformation alertInformation = new AlertInformation();
    //请求终结字符串
   // public static final char REQUEST_END_CHAR = '#';



    public void startServer(String serverIP, int serverPort) throws IOException {


        //使用NIO需要用到ServerSocketChannel
        //开启NIO通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        //设置为非阻塞
        serverChannel.configureBlocking(false);

        ServerSocket ss = serverChannel.socket();
        //创建地址对象
        InetSocketAddress localAddr = new InetSocketAddress(serverIP, serverPort);

        //服务器绑定地址
        ss.bind(localAddr);

        //注册到selector，会调用ServerSocket的accept
        //用selector监听accept能否返回
        //当调用accept可以返回时，会得到通知
        //注意，是可以返回，还需要调用accept
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动成功");

        while (true) {


            if(System.currentTimeMillis() - lastDiscoverTime > TimeInterval){

                lastDiscoverTime = System.currentTimeMillis();
                new Thread(new Discover(discoverHashSet)).start();

            }
            //调用select，阻塞在这里，直到有注册的channel满足条件
            selector.select();

            //走到这里，有符合条件的channel
            //可以通过selector.selectedKeys().iterator()拿到符合条件的迭代器
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();


            //处理满足条件的keys
            while (keys.hasNext()) {
                //取出一个key并移除
                SelectionKey key = keys.next();
                keys.remove();
                try {
                    if (key.isAcceptable()) {
                        //有accept可以返回
                        //取得可以操作的channel
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();

                        //调用accept完成三次握手，返回与客户端可以通信的channel
                        SocketChannel channel = server.accept();
                        //System.out.println(channel.getRemoteAddress());

                        //将该channel置非阻塞
                        channel.configureBlocking(false);
                        //注册进selector，当可读或可写时将得到通知，select返回


                        channel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端连接成功");
                    } else {
                        if (key.isReadable()) {
                            //  System.out.println("检测");
                            //有channel可读,取出可读的channel
                            SocketChannel channel = (SocketChannel) key.channel();

                            //创建读取缓冲区,一次读取1024字节
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            buffer.clear();

                            channel.read(buffer);
                            //  System.out.println(buffer.position());
                            //  System.out.println(buffer.limit());

                            //锁住缓冲区，缓冲区使用的大小将固定
                            buffer.flip();//读出的内容大小锁定，否则是1024内容

                            while (buffer.hasRemaining()) {

                                byte b = buffer.get();
                                // System.out.println(b);
                                list.add(b);


                            }
                            while (!list.isEmpty()) {
                                //读取为String
                                String readMessage = new String(buffer.array());
                                readMessageSplit = readMessage.split(",", -1);
                                //  System.out.println(readMessageSplit[0] + "栋");
                                //  System.out.println(readMessageSplit[1] + "室");
                                //  System.out.println(readMessageSplit[2] + "警报类型");
                                //  System.out.println(readMessageSplit[3] + "级别");

                                if (readMessageSplit.length == 2){
                                    discoverHashSet.add(readMessageSplit[0]);

                                }else {
                                AlertInformation alertInformation=new AlertInformation();
                                alertInformation.setIdOfBuilding(readMessageSplit[0]+"栋");
                                alertInformation.setCellOfBuilding(readMessageSplit[1]+"单元");
                                alertInformation.setIdOfRoom(readMessageSplit[2]+"室");
                                alertInformation.setAlertCategory(MessageProcess.tranCategoryMessage(readMessageSplit[3]));
                                alertInformation.setAlertLevel(MessageProcess.tranLevelMessage(readMessageSplit[4]));
                                //报警信息存进数据库
                                MySQLDB.insert(alertInformation);
                               // System.out.println(alertInformation.getIdOfBuilding());
                                OpenExplorer.browse("http://localhost:8888/alertPrompt.php");
                                }
                                readMessageSplit=null;
                                list.clear();
                            }

                            //附加上buffer，供写出使用
                            key.attach(buffer);
                            key.interestOps(SelectionKey.OP_WRITE);

                        } else {
                            if (key.isWritable() & key.isValid()) {

                                //有channel可写,取出可写的channel
                                SocketChannel channel = (SocketChannel) key.channel();
                                //取出可读时设置的缓冲区
                                ByteBuffer buffer = (ByteBuffer) key.attachment();

                                //将缓冲区指针移动到缓冲区开始位置
                                buffer.rewind();

                                //清空缓冲区
                                buffer.clear();
                                // buffer.flip();

                                //写回数据
                                String ans = new String("Received Success!".getBytes(), "GBK");
                                // byte[] sendBytes = recv.toUpperCase().getBytes();

                                byte[] sendBytes = ans.getBytes();
                                channel.write(ByteBuffer.wrap(sendBytes));
                                //切换为读事件
                                key.interestOps(SelectionKey.OP_READ);

                            }
                        }
                    }
                } catch (IOException e) {
                    //当客户端Socket关闭时，会走到这里，清理资源
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {

        ToUpperTCPNonBlockServer server = new ToUpperTCPNonBlockServer();

        try {
            server.startServer(server.SERVER_IP, server.SERVER_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
