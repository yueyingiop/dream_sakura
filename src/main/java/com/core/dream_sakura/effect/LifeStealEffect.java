package com.core.dream_sakura.effect;

import javax.annotation.Nonnull;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class LifeStealEffect extends MobEffect {
    public LifeStealEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity entity, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return false;
    }
}
