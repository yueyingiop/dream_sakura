package com.core.dream_sakura.effect;

import com.core.dream_sakura.dream_sakura;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryEffect {
    public static final DeferredRegister<MobEffect> EFFECTS = 
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, dream_sakura.MODID);
    
    public static final RegistryObject<MobEffect> STRENGTHEN_EFFECT = 
        EFFECTS.register("strengthen", StrengthenEffect::new);

    public static final RegistryObject<MobEffect> LIFE_STEAL_EFFECT = 
        EFFECTS.register("life_steal", LifeStealEffect::new);
        
    public static final RegistryObject<MobEffect> PERCENT_REGENERATION_EFFECT = 
        EFFECTS.register("percent_regeneration", PercentRegenerationEffect::new);
}
