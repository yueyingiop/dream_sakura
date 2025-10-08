package com.core.dream_sakura.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.util.OtherHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ExpToolItem extends Item {
    private final int xp;
    public ExpToolItem(Properties properties, int xp) {
        super(properties);
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        ItemStack halo = OtherHelper.getCurio(player, "halo", 0);
        boolean isHalo = Items.AIR != halo.getItem();
        if (!level.isClientSide && isHalo) {
            CompoundTag haloTag = halo.getOrCreateTag();
            int itemLevel = haloTag.getInt("level");
            int itemXp = haloTag.getInt("xp");
            int itemMaxXp = haloTag.getInt("maxXp");

            if (itemLevel >= 95) {
                player.sendSystemMessage(Component.translatable("message.dream_sakura.halo_max_level"));
                return InteractionResultHolder.fail(itemStack);
            }

            int newXp = itemXp + getXp();
            itemStack.shrink(1);
            while (itemMaxXp - newXp <= 0) {
                itemLevel++;
                itemMaxXp = (int) OtherHelper.expCalculate(itemLevel);
                newXp = newXp - itemMaxXp;
                if (itemLevel >= 95) {
                    if (newXp > itemMaxXp) newXp = itemMaxXp;
                    break;
                }
            }
            haloTag.putInt("level", itemLevel);
            haloTag.putInt("xp", newXp);
            haloTag.putInt("maxXp", itemMaxXp);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip." + dream_sakura.MODID + ".baseXp", getXp()));
    }
}
