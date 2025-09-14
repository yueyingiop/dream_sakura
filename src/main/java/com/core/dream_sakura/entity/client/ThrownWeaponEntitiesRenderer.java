package com.core.dream_sakura.entity.client;

import javax.annotation.Nonnull;

import org.joml.Vector3f;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.entity.ThrownWeaponEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ThrownWeaponEntitiesRenderer extends GeoEntityRenderer<ThrownWeaponEntity> {
    private final ItemRenderer itemRenderer;

    public ThrownWeaponEntitiesRenderer(EntityRendererProvider.Context context) {
        super(context,new ThrownWeaponEntitiesModel());
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
        ThrownWeaponEntity entity, 
        float yaw, 
        float partialTicks, 
        PoseStack poseStack,
        MultiBufferSource buffer, 
        int light
    ) {
        if (entity.getPickupItem().isEmpty() || 
            entity.getPickupItem().getItem() == Items.AIR) {
            return; // 跳过空气物品的渲染
        }
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        // poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(1.5F, 1.5F, 1.5F); // 缩放渲染
        itemRenderer.renderStatic(
            entity.getPickupItem(), 
            ItemDisplayContext.FIXED, 
            light, 
            OverlayTexture.NO_OVERLAY,
            poseStack, 
            buffer, 
            entity.level(), 
            entity.getId()
        );
        
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownWeaponEntity p_114482_) {
        return ResourceLocation.fromNamespaceAndPath(dream_sakura.MODID, "textures/item/weapon/the_eternal_rotator.png");
    }

}
