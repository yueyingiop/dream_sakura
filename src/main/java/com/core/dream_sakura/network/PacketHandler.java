package com.core.dream_sakura.network;

import com.core.dream_sakura.dream_sakura;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(dream_sakura.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static int id = 0;
    
    public static void register() {
        INSTANCE.registerMessage(id++, 
            SkillTriggerPacket.class, 
            SkillTriggerPacket::encode, 
            SkillTriggerPacket::decode, 
            SkillTriggerPacket::handle);
        INSTANCE.registerMessage(id++, 
            PlayMusicPacket.class, 
            PlayMusicPacket::encode, 
            PlayMusicPacket::decode, 
            PlayMusicPacket::handle);
    }
    
    // 发送给服务端,用于处理客户端请求
    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    // 发送给所有跟踪实体的客户端,用于服务器端向客户端广播
    public static void sendToClientsTrackingEntity(Object packet, LivingEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), packet);
    }
}
