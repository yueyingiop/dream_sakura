package com.core.dream_sakura.util;

import java.util.function.Supplier;

import org.lwjgl.glfw.GLFW;

import com.core.dream_sakura.Config;
import com.core.dream_sakura.skill.SkillBinding;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class RegistryActiveSkill {
    public static final Supplier<SkillBinding> Dream_Final_Skill = ()->{
        return new SkillBinding(
            GLFW.GLFW_KEY_K,
            "Dream Finale Skill", 
            Config.dreamFinaleCooldown, // 冷却120s
            "dream_finale",
            (player, stack)->{
                player.level().getEntitiesOfClass(
                    LivingEntity.class, 
                    player.getBoundingBox().inflate(36.0)
                ).forEach(entity -> {
                    if (entity != player) { // 排除玩家自身
                        entity.discard(); // 删除实体
                    }
                });
            }
        );
    };

    public static final Supplier<SkillBinding> ALS_1_Halo_Skill = () -> {
        return new SkillBinding(
            GLFW.GLFW_KEY_J,
            "ALS 1 Halo Skill", 
            32000, // 冷却32s
            "als_1_halo",
            (player, stack) -> {
                CompoundTag playerData = player.getPersistentData();
                CompoundTag itemData = stack.getOrCreateTag();
                CompoundTag tag = new CompoundTag();
                if (!itemData.contains("level")) {
                    itemData.putInt("level", 1);
                }
                int level = itemData.getInt("level");
                float ExtraDamageMultiplier = OtherHelper.calculate(4, 36, 95, level);
                tag.putFloat("ExtraDamageMultiplier", ExtraDamageMultiplier);
                tag.putBoolean("isTrue", true);

                if (playerData.contains("SkillData")) {
                    playerData.getCompound("SkillData").put("ALS1HaloData", tag);
                } else {
                    CompoundTag ALS1HaloData = new CompoundTag();
                    ALS1HaloData.put("ALS1HaloData", tag);
                    playerData.put("SkillData", ALS1HaloData);
                }
            }
        );
    };

    public static final Supplier<SkillBinding> Hoshino_Halo_Skill = () -> {
        return new SkillBinding(
            GLFW.GLFW_KEY_J,
            "Hoshino Halo Skill",
            28000,
            "hoshino_halo",
            (player, stack) -> {
                CompoundTag playerData = player.getPersistentData();
                CompoundTag itemData = stack.getOrCreateTag();
                CompoundTag tag = new CompoundTag();
                if (!itemData.contains("level")) {
                    itemData.putInt("level", 1);
                }
                int level = itemData.getInt("level");
                float ExtraDamageMultiplier = OtherHelper.calculate(4.8f, 17, 95, level);
                float ExtraShieldMultiplier = OtherHelper.calculate(0.25f, 9.5f, 95, level);
                tag.putFloat("ExtraDamageMultiplier", ExtraDamageMultiplier);
                tag.putInt("ExtraDamage", 4);
                tag.putLong("StartShieldTime", player.level().getGameTime());
                tag.putFloat("ExtraShieldMultiplier", 268 * (1 + ExtraShieldMultiplier));
                tag.putBoolean("ExtraDamageIsTrue", true);

                if (playerData.contains("SkillData")) {
                    playerData.getCompound("SkillData").put("HoshinoHaloData", tag);
                } else {
                    CompoundTag HoshinoHaloData = new CompoundTag();
                    HoshinoHaloData.put("HoshinoHaloData", tag);
                    playerData.put("SkillData", HoshinoHaloData);
                }
            }
        );
    };
}
