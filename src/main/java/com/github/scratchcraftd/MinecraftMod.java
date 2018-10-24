package com.github.scratchcraftd;

import static net.minecraftforge.fml.common.Mod.Instance;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = "scratchcraftd",
    name = "ScratchCraft Daemon",
    useMetadata = true)
public class MinecraftMod {

    @Instance
    public static MinecraftMod instance;

    @SidedProxy(
        clientSide = "com.github.scratchcraftd.ClientProxy",
        serverSide = "com.github.scratchcraftd.CommonProxy")
    public static CommonProxy proxy;

    static Logger mlog;
    private ScratchCraftD scratchCraftD;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        mlog = event.getModLog();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        scratchCraftD = new ScratchCraftD(8080, 100, new PlayerMoveController(), mlog);
        final Thread serverThread = new Thread(scratchCraftD);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @EventHandler
    public void serverStopping(final FMLServerStoppingEvent event) {
        scratchCraftD.stop();
    }
}
