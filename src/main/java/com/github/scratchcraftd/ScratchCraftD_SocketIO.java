//package com.github.scratchcraftd;
//
//import com.corundumstudio.socketio.Configuration;
//import com.corundumstudio.socketio.SocketIONamespace;
//import com.corundumstudio.socketio.SocketIOServer;
//import org.apache.logging.log4j.Logger;
//
//public class ScratchCraftSocketD implements Runnable {
//
//    private final Logger mlog;
//    private final SocketIOServer server;
//
//    ScratchCraftSocketD(final int port, final Logger mlog) {
//        this.mlog = mlog;
//
//        Configuration config = new Configuration();
//        config.setHostname("0.0.0.0");
//        config.setPort(port);
//
//        server = new SocketIOServer(config);
//        final SocketIONamespace playerSocketNamespace = server.addNamespace("/player");
//        playerSocketNamespace.addEventListener(
//            "message",
//            CommandOp.class,
//            (client, op, ackRequest) ->
//                playerSocketNamespace.getBroadcastOperations().sendEvent("message", op));
//
//        final SocketIONamespace robotSockerNamespace = server.addNamespace("/robot");
//        robotSockerNamespace.addEventListener(
//            "message",
//            CommandOp.class,
//            (client, op, ackRequest) ->
//                robotSockerNamespace.getBroadcastOperations().sendEvent("message", op));
//    }
//
//    @Override
//    public void run() {
//        mlog.info("Starting :: ScratchCraft SocketIO Daemon");
//        this.server.start();
//    }
//
//    void stop() {
//        mlog.info("Stopping :: ScratchCraft SocketIO Daemon");
//        this.server.stop();
//    }
//}
