package org.luvx.coding.common.net;

import com.github.phantomthief.util.MoreFunctions;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

public class NetUtils {

    public static Map<String, String> getHostInfo() {
        return MoreFunctions.catching(() -> {
            InetAddress ia = InetAddress.getLocalHost();
            String host = ia.getHostName(), ip = ia.getHostAddress();
            return Map.of("ip", ip, "host", host);
        });
    }

    public static boolean isPortAvailable(int port) {
        try {
            bindPort("0.0.0.0", port);
            bindPort(InetAddress.getLocalHost().getHostAddress(), port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void bindPort(String host, int port) throws Exception {
        try (Socket s = new Socket()) {
            s.bind(new InetSocketAddress(host, port));
        }
    }
}
