package com.core.dream_sakura.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class OtherHelper {
    // 计算数值(线性)
    public static float calculate(float minValue, float maxValue, int maxLevel, int level) {
        float basicValue = minValue;
        float coefficient = (maxValue - minValue) / (maxLevel - 1);
        return calculate(basicValue, coefficient, level);
    }

    public static float calculate(float basicValue, float coefficient, int level) {
        return coefficient * (level-1) + basicValue;
    }

    public static float expCalculate(int level) {
        float yOffset = 924.0f;
        float base = 0.95f;
        float molecule = 100.0f;

        float denominator = (float)Math.pow(base, level-1);
        return molecule / denominator + yOffset;
    }

    // 添加修饰符
    public static void addModifier(
        ListTag modifiersList, 
        String attributeName,
        double amount, 
        int operation,
        UUID uuid,
        String slot
    ) {
        CompoundTag modifierTag = new CompoundTag();
        modifierTag.putString("AttributeName", attributeName);
        modifierTag.putDouble("Amount", amount);
        modifierTag.putInt("Operation", operation);
        modifierTag.putIntArray("UUID", OtherHelper.uuidToIntArray(uuid));
        modifierTag.putString("Slot", slot);
        modifiersList.add(modifierTag);
    }

    // 移除已存在的修饰符
    public static void removeExistingModifier(ListTag modifiersList, UUID uuidToRemove) {
        int[] uuidArray = uuidToIntArray(uuidToRemove);
        
        for (int i = 0; i < modifiersList.size(); i++) {
            CompoundTag modifierTag = modifiersList.getCompound(i);
            if (modifierTag.contains("UUID") && 
                arraysEqual(modifierTag.getIntArray("UUID"), uuidArray)) {
                modifiersList.remove(i);
                break;
            }
        }
    }

    // 将UUID转换为int数组
    public static int[] uuidToIntArray(UUID uuid) {
        long mostSignificant = uuid.getMostSignificantBits();
        long leastSignificant = uuid.getLeastSignificantBits();
        return new int[] {
            (int)(mostSignificant >> 32),
            (int)mostSignificant,
            (int)(leastSignificant >> 32),
            (int)leastSignificant
        };
    }

    // 比较两个int数组是否相等
    public static boolean arraysEqual(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    // 随机效果
    public static void applyRandomDebuff(Player player) {
        Collection<MobEffect> allEffects = ForgeRegistries.MOB_EFFECTS.getValues(); // 获取所有效果
        // List<MobEffect> effects = new ArrayList<>(allEffects);
        List<MobEffect> effects = allEffects.stream()
            .filter(effect -> effect.getCategory() == MobEffectCategory.HARMFUL)
            .collect(Collectors.toList());

        if (!effects.isEmpty()) {
            MobEffect randomEffect = effects.get(player.getRandom().nextInt(effects.size()));
            int duration = (30 + player.getRandom().nextInt(31)) * 20; // 30-60秒
            int level = 1 + player.getRandom().nextInt(255); // 1-255级
            player.addEffect(new MobEffectInstance(randomEffect, duration, level));
        }
    }

    // 判断是否拥有指定Curios物品
    public static boolean getCuriosItem(LivingEntity entity, String curiosSlot, String itemId) {
        Optional<ICuriosItemHandler> curiosHandlerOptional = CuriosApi.getCuriosInventory(entity).resolve();
        if (curiosHandlerOptional.isPresent()) {
            ICuriosItemHandler curiosHandler = curiosHandlerOptional.get();
            Map<String, ICurioStacksHandler> curios = curiosHandler.getCurios();
            ICurioStacksHandler stackHandler = curios.get(curiosSlot);

            if (stackHandler != null) {
                IDynamicStackHandler stacks = stackHandler.getStacks();
                int slots = stacks.getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    
                    if (!stack.isEmpty()) {
                        // dream_sakura.LOGGER.info("Slot {}: {}", i, ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
                        return ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals(itemId);
                    }
                }
            }
        }
        return false;
    }

    // 获取指定槽位饰品
    public static ItemStack getCurio(Player player, String curiosSlot, int index) { 
        Optional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosInventory(player).resolve();
        if (curiosHandler.isPresent()) {
            Map<String, ICurioStacksHandler> stacksHandlers = curiosHandler.get().getCurios();
            ICurioStacksHandler haloHandler = stacksHandlers.get(curiosSlot);
            if (haloHandler != null) {
                IDynamicStackHandler stacks = haloHandler.getStacks();
                return stacks.getStackInSlot(0);
            }
        }
        return ItemStack.EMPTY;
    }
}
