package com.core.dream_sakura.enums;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

public enum DamageType {
    ALL {
        @Override
        public boolean shouldImmune(DamageSource source) {
            return true; 
        }// 免疫所有伤害
    },
    FIRE {
        @Override
        public boolean shouldImmune(DamageSource source) {
            return source.is(DamageTypeTags.IS_FIRE) || 
                    source.is(DamageTypes.IN_FIRE)|| 
                    source.is(DamageTypes.ON_FIRE) || 
                    source.is(DamageTypes.LAVA);
        }// 免疫火焰伤害
    },
    NULL{
        @Override
        public boolean shouldImmune(DamageSource source) {
            return false;
        } // 不免疫任何伤害
    };
    public abstract boolean shouldImmune(DamageSource source);
}
