/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import static View.Server.padLeft;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author n18dc
 */

public class test {
    
    public static void main(String[] args) throws SocketException, UnknownHostException {
           DatagramSocket ds  = new DatagramSocket(1502, InetAddress.getByName("127.0.0.1"));
           System.out.println(ds.getLocalAddress());
           ds.close();
           ds  = new DatagramSocket(1503, InetAddress.getByName("127.0.0.2"));
           System.out.println(ds.getLocalAddress());
           
    }
}
