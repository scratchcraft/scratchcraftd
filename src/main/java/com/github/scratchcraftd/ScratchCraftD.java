package com.github.scratchcraftd;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;

public class ScratchCraftD implements Runnable {

    private final int port;
    private final int numberOfQueueableConnections;
    private final Logger mlog;
    private final PlayerMoveController controller;

    private HttpServer server;

    ScratchCraftD(
        final int port, final int numberOfQueueableConnections,
        final PlayerMoveController controller, final Logger mlog) {
        this.port = port;
        this.numberOfQueueableConnections = numberOfQueueableConnections;
        this.controller = controller;
        this.mlog = mlog;
    }

    @Override
    public void run() {
        InetSocketAddress address = new InetSocketAddress(port);

        try {
            this.server = HttpServer.create(address, numberOfQueueableConnections);
            this.server.createContext(PlayerMoveForwardHandler.URL, new PlayerMoveForwardHandler(controller));
            this.server.createContext(PlayerMoveBackwardHandler.URL, new PlayerMoveBackwardHandler(controller));
            this.server.createContext(PlayerMoveLeftHandler.URL, new PlayerMoveLeftHandler(controller));
            this.server.createContext(PlayerMoveRightHandler.URL, new PlayerMoveRightHandler(controller));
            this.server.createContext(PlayerLookDownHandler.URL, new PlayerLookDownHandler(controller));
            this.server.createContext(PlayerLookUpHandler.URL, new PlayerLookUpHandler(controller));
            this.server.createContext(PlayerJumpHandler.URL, new PlayerJumpHandler(controller));
            this.server.createContext(PlayerPlaceBlockHandler.URL, new PlayerPlaceBlockHandler());
            this.server.createContext(PlayerMineBlockHandler.URL, new PlayerMineBlockHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.server.start();
        mlog.info("Starting :: ScratchCraft Daemon");
    }

    void stop() {
        this.server.stop(0);
        mlog.info("Stopping :: ScratchCraft Daemon");
    }

    static abstract class BaseHandler implements HttpHandler {

        void write(final HttpExchange http, final int statusCode, final String body) throws IOException {
            http.sendResponseHeaders(statusCode, body.length());
            OutputStream os = http.getResponseBody();
            os.write(body.getBytes());
            os.close();
        }
    }

    static class PlayerMoveForwardHandler extends BaseHandler {

        private static final String URL = "/playerMoveForward";

        private final PlayerMoveController controller;

        PlayerMoveForwardHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            final String query = http.getRequestURI().getQuery();

            final Matcher m = compile("steps=[0-9]+").matcher(query);
            if (m.find()) {
                final int steps = valueOf(m.group().split("=")[1]);

                controller.forward(steps);
                write(http, 200, format("Move player forward %d steps.", steps));
            } else {
                write(http, 400, "Must include the 'steps' parameter (integer type).");
            }
        }
    }

    static class PlayerMoveBackwardHandler extends BaseHandler {

        private static final String URL = "/playerMoveBackward";

        private final PlayerMoveController controller;

        PlayerMoveBackwardHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            final String query = http.getRequestURI().getQuery();

            final Matcher m = compile("steps=[0-9]+").matcher(query);
            if (m.find()) {
                final int steps = valueOf(m.group().split("=")[1]);

                controller.backward(steps);
                write(http, 200, format("Move player forward %d steps.", steps));
            } else {
                write(http, 400, "Must include the 'steps' parameter (integer type).");
            }
        }
    }

    static class PlayerMoveLeftHandler extends BaseHandler {

        private static final String URL = "/playerMoveLeft";

        private final PlayerMoveController controller;

        PlayerMoveLeftHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            final String query = http.getRequestURI().getQuery();

            final Matcher m = compile("steps=[0-9]+").matcher(query);
            if (m.find()) {
                final int steps = valueOf(m.group().split("=")[1]);

                controller.left(steps);
                write(http, 200, format("Move player left %d steps.", steps));
            } else {
                write(http, 400, "Must include the 'steps' parameter (integer type).");
            }
        }
    }

    static class PlayerMoveRightHandler extends BaseHandler {

        private static final String URL = "/playerMoveRight";

        private final PlayerMoveController controller;

        PlayerMoveRightHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            final String query = http.getRequestURI().getQuery();

            final Matcher m = compile("steps=[0-9]+").matcher(query);
            if (m.find()) {
                final int steps = valueOf(m.group().split("=")[1]);

                controller.right(steps);
                write(http, 200, format("Move player left %d steps.", steps));
            } else {
                write(http, 400, "Must include the 'steps' parameter (integer type).");
            }
        }
    }

    static class PlayerJumpHandler extends BaseHandler {

        private static final String URL = "/playerJump";

        private final PlayerMoveController controller;

        PlayerJumpHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            controller.getPlayer().jump();
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            write(http, 200, "Jumped.");
        }
    }

    static class PlayerLookDownHandler extends BaseHandler {

        private static final String URL = "/playerLookDown";

        private final PlayerMoveController controller;

        PlayerLookDownHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            controller.setAngles(controller.getPlayer().rotationYaw, 0);
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            write(http, 200, "Looked down.");
        }
    }

    static class PlayerLookUpHandler extends BaseHandler {

        private static final String URL = "/playerLookUp";

        private final PlayerMoveController controller;

        PlayerLookUpHandler(final PlayerMoveController controller) {
            this.controller = controller;
        }

        public void handle(HttpExchange http) throws IOException {
            controller.setAngles(controller.getPlayer().rotationYaw, 180);
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            write(http, 200, "Looked up.");
        }
    }

    static class PlayerPlaceBlockHandler extends BaseHandler {

        private static final String URL = "/playerPlaceBlock";

        public void handle(HttpExchange http) throws IOException {
            final String query = http.getRequestURI().getQuery();

            final Matcher m = compile("block=[a-z_]+").matcher(query);
            if (m.find()) {
                final String blockName = String.valueOf(m.group().split("=")[1]);

                final Block block = Block.REGISTRY.getObject(new ResourceLocation(blockName));
                final BlockPos blockLookingAt = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
                final IBlockState defaultState = block.getDefaultState();
                Minecraft.getMinecraft().world.setBlockState(blockLookingAt, defaultState);
                write(http, 200, format("Set block at [%d, %d, %d] to '%s'.",
                    blockLookingAt.getX(), blockLookingAt.getY(), blockLookingAt.getZ(),
                    blockName));
            } else {
                write(http, 400, "Must include the 'block' parameter (string type).");
            }
        }
    }

    static class PlayerMineBlockHandler extends BaseHandler {

        private static final String URL = "/playerMineBlock";

        public void handle(HttpExchange http) throws IOException {
            final BlockPos blockLookingAt = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
            final IBlockState defaultState = Blocks.AIR.getDefaultState();
            Minecraft.getMinecraft().world.setBlockState(blockLookingAt, defaultState);
            write(http, 200, format("Set block at [%d, %d, %d] to AIR.",
                blockLookingAt.getX(), blockLookingAt.getY(), blockLookingAt.getZ()));
        }
    }
}
