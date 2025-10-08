package com.core.dream_sakura.events;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.effect.RegistryEffect;
import com.core.dream_sakura.util.OtherHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = dream_sakura.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageHandler {
    // 强化buff
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        // 检查伤害来源是近战实体伤害
        if (!(event.getSource().getDirectEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity attacker = (LivingEntity) event.getSource().getDirectEntity();
        if (attacker == null) return;

        // 获取攻击者身上的"强化"效果
        if (attacker.hasEffect(RegistryEffect.STRENGTHEN_EFFECT.get())) {
            MobEffectInstance effect = attacker.getEffect(RegistryEffect.STRENGTHEN_EFFECT.get());
            if (effect == null) return;
            
            int amplifier = effect.getAmplifier();
            float originalDamage = event.getAmount();
            
            // 应用伤害计算公式: 伤害 = [((效果等级 + 1) * 20%) + 1] * 原伤害
            float damageMultiplier = (float) ((amplifier + 1) * 0.2 + 1.0);
            float newDamage = originalDamage * damageMultiplier;
            
            // 设置新的伤害值
            event.setAmount(newDamage);
        }
    }


    // 爱丽丝光环主动技能伤害逻辑
    @SubscribeEvent
    public static void ALSHaloAHurt(LivingHurtEvent event) { 

        DamageSource damageSource = event.getSource();
        // 检查伤害源是否为玩家
        if (!(damageSource.getEntity() instanceof Player player)) return;

        // 获取玩家的持久化数据
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains("SkillData")) return;
        // 获取技能数据
        CompoundTag skillData = playerData.getCompound("SkillData");
        if (!skillData.contains("ALS1HaloData")) return;

        
        CompoundTag als1HaloData = skillData.getCompound("ALS1HaloData");
        if (als1HaloData.getBoolean("isTrue")) {
            event.setAmount(event.getAmount() * (1 + als1HaloData.getFloat("ExtraDamageMultiplier")));
            als1HaloData.putBoolean("isTrue", false);
        }
    }

    // 爱丽丝光环被动技能伤害逻辑
    @SubscribeEvent
    public static void ALSHaloBHurt(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        // 检查伤害源是否为玩家
        if (!(damageSource.getEntity() instanceof Player player)) return;

        boolean isWearingHalo = OtherHelper.getCuriosItem(player, "halo", "dream_sakura:als_1_halo");
        if (!isWearingHalo) return;

        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains("SkillData")) {
            CompoundTag skillData = new CompoundTag();
            playerData.put("skillData", skillData);
        }

        CompoundTag skillData = playerData.getCompound("SkillData");
        if (!skillData.contains("ALS1HaloData")) {
            CompoundTag ALS1HaloData = new CompoundTag();
            ALS1HaloData.putBoolean("isTrue", false);
            ALS1HaloData.putInt("damageCount", 0);
            skillData.put("ALS1HaloData", ALS1HaloData);
        }

        CompoundTag als1HaloData = skillData.getCompound("ALS1HaloData");
        int damageCount = als1HaloData.getInt("damageCount");
        float hurt;

        if (!als1HaloData.contains("lastDamageTime")) {
            als1HaloData.putFloat("lastDamageTime", 0);
        }

        if ((player.level().getGameTime() - als1HaloData.getFloat("lastDamageTime")) < 20*25) {
            als1HaloData.putInt("damageCount", 0);
            return;
        }

        switch (damageCount) {
            case 0:
                hurt = event.getAmount() * 2.0f;
                als1HaloData.putInt("damageCount", damageCount+1);
                break;
            case 1:
                hurt = event.getAmount() * 4.0f;
                als1HaloData.putInt("damageCount", damageCount+1);
                break;
            case 2:
                hurt = event.getAmount();
                als1HaloData.putInt("damageCount", damageCount+1);
                als1HaloData.putLong("lastDamageTime", player.level().getGameTime());
                player.addEffect(new MobEffectInstance(RegistryEffect.STRENGTHEN_EFFECT.get(), 300, 2));
                break;
            default:
                hurt = event.getAmount();
                als1HaloData.putInt("damageCount", 0);
                break;
        }
        event.setAmount(hurt);
    }


    // 星野光环主动技能伤害逻辑
    @SubscribeEvent
    public static void HoshinoHaloAHurt(LivingHurtEvent event) { 

        DamageSource damageSource = event.getSource();
        // 检查伤害源是否为玩家
        if (!(damageSource.getEntity() instanceof Player player)) return;

        boolean isWearingHalo = OtherHelper.getCuriosItem(player, "halo", "dream_sakura:hoshino_halo");
        if (!isWearingHalo) return;
        
        // 获取玩家的持久化数据
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains("SkillData")) return;
        // 获取技能数据
        CompoundTag skillData = playerData.getCompound("SkillData");
        if (!skillData.contains("HoshinoHaloData")) return;

        
        CompoundTag HoshinoHaloData = skillData.getCompound("HoshinoHaloData");
        int extraDamage = HoshinoHaloData.getInt("ExtraDamage");
        if (extraDamage > 0) {
            event.setAmount(event.getAmount() * (1 + extraDamage));
            HoshinoHaloData.putInt("ExtraDamage", extraDamage - 1);
        }
    }

    // 星野光环主动技能护盾逻辑
    @SubscribeEvent
    public static void HoshinoShieldAHurt(LivingDamageEvent event) { 

        if (!(event.getEntity() instanceof Player player)) return;

        boolean isWearingHalo = OtherHelper.getCuriosItem(player, "halo", "dream_sakura:hoshino_halo");
        if (!isWearingHalo) return;

        // 获取玩家的持久化数据
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains("SkillData")) return;
        // 获取技能数据
        CompoundTag skillData = playerData.getCompound("SkillData");
        if (!skillData.contains("HoshinoHaloData")) return;

        
        CompoundTag HoshinoHaloData = skillData.getCompound("HoshinoHaloData");
        
        float shieldAmount = HoshinoHaloData.getFloat("ExtraShieldMultiplier") - event.getAmount();

        if (HoshinoHaloData.getBoolean("ExtraDamageIsTrue")) {
            if (shieldAmount >= 0) {
                event.setAmount(0);
                HoshinoHaloData.putFloat("ExtraShieldMultiplier", shieldAmount);
            } else {
                event.setAmount(-shieldAmount);
                HoshinoHaloData.putFloat("ExtraShieldMultiplier", 0);
            }
        }
    }
}
