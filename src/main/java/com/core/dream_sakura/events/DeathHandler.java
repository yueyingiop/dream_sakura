package com.core.dream_sakura.events;

import java.util.Random;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.effect.RegistryEffect;
import com.core.dream_sakura.util.OtherHelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = dream_sakura.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathHandler {
    private static Random RANDOM = new Random();

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

    // 击杀生物获取经验逻辑
    @SubscribeEvent
    public static void GainExpEvent(LivingDeathEvent event) { 
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof Monster monster) {
            ItemStack haloStack = OtherHelper.getCurio(player, "halo", 0);
            if (haloStack.getItem() != Items.AIR) {
                int randomExp = RANDOM.nextInt(2, 17);

                if (monster.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("forge", "bosses")))) {
                    randomExp = RANDOM.nextInt(50, 201);
                }

                CompoundTag haloTag = haloStack.getOrCreateTag();
                int itemLevel = haloTag.getInt("level");
                int itemXp = haloTag.getInt("xp");
                int itemMaxXp = haloTag.getInt("maxXp");

                if (itemLevel >= 95) {
                    player.sendSystemMessage(Component.translatable("message.dream_sakura.halo_max_level"));
                    return;
                }
                
                int newXp = itemXp + randomExp;
                if (newXp >= itemMaxXp) {
                    itemLevel++;
                    itemMaxXp = (int) OtherHelper.expCalculate(itemLevel);
                    newXp = newXp - itemMaxXp;
                }

                haloTag.putInt("level", itemLevel);
                haloTag.putInt("xp", newXp);
                haloTag.putInt("maxXp", itemMaxXp);
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
