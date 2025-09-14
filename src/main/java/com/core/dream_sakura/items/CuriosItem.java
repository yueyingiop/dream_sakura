package com.core.dream_sakura.items;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.util.TriConsumer;

import com.core.dream_sakura.dream_sakura;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CuriosItem extends Item implements ICurioItem {
    private final BiConsumer<SlotContext, ItemStack> curioEquipCallback;
    private final TriConsumer<SlotContext, ItemStack, ItemStack> curioUnequipCallback;
    private final String ItemId;

    public CuriosItem(String id, Properties properties) {
        this(id, properties, null);
    }

    public CuriosItem(String id, Properties properties, BiConsumer<SlotContext, ItemStack> curioTickCallback) {
        this(id, properties, curioTickCallback, null);
    }

    public CuriosItem(String id, Properties properties, BiConsumer<SlotContext, ItemStack> curioTickCallback, TriConsumer<SlotContext, ItemStack, ItemStack> curioUnequipCallback) {
        super(properties);
        this.ItemId = id;
        this.curioEquipCallback = curioTickCallback;
        this.curioUnequipCallback = curioUnequipCallback;
    }


    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "ring".equals(slotContext.identifier());
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        if (curioUnequipCallback != null) {
            curioUnequipCallback.accept(slotContext, newStack, stack);
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // 执行callback
        if (curioEquipCallback != null) {
            curioEquipCallback.accept(slotContext, stack);
        }
    }

    @Override
    public void appendHoverText(
        @Nonnull ItemStack stack, 
        @Nullable Level level, 
        @Nonnull List<Component> tooltip,
        @Nonnull TooltipFlag flag
    ) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip." + dream_sakura.MODID + "." + this.ItemId + ".base")
            .withStyle(ChatFormatting.GRAY));  // 添加基础描述
        if (this.ItemId == "farewell") {
            CompoundTag tag = stack.getOrCreateTag();
            double damageBonus = tag.getDouble("DamageBonus");
            tooltip.add(
                Component.literal(
                    String.format("伤害加成: +%.2f%%", damageBonus*100)
                ).withStyle(ChatFormatting.GREEN)
            );
        }
    }

    
    // @Override
    // public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
    //     SlotContext slotContext, 
    //     UUID uuid,
    //     ItemStack stack
    // ) {
    //     Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
    //     if ("farewell".equals(this.ItemId) && "ring".equals(slotContext.identifier())) {
    //         CompoundTag tag = stack.getOrCreateTag();
    //         if (tag.contains("DamageBonus")) {
    //             double damageBonus = tag.getDouble("DamageBonus");
    //             if (damageBonus > 0) {
    //                 modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
    //                     UUID.fromString("6e2cb19d-471e-4adc-b39a-ed3c3dc29b67"),
    //                     "damage",
    //                     damageBonus,
    //                     AttributeModifier.Operation.MULTIPLY_TOTAL
    //                 ));
    //             }
    //             CuriosApi.addSlotModifier(
    //                 modifiers, 
    //                 "ring", 
    //                 UUID.fromString("6e2cb19d-471e-4adc-b39a-ed3c3dc29b67"),
    //                 damageBonus,
    //                 AttributeModifier.Operation.MULTIPLY_TOTAL
    //             );
    //         }
    //     }
        
    //     return modifiers;
    // }
}
