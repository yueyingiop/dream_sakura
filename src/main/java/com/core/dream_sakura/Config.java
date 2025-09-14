package com.core.dream_sakura;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// 创建配置文件
@Mod.EventBusSubscriber(modid = dream_sakura.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 添加 Dream Finale 技能冷却时间配置（单位：毫秒）
    private static final ForgeConfigSpec.IntValue DREAM_FINALE_COOLDOWN = BUILDER
            .comment("Cooldown time in millisecond for Dream Finale Skill")
            .defineInRange("dreamFinaleCooldown", 120000, 1, Integer.MAX_VALUE);
    

    // 饰品动画关闭列表
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> VISUAL_ANIMATION = BUILDER
        .comment("Visual animation for turning off fires and frostbite")
        .defineList("visualAnimation", Arrays.asList("dream_sakura:dream_finale{fire,ice}"), item -> item instanceof String);
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int dreamFinaleCooldown = 120000;
    public static List<String> visualAnimation = Arrays.asList("dream_sakura:dream_finale{fire,ice}");

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        // 从配置加载冷却时间
        dreamFinaleCooldown = DREAM_FINALE_COOLDOWN.get();
        visualAnimation =  new ArrayList<>(VISUAL_ANIMATION.get());

        //
    }
}
