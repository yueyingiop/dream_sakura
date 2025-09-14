package com.core.dream_sakura.items;

import javax.annotation.Nonnull;

import com.core.dream_sakura.entity.ThrownWeaponEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrowableWeaponItem extends WeaponItem{

    public ThrowableWeaponItem(String itemId, Properties properties) {
        super(itemId, properties);
    }
    
     
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            ThrownWeaponEntity weaponEntity = new ThrownWeaponEntity(world, player, stack); // 创建实体
            weaponEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F); // 设置速度和方向
            world.addFreshEntity(weaponEntity); // 将实体添加到世界
        }

        player.getCooldowns().addCooldown(this, 20);
        if (!player.getAbilities().instabuild) stack.shrink(1);

        return InteractionResultHolder.success(stack);
    }
}
