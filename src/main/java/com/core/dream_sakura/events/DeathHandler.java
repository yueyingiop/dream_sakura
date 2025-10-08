package com.core.dream_sakura.events;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.effect.RegistryEffect;
import com.core.dream_sakura.util.OtherHelper;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = dream_sakura.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathHandler {

    @SubscribeEvent
    public static void LifeStealEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.hasEffect(RegistryEffect.LIFE_STEAL_EFFECT.get())) {
                MobEffectInstance effect = player.getEffect(RegistryEffect.STRENGTHEN_EFFECT.get());
                if (effect == null) return;
                dream_sakura.LOGGER.info(effect.getAmplifier());
                float damageMultiplier = (float) (effect.getAmplifier() * 0.01F) + 1;
                float health = Math.min(player.getHealth() + player.getMaxHealth() * damageMultiplier, player.getMaxHealth());
                
                player.setHealth(health);
            }
        }
    }

    // 星野光环被动1吸血逻辑
    @SubscribeEvent
    public static void HoshinoHaloBEvent(LivingDeathEvent event) { 
        if (event.getSource().getEntity() instanceof Player player) {
            if (OtherHelper.getCuriosItem(player, "halo", "dream_sakura:hoshino_halo")) {
                float healAmount = player.getMaxHealth() * 0.05f;
                player.heal(healAmount);
            }
        }
    }
}
