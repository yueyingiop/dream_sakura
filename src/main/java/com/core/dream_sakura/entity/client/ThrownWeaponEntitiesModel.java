package com.core.dream_sakura.entity.client;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.entity.ThrownWeaponEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.model.GeoModel;

public class ThrownWeaponEntitiesModel extends GeoModel<ThrownWeaponEntity> {

    private String getEntityId(ThrownWeaponEntity thrownWeapon) {
        ItemStack weaponStack = thrownWeapon.getPickupItem();
        Item item = weaponStack.getItem();
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    @Override
    public ResourceLocation getModelResource(ThrownWeaponEntity thrownWeapon) {
        String itemId = getEntityId(thrownWeapon);
        return ResourceLocation.fromNamespaceAndPath(dream_sakura.MODID, "geo/weapon/"+itemId+".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThrownWeaponEntity thrownWeapon) {
        String itemId = getEntityId(thrownWeapon);
        return ResourceLocation.fromNamespaceAndPath(dream_sakura.MODID, "textures/item/weapon/"+itemId+".png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThrownWeaponEntity thrownWeapon) {
        String itemId = getEntityId(thrownWeapon);
        dream_sakura.LOGGER.info("itemId: "+itemId);
        return ResourceLocation.fromNamespaceAndPath(dream_sakura.MODID, "animations/weapon/"+itemId+".animation.json");
    }
}
