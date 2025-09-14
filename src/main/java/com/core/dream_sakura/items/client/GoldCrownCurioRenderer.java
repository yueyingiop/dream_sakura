package com.core.dream_sakura.items.client;

import com.mojang.blaze3d.vertex.PoseStack;

import io.redspace.ironsspellbooks.item.armor.GoldCrownArmorItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class GoldCrownCurioRenderer extends GeoArmorRenderer<GoldCrownArmorItem> implements ICurioRenderer {

    public GoldCrownCurioRenderer() {
        super(new GoldCrownModel());
    }

    @Override
    public <
        L extends LivingEntity, 
        M extends EntityModel<L>
    > void render(
            ItemStack stack, // 物品栈
            SlotContext slotContext, // 插槽上下文
            PoseStack matrixStack, // 矩阵栈
            RenderLayerParent<L, M> renderLayerParent, // 渲染层
            MultiBufferSource renderTypeBuffer, // 缓存器
            int light, // 亮度
            float limbSwing, // 四肢摆动
            float limbSwingAmount, // 四肢摆动量
            float partialTicks, // tick函数
            float ageInTicks, // 年龄
            float netHeadYaw, // 头部偏航
            float headPitch // 头部俯仰
        ) {
            // LivingEntity entity = slotContext.entity();
            var headBone = this.getGeoModel().getBone("head").orElse(null);
            if (headBone != null) {
                this.renderRecursively(
                    matrixStack, 
                    animatable, 
                    headBone, 
                    this.getRenderType(animatable, this.getTextureLocation(animatable), renderTypeBuffer, partialTicks), 
                    renderTypeBuffer, 
                    null, 
                    riding, 
                    partialTicks, 
                    light, 
                    light, 
                    partialTicks, 
                    ageInTicks, 
                    netHeadYaw, 
                    headPitch
                );
            }
            
    }


}
