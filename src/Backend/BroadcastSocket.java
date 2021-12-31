/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import static View.Server.padLeft;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;

/**
 *
 * @author n18dc
 */
public class BroadcastSocket {
    public InetAddress ipSer;
    public byte[] receive_from_server;
    public byte[] send_to_server = new byte[256];
    public DatagramPacket din, dout;
    public DatagramSocket socket;
    public JTextPane txpClient;
    public static String padLeft(String s, int n) {
    return String.format("%" + n + "s", s);  
            }
    final int CENTER_FOR_TIME =90;
     String timeStamp;
    //tạo contrustor cho class
    //có thể truyền đối tượng hiển thị khi có giao diện text Pane, testfield, ...
    public BroadcastSocket(JTextPane txpClient) throws IOException{
        //tạo socket Multicast 1502 là port phía Client
        socket = new DatagramSocket();
        this.txpClient = txpClient;
        this.txpClient.setText("Waiting notifitions from server..." + "\n");
        //cài đặt ip cho Multicast
        ipSer = InetAddress.getByName("192.168.209.1");
        //thêm vào nhóm sẽ nhận được tin nhắn từ Server;s
        reg();
        receive();
    }
    
    public void reg() throws IOException{
        send_to_server = "1".getBytes();
        din = new DatagramPacket(send_to_server, send_to_server.length, ipSer, 8888);
        socket.send(din);
//                    receive_from_server = new byte[256];
//                    din = new DatagramPacket(receive_from_server, receive_from_server.length);
//                    socket.receive(din);
//                    String line = new String(din.getData(),0,din.getLength()).trim();
//                    line = line.substring(line.indexOf("_")+1);
//                    int p = Integer.parseInt(line.substring(0, line.indexOf("_")));
//                    ipSer = InetAddress.getByName(line.substring(line.indexOf("_")+1));
//                    socket.close();
//                    socket = new DatagramSocket(p, ipSer);
                    
    }
    //Sử dụng Thread để hiên thị dữ liệu nhận được từ server
    public void receive(){
        
        Thread th = new Thread(){
            
            public void run(){
               try {
                    
                    while(true){ 
                    receive_from_server = new byte[256];
                    din = new DatagramPacket(receive_from_server, receive_from_server.length);
                     
                    socket.receive(din);
                    String line = new String(din.getData(),0,din.getLength()).trim();
                     //kiểm tra chuỗi nhận từ server khác null thì hiển thị
                     if(line!=null){
                         //phần hiển thị này sẽ được tùy chỉnh khi có giao diện
                         timeStamp = new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());
                         txpClient.setText(txpClient.getText()+padLeft(timeStamp, CENTER_FOR_TIME)+"\n\n");
                            txpClient.setText(txpClient.getText()+ line+"\n\n");
                     }
                     
                }
                    } catch (IOException ex) {
                        System.out.println("Lỗi hệ thống");
                    }
            }
        };
        th.start();
    }
    //Bỏ Client khỏi group nhận được tin nhắn và đóng socket
    public void remove() throws IOException{
        //socket.leaveGroup(ipSer);
        socket.close();
    }
    
    
    
    
    
}
