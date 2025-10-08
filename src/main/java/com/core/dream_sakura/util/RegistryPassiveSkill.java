package com.core.dream_sakura.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.core.dream_sakura.effect.RegistryEffect;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class RegistryPassiveSkill {
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("389b669a-4576-4bb2-9e87-ede652fbc7c9");
    private static final UUID CRIT_CHANCE_MODIFIER_UUID = UUID.fromString("88d03014-7dda-45b7-a7aa-a5add5902374");
    private static final UUID CRIT_DAMAGE_MODIFIER_UUID = UUID.fromString("3fed0d50-b6b4-4b9f-80b3-c2976e4c735b");
    private static final UUID LUCK_MODIFIER_UUID = UUID.fromString("389b669a-4576-4bb2-9e08-ede652fbc7c9");
    private static final UUID ATTACK_MODIFIER_UUID = UUID.fromString("6e2cb19d-471e-4adc-b39a-ed3c3dc29b67");
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("22ca548e-fb29-4d54-8901-77bd84445b9a");

    private static final UUID HOSHINO_ARMOR_MODIFIER_UUID = UUID.fromString("6031e4b6-a956-4b42-9368-743761b1431a");
    private static final UUID HOSHINO_DAMAGE_MODIFIER_UUID = UUID.fromString("554a983b-8ba9-4b4d-8db1-ebdbdbdbf391");

    public static final void Dream_Finale_Skill(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        EntityDataCleaner.cleanEntityData(entity); // 清除实体数据(不管用)

        // 每tick移除所有效果
        if (!entity.level().isClientSide()){
            // 检测正在活动的负面效果
            Collection<MobEffectInstance> activeEffects = entity.getActiveEffects();  // 获取所有效果
            List<MobEffect> effectsToRemove = new ArrayList<>();  // 负面效果列表
            for (MobEffectInstance effectInstance : activeEffects) {
                if (effectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    effectsToRemove.add(effectInstance.getEffect());  // 将负面效果添加到列表中
                }
            }

            // 移除负面效果
            for (MobEffect effect : effectsToRemove) {
                entity.removeEffect(effect);
            }

            // 添加飞行效果
            if(entity instanceof Player player){
                Abilities abilities = player.getAbilities();
                abilities.mayfly = true;
                abilities.invulnerable = true; // 无敌

                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    serverPlayer.onUpdateAbilities();
                }
                
            }
            
            // 生命值低于50%给予生命回复效果
            if (entity.getHealth() < (entity.getMaxHealth()/2)) {
                
                MobEffectInstance regeneration = new MobEffectInstance(
                    MobEffects.REGENERATION,  // 生命回复效果
                    100,             // 持续时间：100 ticks
                    9,              // 效果等级
                    false,          // 是否显示粒子
                    true,           // 是否显示图标
                    true            // 环境效果是否可见
                );
                entity.addEffect(regeneration);
            }
        }
    }

    public static final void ALS_1_Halo_Skill_2(SlotContext slotContext, ItemStack stack) {
        CompoundTag itemData = stack.getOrCreateTag();
        if (!itemData.contains("level")) {
            itemData.putInt("level", 1);
            itemData.putInt("lastExtraLevels", 0);
        }
        int level = itemData.getInt("level");
        int lastExtraLevels = itemData.getInt("lastExtraLevels");
        if (level != lastExtraLevels) {
            ListTag modifiersList;
            if (itemData.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
                modifiersList = itemData.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
            } else {
                modifiersList = new ListTag();
            }

            itemData.putInt("lastExtraLevels", level);
            OtherHelper.removeExistingModifier(modifiersList, DAMAGE_MODIFIER_UUID);
            OtherHelper.removeExistingModifier(modifiersList, CRIT_CHANCE_MODIFIER_UUID);
            OtherHelper.removeExistingModifier(modifiersList, CRIT_DAMAGE_MODIFIER_UUID);
            OtherHelper.addModifier(modifiersList, "minecraft:generic.attack_damage", level*0.12, 2, DAMAGE_MODIFIER_UUID, "halo");
            OtherHelper.addModifier(modifiersList, "attributeslib:crit_chance", level*0.001, 2, CRIT_CHANCE_MODIFIER_UUID, "halo");
            OtherHelper.addModifier(modifiersList, "attributeslib:crit_damage", level*0.1, 2, CRIT_DAMAGE_MODIFIER_UUID, "halo");
            itemData.put("CurioAttributeModifiers", modifiersList);
        }
    }

    public static final void Hoshino_Halo_Skill_0(SlotContext slotContext, ItemStack stack) { 
        if (slotContext.entity() instanceof Player player) { 
            CompoundTag playerData = player.getPersistentData();
            if (!playerData.contains("SkillData")) return;

            CompoundTag skillData = playerData.getCompound("SkillData");
            if (!skillData.contains("HoshinoHaloData")) return;

            CompoundTag hoshinoHaloData = skillData.getCompound("HoshinoHaloData");
            
            if (
                player.level().getGameTime() - hoshinoHaloData.getLong("StartShieldTime") >= 20*12 ||
                hoshinoHaloData.getInt("ExtraDamage") == 0 ||
                hoshinoHaloData.getFloat("ExtraShieldMultiplier") <= 0
            ) {
                hoshinoHaloData.putBoolean("ExtraDamageIsTrue", false);
            }
        }
    }

    public static final void Hoshino_Halo_Skill_1(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            CompoundTag itemData = stack.getOrCreateTag();
            if (!itemData.contains("level")) {
                itemData.putInt("level", 1);
                itemData.putInt("lastExtraLevels", 0);
                itemData.putLong("passiveTime", 0);
                itemData.putLong("passiveEffectTime", 0);
            }

            ListTag modifiersList;
            if (itemData.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
                modifiersList = itemData.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
            } else {
                modifiersList = new ListTag();
            }

            long passiveTime = itemData.getLong("passiveTime");
            long passiveEffectTime = itemData.getLong("passiveEffectTime");

            if (player.level().getGameTime() - passiveEffectTime >= 20*10) {
                OtherHelper.removeExistingModifier(modifiersList, HOSHINO_DAMAGE_MODIFIER_UUID);
                OtherHelper.removeExistingModifier(modifiersList, HOSHINO_ARMOR_MODIFIER_UUID);
            }

            if (player.level().getGameTime() - passiveTime < 20*20) {
                return;
            }

            int level = itemData.getInt("level");
            float maxHealth = player.getMaxHealth();
            float health = player.getHealth();
            float healthPercentage = health / maxHealth; // 获取当前生命值百分比
            OtherHelper.removeExistingModifier(modifiersList, HOSHINO_DAMAGE_MODIFIER_UUID);
            OtherHelper.removeExistingModifier(modifiersList, HOSHINO_ARMOR_MODIFIER_UUID);
            float attackDamageValue = OtherHelper.calculate(1.28f, 36.8f, 95, level);
            float armorValue = OtherHelper.calculate(2, 12.8f, 95, level);
            if (healthPercentage < 0.5f) {
                OtherHelper.addModifier(modifiersList, "minecraft:generic.attack_damage", attackDamageValue, 2, HOSHINO_DAMAGE_MODIFIER_UUID, "halo");
                OtherHelper.addModifier(modifiersList, "minecraft:generic.armor", armorValue, 2, HOSHINO_ARMOR_MODIFIER_UUID, "halo");
                long GameTime = player.level().getGameTime();
                itemData.putLong("passiveTime", GameTime);
                itemData.putLong("passiveEffectTime", GameTime);
            
                MobEffectInstance regenerationEffect = new MobEffectInstance(
                    RegistryEffect.PERCENT_REGENERATION_EFFECT.get(),
                    200,
                    level - 1,
                    false,
                    true,
                    true
                );

                player.addEffect(regenerationEffect);
            }
        }
    }

    public static final void Hoshino_Halo_Skill_2(SlotContext slotContext, ItemStack stack) { 
        CompoundTag itemData = stack.getOrCreateTag();
        if (!itemData.contains("level")) {
            itemData.putInt("level", 1);
            itemData.putInt("lastExtraLevels", 0);
        }
        int level = itemData.getInt("level");
        int lastExtraLevels = itemData.getInt("lastExtraLevels");
        if (level != lastExtraLevels) {
            ListTag modifiersList;
            if (itemData.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
                modifiersList = itemData.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
            } else {
                modifiersList = new ListTag();
            }

            itemData.putInt("lastExtraLevels", level);
            OtherHelper.removeExistingModifier(modifiersList, DAMAGE_MODIFIER_UUID);
            OtherHelper.removeExistingModifier(modifiersList, ARMOR_MODIFIER_UUID);
            OtherHelper.addModifier(modifiersList, "minecraft:generic.attack_damage", level*0.05, 2, DAMAGE_MODIFIER_UUID, "halo");
            OtherHelper.addModifier(modifiersList, "minecraft:generic.armor", level*0.1, 2, ARMOR_MODIFIER_UUID, "halo");
            itemData.put("CurioAttributeModifiers", modifiersList);
        }
    }

    public static final void Blessed_Encounter_Skill(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            int lastExtraLevels = tag.getInt("LastExtraLevels");
            int lastLevel = tag.getInt("LastLevel");
            
            int experienceLevel = player.experienceLevel;
            int extraLevels = Math.max(0, experienceLevel - 520);
            if (experienceLevel != lastLevel || extraLevels != lastExtraLevels) {
                var attributes = player.getAttributes();
                AttributeInstance luckAttribute = attributes.getInstance(Attributes.LUCK);
                // dream_sakura.LOGGER.info("[DEBUG] 幸运值 " + luckAttribute);
                if (luckAttribute != null) {
                    ListTag modifiersList;
                    if (tag.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
                        modifiersList = tag.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
                    } else {
                        modifiersList = new ListTag();
                    }

                    OtherHelper.removeExistingModifier(modifiersList, LUCK_MODIFIER_UUID); // 移除旧的幸运值修改器
                    OtherHelper.addModifier(modifiersList, "minecraft:generic.luck", extraLevels, 0, LUCK_MODIFIER_UUID, "ring");

                    tag.put("CurioAttributeModifiers", modifiersList);
                    
                    // 更新NBT中的存储值
                    tag.putInt("LastExtraLevels", extraLevels);
                    tag.putInt("LastLevel", experienceLevel);
                }
            }
        }
    }

    //#region Farewell技能相关
    public static final void Farewell_Skill(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            MinecraftServer server = player.getServer();
            int playerCount = server != null?server.getPlayerCount():1;
            Boolean isTrue = false;
            try {
                Path configPath = Paths.get("usercache.json");
                File file = configPath.toFile();
                JsonArray currentData = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonArray();
                isTrue = currentData.size() > 1;
            } catch (Exception e) {
                isTrue = false;
            }
            if (playerCount == 1 && isTrue) {
                double damageBonus = tag.getDouble("DamageBonus");
                if (player.level().getGameTime() % 20 == 0) {
                    damageBonus += 0.00001;
                    tag.putDouble("DamageBonus", damageBonus);
                    stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                    
                    // applyDamageBonus(player, damageBonus);
                    updateItemAttributeModifier(stack, damageBonus);
                    if (player.getRandom().nextFloat() < 0.025f) {
                        OtherHelper.applyRandomDebuff(player);
                    }
                }
            } else {
                ListTag modifiersList;
                if (tag.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
                    modifiersList = tag.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
                } else {
                    modifiersList = new ListTag();
                }
                OtherHelper.removeExistingModifier(modifiersList, ATTACK_MODIFIER_UUID);
            }
        }
    }

    private static void updateItemAttributeModifier(ItemStack stack, double damageBonus) {
        CompoundTag tag = stack.getOrCreateTag();
        
        // 创建或获取AttributeModifiers列表
        ListTag modifiersList;
        if (tag.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
            modifiersList = tag.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
        } else {
            modifiersList = new ListTag();
        }
        
        // 移除旧的修饰符（如果存在）
        OtherHelper.removeExistingModifier(modifiersList, ATTACK_MODIFIER_UUID);
        
        // 创建新的属性修饰符
        if (damageBonus > 0) {
            CompoundTag modifierTag = new CompoundTag();
            modifierTag.putString("AttributeName", "minecraft:generic.attack_damage");
            modifierTag.putDouble("Amount", damageBonus);
            modifierTag.putInt("Operation", 2);
            modifierTag.putIntArray("UUID", OtherHelper.uuidToIntArray(ATTACK_MODIFIER_UUID));
            modifierTag.putString("Slot", "ring"); // 使用curio槽位
            
            modifiersList.add(modifierTag);
        }
        
        // 将更新后的列表放回NBT
        tag.put("CurioAttributeModifiers", modifiersList);
        
        // 设置HideFlags以隐藏属性修饰符信息（可选）
        tag.putInt("HideFlags", tag.getInt("HideFlags") | 2); // 2表示隐藏属性修饰符信息
    }
    //#endregion
}
