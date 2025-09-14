package com.core.dream_sakura.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.lwjgl.glfw.GLFW;

import com.core.dream_sakura.Config;
import com.core.dream_sakura.dream_sakura;
import com.core.dream_sakura.blocks.RegistryBlock;
import com.core.dream_sakura.enums.DamageType;
import com.core.dream_sakura.skill.SkillBinding;
import com.core.dream_sakura.sounds.RegistrySound;
// import com.mega.uom.util.entity.EntityASMUtil;
import com.core.dream_sakura.util.EntityDataCleaner;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;



public class RegistryItem {
    // 创建注册器
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, dream_sakura.MODID);
    
    // 定义发光颜色
    private static final float[] NULL_GLOW = {}; // 无色
    private static final float[] PINK_GLOW = {0.956863f,0.807843f,0.901961f}; // 粉色
    private static final float[] GOLD_GLOW = {0.956863f,0.992157f,0.329412f}; // 金色
    
    // 技能
    private static final Supplier<SkillBinding> Dream_Final_Skill = ()->{
        return new SkillBinding(
            GLFW.GLFW_KEY_K,
            "Dream Finale Skill", 
            Config.dreamFinaleCooldown, // 冷却120s
            "dream_finale",
            (player, stack)->{
                player.level().getEntitiesOfClass(
                    LivingEntity.class, 
                    player.getBoundingBox().inflate(36.0)
                ).forEach(entity -> {
                    if (entity != player) { // 排除玩家自身
                        entity.discard(); // 删除实体
                    }
                });
            }
        );
    };
    
    



    // 注册饰品
    public static final RegistryObject<Item> TEST_HALO = ITEMS.register(
        "test_halo", 
        () -> new DecorationItem(
            "test_halo",
            new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON),
            (slotContext, stack) ->{},
            NULL_GLOW
        )
    );
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
            (slotContext, stack) ->{
                LivingEntity entity = slotContext.entity();
                EntityDataCleaner.cleanEntityData(entity); // 清除实体数据(不管用)

                // 每tick移除所有效果
                if (!entity.level().isClientSide()){
                    // 检测正在活动的负面效果
                    Collection<MobEffectInstance> activeEffects = entity.getActiveEffects();  // 获取所有效果
                    List<MobEffect> effectsToRemove = new ArrayList<>();  // 负面效果列表
                    for (MobEffectInstance effectInstance : activeEffects) {
                        if (effectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                            effectsToRemove.add(effectInstance.getEffect());  // 将负面效果添加到列表中
                        }
                    }

                    // 移除负面效果
                    for (MobEffect effect : effectsToRemove) {
                        entity.removeEffect(effect);
                    }

                    // 添加飞行效果
                    if(entity instanceof Player player){
                        Abilities abilities = player.getAbilities();
                        abilities.mayfly = true;
                        abilities.invulnerable = true; // 无敌

                        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                            serverPlayer.onUpdateAbilities();
                        }
                        
                    }
                    
                    // 生命值低于50%给予生命回复效果
                    if (entity.getHealth() < (entity.getMaxHealth()/2)) {
                        
                        MobEffectInstance regeneration = new MobEffectInstance(
                            MobEffects.REGENERATION,  // 生命回复效果
                            100,             // 持续时间：100 ticks
                            9,              // 效果等级
                            false,          // 是否显示粒子
                            true,           // 是否显示图标
                            true            // 环境效果是否可见
                        );
                        entity.addEffect(regeneration);
                    }
                }
            },
            stack -> {
                // 免疫所有伤害
                return EnumSet.of(DamageType.ALL);
            },
            PINK_GLOW,
            List.of(244,206,230), // 粉色工具提示颜色
            Dream_Final_Skill.get(),
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
            (slotContext, stack) ->{
                
            },
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
                
            },
            NULL_GLOW
        )
    );

    private static final UUID LUCK_MODIFIER_UUID = UUID.fromString("389b669a-4576-4bb2-9e08-ede652fbc7c9");
    public static final RegistryObject<Item> BLESSED_ENCOUNTER = ITEMS.register(
        "blessed_encounter",
        () -> new CuriosItem(
            "blessed_encounter",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON),
            (slotContext, stack) ->{
                LivingEntity entity = slotContext.entity();
                if (!entity.level().isClientSide() && entity instanceof Player player) {
                    int experienceLevel = player.experienceLevel;
                    int extraLevels = Math.max(0, experienceLevel - 520);
                    if (extraLevels > 0) {
                        var attributes = player.getAttributes();
                        AttributeInstance luckAttribute = attributes.getInstance(Attributes.LUCK);
                        
                        if (luckAttribute != null) {
                            luckAttribute.removeModifier(LUCK_MODIFIER_UUID);
                            AttributeModifier luckModifier = new AttributeModifier(
                                LUCK_MODIFIER_UUID,
                                "luck",
                                extraLevels, // 幸运值增加量 = 超出等级数
                                AttributeModifier.Operation.ADDITION
                            );
                            luckAttribute.addPermanentModifier(luckModifier);
                        }
                    }
                } else {
                    // 移除幸运值
                    Player player = (Player) entity;
                    var attributes = player.getAttributes();
                    AttributeInstance luckAttribute = attributes.getInstance(Attributes.LUCK);
                    if (luckAttribute != null) {
                        luckAttribute.removeModifier(LUCK_MODIFIER_UUID);
                    }
                }
            },
            (slotContext, newStack, stack) ->{
                LivingEntity entity = slotContext.entity();
                if (!entity.level().isClientSide() && entity instanceof Player player) {
                    var attributes = player.getAttributes();
                    AttributeInstance luckAttribute = attributes.getInstance(Attributes.LUCK);
                    if (luckAttribute != null) {
                        luckAttribute.removeModifier(LUCK_MODIFIER_UUID);
                    }
                }
            }
        )
    );

    private static final UUID ATTACK_MODIFIER_UUID = UUID.fromString("6e2cb19d-471e-4adc-b39a-ed3c3dc29b67");
    public static final RegistryObject<Item> FAREWELL = ITEMS.register(
        "farewell",
        () -> new CuriosItem(
            "farewell",
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON),
            (slotContext, stack) -> {
                LivingEntity entity = slotContext.entity();
                if (!entity.level().isClientSide() && entity instanceof Player player) {
                    CompoundTag tag = stack.getOrCreateTag();
                    MinecraftServer server = player.getServer();
                    int playerCount = server != null?server.getPlayerCount():1;
                    if (playerCount == 1) {
                        double damageBonus = tag.getDouble("DamageBonus");
                        if (player.level().getGameTime() % 20 == 0) {
                            damageBonus += 0.00001;
                            tag.putDouble("DamageBonus", damageBonus);
                            stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                            
                            // applyDamageBonus(player, damageBonus);
                            updateItemAttributeModifier(stack, damageBonus);
                            if (player.getRandom().nextFloat() < 0.025f) {
                                applyRandomDebuff(player);
                            }
                        }
                    } else {
                        ListTag modifiersList;
                        if (tag.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
                            modifiersList = tag.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
                        } else {
                            modifiersList = new ListTag();
                        }
                        removeExistingModifier(modifiersList, ATTACK_MODIFIER_UUID);
                    }
                }
            }
        )
    );


    private static void updateItemAttributeModifier(ItemStack stack, double damageBonus) {
        CompoundTag tag = stack.getOrCreateTag();
        
        // 创建或获取AttributeModifiers列表
        ListTag modifiersList;
        if (tag.contains("CurioAttributeModifiers", 9)) { // 9表示LIST TAG
            modifiersList = tag.getList("CurioAttributeModifiers", 10); // 10表示COMPOUND TAG
        } else {
            modifiersList = new ListTag();
        }
        
        // 移除旧的修饰符（如果存在）
        removeExistingModifier(modifiersList, ATTACK_MODIFIER_UUID);
        
        // 创建新的属性修饰符
        if (damageBonus > 0) {
            CompoundTag modifierTag = new CompoundTag();
            modifierTag.putString("AttributeName", "minecraft:generic.attack_damage");
            modifierTag.putDouble("Amount", damageBonus);
            modifierTag.putInt("Operation", 2);
            modifierTag.putIntArray("UUID", uuidToIntArray(ATTACK_MODIFIER_UUID));
            modifierTag.putString("Slot", "ring"); // 使用curio槽位
            
            modifiersList.add(modifierTag);
        }
        
        // 将更新后的列表放回NBT
        tag.put("CurioAttributeModifiers", modifiersList);
        
        // 设置HideFlags以隐藏属性修饰符信息（可选）
        tag.putInt("HideFlags", tag.getInt("HideFlags") | 2); // 2表示隐藏属性修饰符信息
    }

    // 移除已存在的修饰符
    private static void removeExistingModifier(ListTag modifiersList, UUID uuidToRemove) {
        int[] uuidArray = uuidToIntArray(uuidToRemove);
        
        for (int i = 0; i < modifiersList.size(); i++) {
            CompoundTag modifierTag = modifiersList.getCompound(i);
            if (modifierTag.contains("UUID") && 
                arraysEqual(modifierTag.getIntArray("UUID"), uuidArray)) {
                modifiersList.remove(i);
                break;
            }
        }
    }

    // 将UUID转换为int数组
    private static int[] uuidToIntArray(UUID uuid) {
        long mostSignificant = uuid.getMostSignificantBits();
        long leastSignificant = uuid.getLeastSignificantBits();
        return new int[] {
            (int)(mostSignificant >> 32),
            (int)mostSignificant,
            (int)(leastSignificant >> 32),
            (int)leastSignificant
        };
    }

    // 比较两个int数组是否相等
    private static boolean arraysEqual(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    // 随机效果
    private static void applyRandomDebuff(Player player) {
        Collection<MobEffect> allEffects = ForgeRegistries.MOB_EFFECTS.getValues(); // 获取所有效果
        List<MobEffect> effects = new ArrayList<>(allEffects);

        if (!effects.isEmpty()) {
            MobEffect randomEffect = effects.get(player.getRandom().nextInt(effects.size()));
            int duration = (30 + player.getRandom().nextInt(31)) * 20;
            player.addEffect(new MobEffectInstance(randomEffect, duration));
        }
    }



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
 
    /**
     * 随时间改变颜色rgb的函数
     * @param steps - 步骤数
     * @return
    */
    public static List<Integer> getRainbowColorList(int steps) {
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < steps; i++) {
            float hue = (float) i / steps;
            int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
            colorList.add(rgb);
        }
        return colorList;
    }
}
