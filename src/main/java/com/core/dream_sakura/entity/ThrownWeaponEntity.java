package com.core.dream_sakura.entity;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ThrownWeaponEntity extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private ItemStack weaponStack;

    public ThrownWeaponEntity(EntityType<? extends ThrownWeaponEntity> type, Level world){
        super(type, world);
        this.weaponStack = ItemStack.EMPTY;
    };

    public ThrownWeaponEntity(Level world, LivingEntity owner, ItemStack weaponStack) {
        super(RegistryEntities.THE_ETERNAL_ROTATOR.get(), owner, world);
        this.weaponStack = weaponStack.copy();
    }
    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(
            this.damageSources().trident(this, this.getOwner()),
            8.0F
        );
    }
    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        super.onHitBlock(result);
        // 插入方块时的效果
        this.setNoPhysics(false);
        this.playSound(SoundEvents.TRIDENT_HIT_GROUND, 1.0F, 1.0F);
    }
    @Override
    public ItemStack getPickupItem() {
        return weaponStack.copy();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::handleAnimations));
    }

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation");


    private <T extends GeoAnimatable> PlayState handleAnimations(AnimationState<T> state) {
        if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            state.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("weaponStack")) {
            this.weaponStack = ItemStack.of(compound.getCompound("weaponStack"));
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.weaponStack.isEmpty()) {
            compound.put("weaponStack", this.weaponStack.save(new CompoundTag()));
        }
    }
}
