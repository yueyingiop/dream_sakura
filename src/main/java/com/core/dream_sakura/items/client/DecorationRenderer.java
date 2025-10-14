package com.core.dream_sakura.items.client;

import com.core.dream_sakura.items.DecorationItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DecorationRenderer extends GeoItemRenderer<DecorationItem> {
    public DecorationRenderer() {
        super(new DecorationModel());
    }
    @Override
    public void renderByItem(
        ItemStack stack, 
        ItemDisplayContext transformType, 
        PoseStack poseStack, 
        MultiBufferSource bufferSource, 
        int packedLight, 
        int packedOverlay
    ) {
        
        if (!(stack.getItem() instanceof DecorationItem item)) {
            return;
        }
        
        
        // 保存当前状态
        poseStack.pushPose();

        try {
            super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
            if (item.getGlowColor().length != 0) {
                renderGlowLayer(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay, item);
            }
        
        } finally {
            poseStack.popPose();
        }
    }

    private void renderGlowLayer(
        ItemStack stack, 
        ItemDisplayContext transformType,
        PoseStack poseStack, 
        MultiBufferSource bufferSource,
        int packedLight, 
        int packedOverlay, 
        DecorationItem item
    ) {
        ResourceLocation glowTexture  = getGlowTextureResource(item);
        if (glowTexture == null) return;

        int fullBrightLight = 0xF000F0;

        float[] glowColor = item.getGlowColor();
        float glowIntensity = item.getGlowIntensity();

        RenderType glowRenderType = RenderType.eyes(glowTexture);
        VertexConsumer glowBuffer = bufferSource.getBuffer(glowRenderType);

        BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(item));
        poseStack.pushPose();
        try {
            poseStack.translate(0.5, 0.5 + 0.01D, 0.5); //偏移
            float scaleFactor = 1.0f;
            poseStack.scale(scaleFactor, scaleFactor, scaleFactor); // 缩放
            this.reRender(
                model, 
                poseStack, 
                bufferSource, 
                item, 
                glowRenderType, 
                glowBuffer, 
                0, 
                fullBrightLight, 
                OverlayTexture.NO_OVERLAY, 
                glowColor[0], 
                glowColor[1], 
                glowColor[2], 
                glowIntensity
            );
        } finally {
            poseStack.popPose();
        }
        
    }


    private ResourceLocation getGlowTextureResource(DecorationItem item) {
        ResourceLocation normalTexture = this.getGeoModel().getTextureResource(item);
        
        // 在正常贴图路径后添加 "_glow" 作为发光贴图
        String normalPath = normalTexture.getPath();
        String glowPath = normalPath.replace(".png", "_glow.png");
        
        return ResourceLocation.fromNamespaceAndPath(
            normalTexture.getNamespace(),
            glowPath
        );
    }
}
