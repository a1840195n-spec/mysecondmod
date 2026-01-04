package com.example.mysecondmod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;

import static net.neoforged.neoforge.common.data.SoundDefinition.SoundType.SOUND;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MysecondMod.MODID)
public class MysecondMod {
    public static final String MODID = "mysecondmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID); // Указание на регистрацию блоков в моде
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID); //Регистрация кастомных звуков
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID); //Указание на регистрацию новых табов в креативе

    public static final Supplier<SoundEvent> RUBY_BREAK = SOUNDS.register("ruby_break", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "ruby_break")));
    public static final Supplier<SoundEvent> RUBY_STEP = SOUNDS.register("ruby_step", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "ruby_step")));
    public static final Supplier<SoundEvent> RUBY_PLACE = SOUNDS.register("ruby_place", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "ruby_place")));
    public static final Supplier<SoundEvent> RUBY_HIT = SOUNDS.register("ruby_hit", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "ruby_hit")));
    public static final Supplier<SoundEvent> RUBY_FALL = SOUNDS.register("ruby_fall", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "ruby_fall")));

    public static final DeferredHolder<SoundEvent, SoundEvent> RIVER_OF_DESPAIR = SOUNDS.register("river_of_despair", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "river_of_despair")));//Регистрация звука пластинки
    public static final ResourceKey<JukeboxSong> RIVER_OF_DESPAIR_KEY = ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(MODID, "river_of_despair_key"));//Создание ключа для пластинки


    public static final DeferredItem<Item> RUBY = ITEMS.registerSimpleItem("ruby"); //Регистрация рубина

    public static final DeferredBlock RUBY_ORE = BLOCKS.registerSimpleBlock("ruby_ore", //Блок рубиновой руды
            BlockBehaviour.Properties.of().
                    strength(3.0f, 6.0f)
                    .lightLevel((state) -> 2)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHER_GOLD_ORE)
    );

    public static final DeferredItem<BlockItem> RUBY_ORE_ITEM = ITEMS.registerSimpleBlockItem("ruby_ore",RUBY_ORE);//предмет рубиновой руды

    public static final DeferredBlock RUBY_BLOCK = BLOCKS.register("ruby_block", () -> new Block( //Регистрация блока рубина
                    BlockBehaviour.Properties.of()
                            .strength(3.0f, 6.0f) //Соотношение кофицента прочности и взрывоусточивости
                            .lightLevel(state -> 2)
                            .requiresCorrectToolForDrops()
                            .sound(new SoundType(
                                    1.0F, 1.0F,
                                    RUBY_BREAK.get(),  // break
                                    RUBY_STEP.get(),   // step
                                    RUBY_PLACE.get(),  // place
                                    RUBY_HIT.get(),    // hit
                                    RUBY_FALL.get()    // fall
                            ))
            )
    );

    public static final DeferredItem<BlockItem> RUBY_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("ruby_block", RUBY_BLOCK);//Регистрация предмета для блока

    public static final DeferredItem<Item> RIVER_OF_DESPAIR_DISC = ITEMS.register("river_of_despair_disc", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(RIVER_OF_DESPAIR_KEY)));//Регистрация пластинки

    public static final Supplier<CreativeModeTab> MYSECOND_TAB = TABS.register("mysecond_tab", //Регистрация вкладки в креативе для мода
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.mysecondmod"))
                    .icon(() -> new ItemStack(RUBY_BLOCK_ITEM.get()))
                    .displayItems((params, output) -> {//Добавление предметов в таб
                        output.accept(RUBY_BLOCK_ITEM.get());
                        output.accept(RUBY_ORE_ITEM.get());
                        output.accept(RUBY.get());
                        output.accept(RIVER_OF_DESPAIR_DISC.get());
                    })
                    .build()
    );



    public MysecondMod(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        TABS.register(modEventBus);
        SOUNDS.register(modEventBus);
        LOGGER.info("MysecondMod готов! Ruby загружен! 💎");
    }

     @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
