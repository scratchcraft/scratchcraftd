//package com.github.scratchcraftd;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.apache.logging.log4j.Logger;
//import org.eclipse.jetty.server.Handler;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.handler.DefaultHandler;
//import org.eclipse.jetty.server.handler.HandlerList;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.WebSocketListener;
//import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
//import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
//
//import javax.servlet.annotation.WebServlet;
//
//public class ScratchCraftDWs implements Runnable {
//
//    private static final Gson gson = new GsonBuilder().create();
//
//    private final int    port;
//    private final Logger log;
//
//    public ScratchCraftDWs(final int port, final Logger log) {
//        this.port = port;
//        this.log  = log;
//    }
//
//    @Override
//    public void run() {
//        final Server server = new Server(port);
//
//        ServletContextHandler websocketHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        websocketHandler.setContextPath("/");
//        websocketHandler.addServlet(Servlet.class, "/*");
//
//        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[] {
//            websocketHandler,
//            new DefaultHandler()
//        });
//        server.setHandler(handlers);
//
//        try {
//            server.start();
//        } catch (Throwable e) {
//            log.error("Failure to start scratchcraftd web socket server.", e);
//        }
//
//        server.dumpStdErr();
//
//        try {
//            server.join();
//        } catch (Throwable e) {
//            log.error("Failure waiting for scratchcraftd web socket server to stop.");
//        }
//    }
//
//    @WebServlet(name = "WebSocket Commands", urlPatterns = "/command")
//    public static class Servlet extends WebSocketServlet {
//
//        @Override
//        public void configure(WebSocketServletFactory factory) {
//            factory.getPolicy().setIdleTimeout(120_000);
//            factory.register(Listener.class);
//        }
//    }
//
//    public class Listener implements WebSocketListener {
//
//        @Override
//        public void onWebSocketClose(int statusCode, String reason) {
//            log.info("Session closed");
//        }
//
//        @Override
//        public void onWebSocketConnect(Session session) {
//            log.info("Session connected");
//        }
//
//        @Override
//        public void onWebSocketError(Throwable cause) {
//            log.error("Session error", cause);
//        }
//
//        @Override
//        public void onWebSocketBinary(byte[] payload, int offset, int len) {
//            log.info("Received binary message.");
//        }
//
//        @Override
//        public void onWebSocketText(String json) {
//            try {
//                final CommandOp commandOp = gson.fromJson(json, CommandOp.class);
//                System.out.println(commandOp);
//            } catch (Throwable e) {
//                log.error("error reading: " + json, e);
//            }
//        }
//    }
//}
