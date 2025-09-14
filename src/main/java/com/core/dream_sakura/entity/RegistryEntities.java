package com.core.dream_sakura.entity;

import com.core.dream_sakura.dream_sakura;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryEntities {
    public static final DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, dream_sakura.MODID);

    public static final RegistryObject<EntityType<ThrownWeaponEntity>> THE_ETERNAL_ROTATOR = ENTITYS.register(
        "the_eternal_rotator", 
        () -> EntityType.Builder.<ThrownWeaponEntity>of(
            ThrownWeaponEntity::new,
            MobCategory.MISC
        )
        .sized(0.5F, 0.5F) // 宽高
        .clientTrackingRange(4) // 客户端跟踪范围
        .updateInterval(20)  // 更新间隔
        .build("the_eternal_rotator") // 实体ID
    );
}
