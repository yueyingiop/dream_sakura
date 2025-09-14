package com.core.dream_sakura.items.client;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.blocks.entity.RegistryBlockEntity;
import com.core.dream_sakura.blocks.entity.client.AnimatedBlockRender;
import com.core.dream_sakura.entity.RegistryEntities;
import com.core.dream_sakura.entity.client.ThrownWeaponEntitiesRenderer;
import com.core.dream_sakura.enums.CurioRendererType;
import com.core.dream_sakura.items.RegistryItem;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = dream_sakura.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        DecorationRenderer decorationRenderer = new DecorationRenderer();

        CuriosRendererRegistry.register(
            RegistryItem.RGB_HALO.get(), 
            () -> new GeckoCurioRenderer<>(decorationRenderer,CurioRendererType.HALO_UNCOMMON)
        );

        CuriosRendererRegistry.register(
            RegistryItem.TEST_HALO.get(), 
            () -> new GeckoCurioRenderer<>(decorationRenderer,CurioRendererType.HALO_UNCOMMON)
        );
        CuriosRendererRegistry.register(
            RegistryItem.DREAM_FINALE.get(), 
            () -> new GeckoCurioRenderer<>(decorationRenderer,CurioRendererType.HALO_UNCOMMON)
        );

        CuriosRendererRegistry.register(
            RegistryItem.BASIC_HALO.get(),
            ()-> new GeckoCurioRenderer<>(decorationRenderer,CurioRendererType.HALO_UNCOMMON)
        );

        CuriosRendererRegistry.register(
            RegistryItem.ALS_1_HALO.get(),
            ()-> new GeckoCurioRenderer<>(decorationRenderer,CurioRendererType.HALO_COMMON)
        );

        CuriosRendererRegistry.register(
            Items.GOLD_INGOT,
            () -> new GoldCrownCurioRenderer()
        );
        //#region crystal实体系列
        BlockEntityRenderers.register(
            RegistryBlockEntity.CRYSTAL_ENTITY.get(),
            AnimatedBlockRender::new
        );
        //#endregion

        EntityRenderers.register(
            RegistryEntities.THE_ETERNAL_ROTATOR.get(),
            ThrownWeaponEntitiesRenderer::new 
        );
    }


}
