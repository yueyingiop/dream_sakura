package com.core.dream_sakura.effect;

import javax.annotation.Nonnull;

import com.core.dream_sakura.util.OtherHelper;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PercentRegenerationEffect extends MobEffect {

    public PercentRegenerationEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x98FB98);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            float healPercent = OtherHelper.calculate(0.001f, 0.85f, 95, amplifier+1);
            float healAmount = entity.getMaxHealth() * healPercent;

            entity.heal(healAmount);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    
}
