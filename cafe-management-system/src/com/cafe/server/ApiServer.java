package com.cafe.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ApiServer {

    public static void main(String[] args) throws Exception {
        String portEnv = System.getenv("PORT");
        int port = (portEnv != null) ? Integer.parseInt(portEnv) : 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        File webRoot = new File("web");
        if (!webRoot.exists()) {
            webRoot = new File("../web");
        }
        server.createContext("/", new StaticFileHandler(webRoot));

        //backend
        server.createContext("/api/products", new ProductHandler());
        server.createContext("/api/orders", new OrderHandler());
        server.createContext("/api/tables", new TableHandler());
        server.createContext("/api/reservations", new ReservationHandler());
        server.createContext("/api/ingredients", new IngredientHandler());
        server.createContext("/api/reports", new ReportHandler());
        server.createContext("/api/expenses", new ReportHandler());


        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("=========================================");
        System.out.println(" ระบบจัดการร้านคาเฟ่ เปิดใช้งานแล้ว!");
        System.out.println(" เปิดเบราว์เซอร์ไปที่: http://localhost:" + port);
        System.out.println(" กด Ctrl+C เพื่อหยุดเซิร์ฟเวอร์");
        System.out.println("=========================================");
    }
}
