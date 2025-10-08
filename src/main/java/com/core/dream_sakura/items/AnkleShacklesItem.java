package com.core.dream_sakura.items;

import java.util.UUID;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class AnkleShacklesItem extends Item implements ICurioItem {
    private static final UUID SPEED_LIMITER_UUID = UUID.fromString("a3d8a79a-7d4a-4b8a-9d2a-1a2b3c4d5e6f");

    public AnkleShacklesItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "feet".equals(slotContext.identifier());
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            AttributeInstance movementAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);

            if (movementAttribute != null) {
                movementAttribute.removeModifier(SPEED_LIMITER_UUID);
                
                double currentSpeed = movementAttribute.getValue();
                if (currentSpeed > 0.1D) { // 0.1是玩家正常行走速度:cite[7]
                    double excessSpeed = currentSpeed - 0.1D;
                    double reductionRatio = excessSpeed / currentSpeed;
                    
                    AttributeModifier speedLimiter = new AttributeModifier(
                        SPEED_LIMITER_UUID,
                        "ankle_shackles_speed_limit",
                        -reductionRatio, // 减少超出的部分
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                    );
                    movementAttribute.addPermanentModifier(speedLimiter);
                }
            }
        }
        
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            AttributeInstance movementAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);

            if (movementAttribute != null) {
                movementAttribute.removeModifier(SPEED_LIMITER_UUID);
                
                double currentSpeed = movementAttribute.getValue();
                if (currentSpeed > 0.1D) { // 0.1是玩家正常行走速度
                    double excessSpeed = currentSpeed - 0.1D;
                    double reductionRatio = excessSpeed / currentSpeed;
                    
                    AttributeModifier speedLimiter = new AttributeModifier(
                        SPEED_LIMITER_UUID,
                        "ankle_shackles_speed_limit",
                        -reductionRatio, // 减少超出的部分
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                    );
                    movementAttribute.addPermanentModifier(speedLimiter);
                }
            }
        }
        ICurioItem.super.curioTick(slotContext, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide()) {
            // 卸下时移除速度限制修饰符
            AttributeInstance movementAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementAttribute != null) {
                movementAttribute.removeModifier(SPEED_LIMITER_UUID);
            }
        }
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
