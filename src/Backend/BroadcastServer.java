/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author n18dc
 */
public class BroadcastServer {
      //tạo server, và các biến cần sử dụng
         DatagramSocket server;
         byte[] send_to_client = new byte[265];
         byte[] receive_from_client  = new byte[256];
         DatagramPacket dout, din;
        //gán địa chỉ mặc định 
        InetAddress ipSer ;
        String temp;
        List<Integer> ports = new ArrayList<>();
        List<InetAddress> list = listAllBroadcastAddresses();
        //tạo đối tượng nhập liệu
       public BroadcastServer() throws SocketException, UnknownHostException, IOException{
           this.server = new DatagramSocket(8888);
           
           reg();
       }
        public void reg(){
            
           Thread th = new Thread(){
               public void run(){
                   try{
                       while(true){
                           din = new DatagramPacket(receive_from_client, receive_from_client.length);
                           server.receive(din);
                           String txt =  new String(din.getData(),0,din.getLength()).trim();
                           if(txt.equals("1")){
                               ports.add(din.getPort());
//                               list.add("127.0.0.2");
//                               String temp ="set_"+ ports.get(ports.size()-1) + "_"+list.get(list.size()-1);
//                               dout = new DatagramPacket(temp.getBytes(), temp.getBytes().length, InetAddress.getByName(list.get(list.size()-1)), ports.get(ports.size()-1));
                              // server.send(dout);
                           }
//                           else if(txt.equals("1")&&list.size()!=0){
//                               ports.add(din.getPort());
//                               int i = Integer.parseInt(list.get(list.size()-1).substring(list.get(list.size()-1).lastIndexOf(".")+1));
//                               i++;
//                               list.add("127.0.0."+String.valueOf(i));
//                               String temp ="set_"+ ports.get(ports.size()-1) + "_"+list.get(list.size()-1);
//                               dout = new DatagramPacket(temp.getBytes(), temp.getBytes().length, InetAddress.getByName(list.get(list.size()-1)), ports.get(ports.size()-1));
//                               server.send(dout);
//                           }
                           
                           
                       }
                   } catch (IOException ex) {
                       Logger.getLogger(BroadcastServer.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
           }; 
           th.start();
        }
        public void send(String a) throws IOException{
            send_to_client = a.getBytes();
           
            for(int i = 0; i<list.size();i++){
                for(int j = 0; j<ports.size();j++){
                    dout    = new DatagramPacket(send_to_client, send_to_client.length, list.get(i),ports.get(j));
                     server.send(dout);
                     
                }
                     
                     

            }

        }
        public void close(){
            server.close();
        }
        
        
        public static List<InetAddress> listAllBroadcastAddresses() throws SocketException 
    {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        
        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();
            
            if (networkInterface.isLoopback() || !networkInterface.isUp()) 
            {
                continue;
            }
            
            networkInterface.getInterfaceAddresses().stream()
                .map(a -> a.getBroadcast())
                .filter(Objects::nonNull)
                .forEach(broadcastList::add);
        }
        
        return broadcastList;
    }
}
