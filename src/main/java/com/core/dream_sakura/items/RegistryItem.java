package com.core.dream_sakura.items;

import java.util.EnumSet;
import java.util.List;

import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.blocks.RegistryBlock;
import com.core.dream_sakura.enums.DamageType;
import com.core.dream_sakura.sounds.RegistrySound;
import com.core.dream_sakura.util.RegistryActiveSkill;
import com.core.dream_sakura.util.RegistryPassiveSkill;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryItem {
    // 创建注册器
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, dream_sakura.MODID);
    
    // 定义发光颜色
    private static final float[] NULL_GLOW = {}; // 无色
    private static final float[] PINK_GLOW = {0.956863f,0.807843f,0.901961f}; // 粉色
    private static final float[] GOLD_GLOW = {0.956863f,0.992157f,0.329412f}; // 金色
    
    // 注册饰品
    public static final RegistryObject<Item> RGB_HALO = ITEMS.register(
        "rgb_halo", 
        () -> new DecorationItem(
            "rgb_halo",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON),
            (slotContext, stack) ->{},
            NULL_GLOW
        )
    );
    
    public static final RegistryObject<Item> DREAM_FINALE = ITEMS.register(
        "dream_finale", 
        () -> new DecorationItem(
            "dream_finale",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON),
            (slotContext, stack) -> RegistryPassiveSkill.Dream_Finale_Skill(slotContext, stack),
            stack -> {
                // 免疫所有伤害
                return EnumSet.of(DamageType.ALL);
            },
            PINK_GLOW,
            List.of(244,206,230), // 粉色工具提示颜色
            RegistryActiveSkill.Dream_Final_Skill.get(),
            ResourceLocation.fromNamespaceAndPath(dream_sakura.MODID, "dream_finale_music")
        )
    );

    public static final RegistryObject<Item> BASIC_HALO = ITEMS.register(
        "basic_halo",
        () -> new DecorationItem(
            "basic_halo",
            new Item.Properties()
                .stacksTo(1).
                rarity(Rarity.COMMON),
            (slotContext, stack) -> {},
            GOLD_GLOW
        )
    );

    
    public static final RegistryObject<Item> ALS_1_HALO = ITEMS.register(
        "als_1_halo",
        () -> new DecorationItem(
            "als_1_halo",
            new Item.Properties()
                .stacksTo(1).
                rarity(Rarity.COMMON),
            (slotContext, stack) ->{
                RegistryPassiveSkill.ALS_1_Halo_Skill_2(slotContext, stack);
            },
            itemStack -> EnumSet.of(DamageType.NULL),
            NULL_GLOW,
            null,
            RegistryActiveSkill.ALS_1_Halo_Skill.get(),
            null
        )
    );

    public static final RegistryObject<Item> HOSHINO_HALO = ITEMS.register(
        "hoshino_halo", 
        () -> new DecorationItem(
            "hoshino_halo",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON),
            (slotContext, stack) ->{
                RegistryPassiveSkill.Hoshino_Halo_Skill_0(slotContext, stack);
                RegistryPassiveSkill.Hoshino_Halo_Skill_1(slotContext, stack);
                RegistryPassiveSkill.Hoshino_Halo_Skill_2(slotContext, stack);
            },
            itemStack -> EnumSet.of(DamageType.NULL),
            NULL_GLOW,
            null,
            RegistryActiveSkill.Hoshino_Halo_Skill.get(),
            null
        )
    );

    public static final RegistryObject<Item> BLESSED_ENCOUNTER = ITEMS.register(
        "blessed_encounter",
        () -> new CuriosItem(
            "blessed_encounter",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON),
            (slotContext, stack) -> RegistryPassiveSkill.Blessed_Encounter_Skill(slotContext, stack)
        )
    );

    
    public static final RegistryObject<Item> FAREWELL = ITEMS.register(
        "farewell",
        () -> new CuriosItem(
            "farewell",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON),
            (slotContext, stack) -> RegistryPassiveSkill.Farewell_Skill(slotContext, stack)
        )
    );

    public static final RegistryObject<Item> ANKLE_SHACKLES = ITEMS.register(
        "ankle_shackles",
        () -> new AnkleShacklesItem(
            new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)
        )
    );



    // 注册方块物品
    //#region crystal系列物品
    public static final RegistryObject<Item> CRYSTAL_ITEM = ITEMS.register(
        "crystal", 
        () -> new AnimatedBlockItem(
            "crystal",
            RegistryBlock.CRYSTAL.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_BLACK_ITEM = ITEMS.register(
        "crystal_black", 
        () -> new AnimatedBlockItem(
            "crystal_black",
            RegistryBlock.CRYSTAL_BLACK.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_BLUE_ITEM = ITEMS.register(
        "crystal_blue", 
        () -> new AnimatedBlockItem(
            "crystal_blue",
            RegistryBlock.CRYSTAL_BLUE.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_BROWN_ITEM = ITEMS.register(
        "crystal_brown", 
        () -> new AnimatedBlockItem(
            "crystal_brown",
            RegistryBlock.CRYSTAL_BROWN.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_CYAN_ITEM = ITEMS.register(
        "crystal_cyan", 
        () -> new AnimatedBlockItem(
            "crystal_cyan",
            RegistryBlock.CRYSTAL_CYAN.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_GRAY_ITEM = ITEMS.register(
        "crystal_gray", 
        () -> new AnimatedBlockItem(
            "crystal_gray",
            RegistryBlock.CRYSTAL_GRAY.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_GREEN_ITEM = ITEMS.register(
        "crystal_green", 
        () -> new AnimatedBlockItem(
            "crystal_green",
            RegistryBlock.CRYSTAL_GREEN.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_LIGHT_BLUE_ITEM = ITEMS.register(
        "crystal_light_blue", 
        () -> new AnimatedBlockItem(
            "crystal_light_blue",
            RegistryBlock.CRYSTAL_LIGHT_BLUE.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_LIGHT_GRAY_ITEM = ITEMS.register(
        "crystal_light_gray", 
        () -> new AnimatedBlockItem(
            "crystal_light_gray",
            RegistryBlock.CRYSTAL_LIGHT_GRAY.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_LIME_ITEM = ITEMS.register(
        "crystal_lime", 
        () -> new AnimatedBlockItem(
            "crystal_lime",
            RegistryBlock.CRYSTAL_LIME.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_MAGENTA_ITEM = ITEMS.register(
        "crystal_magenta", 
        () -> new AnimatedBlockItem(
            "crystal_magenta",
            RegistryBlock.CRYSTAL_MAGENTA.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_ORANGE_ITEM = ITEMS.register(
        "crystal_orange", 
        () -> new AnimatedBlockItem(
            "crystal_orange",
            RegistryBlock.CRYSTAL_ORANGE.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_PINK_ITEM = ITEMS.register(
        "crystal_pink", 
        () -> new AnimatedBlockItem(
            "crystal_pink",
            RegistryBlock.CRYSTAL_PINK.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_PURPLE_ITEM = ITEMS.register(
        "crystal_purple", 
        () -> new AnimatedBlockItem(
            "crystal_purple",
            RegistryBlock.CRYSTAL_PURPLE.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_RED_ITEM = ITEMS.register(
        "crystal_red", 
        () -> new AnimatedBlockItem(
            "crystal_red",
            RegistryBlock.CRYSTAL_RED.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_WHITE_ITEM = ITEMS.register(
        "crystal_white", 
        () -> new AnimatedBlockItem(
            "crystal_white",
            RegistryBlock.CRYSTAL_WHITE.get(), 
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CRYSTAL_YELLOW_ITEM = ITEMS.register(
        "crystal_yellow", 
        () -> new AnimatedBlockItem(
            "crystal_yellow",
            RegistryBlock.CRYSTAL_YELLOW.get(), 
            new Item.Properties()
        )
    );

    //#endregion

    
    
    // 注册唱片
    public static final RegistryObject<Item> TEST_MUSIC_DISC_ITEM = ITEMS.register(
        "test_music_disc", 
        () -> new MusicDiscItem(
            5, 
            () -> RegistrySound.DREAM_FINALE_MUSIC.get(), 
            new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 
            155 * 20 // 时长(tick)
        )
    );

    // 注册武器
    public static final RegistryObject<Item> PSIONIC_SCEPTER = ITEMS.register(
        "psionic_scepter",
        () -> new WeaponItem(
            "psionic_scepter", 
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON)
        )
    );

    public static final RegistryObject<Item> ENDER_SLAYER = ITEMS.register(
        "ender_slayer",
        () -> new WeaponItem(
            "ender_slayer", 
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON)
        )
    );

    public static final RegistryObject<Item> THE_ETERNAL_ROTATOR = ITEMS.register(
        "the_eternal_rotator", 
        () -> new ThrowableWeaponItem(
            "the_eternal_rotator", 
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON)
        )
    );


    //#region 经验材料
    public static final RegistryObject<Item> JUNIOR_EXP = ITEMS.register(
        "primary_exp", 
        () -> new ExpToolItem(
            new Item.Properties()
                .rarity(Rarity.COMMON),
            200
        )
    );

    public static final RegistryObject<Item> INTERMEDIATE_EXP = ITEMS.register(
        "intermediate_exp", 
        () -> new ExpToolItem(
            new Item.Properties()
                .rarity(Rarity.COMMON),
            1024
        )
    );

    public static final RegistryObject<Item> SENIOR_EXP = ITEMS.register(
        "senior_exp", 
        () -> new ExpToolItem(
            new Item.Properties()
                .rarity(Rarity.COMMON),
            4096
        )
    );

    public static final RegistryObject<Item> SUPERIOR_EXP = ITEMS.register(
        "superior_exp", 
        () -> new ExpToolItem(
            new Item.Properties()
                .rarity(Rarity.COMMON),
            8460
        )
    );

    public static final RegistryObject<Item> CREATE_EXP = ITEMS.register(
        "create_exp", 
        () -> new ExpToolItem(
            new Item.Properties()
                .rarity(Rarity.COMMON),
            999999
        )
    );
    //#endregion

}
