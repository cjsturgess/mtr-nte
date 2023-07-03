package cn.zbx1425.mtrsteamloco;

import cn.zbx1425.mtrsteamloco.block.BlockDepartureBell;
import cn.zbx1425.mtrsteamloco.block.BlockEyeCandy;
import cn.zbx1425.mtrsteamloco.block.BlockFeedbackBox;
import cn.zbx1425.mtrsteamloco.block.BlockStatisticTurnstile;
import cn.zbx1425.mtrsteamloco.network.PacketUpdateBlockEntity;
import cn.zbx1425.mtrsteamloco.network.PacketUpdateHoldingItem;
import cn.zbx1425.mtrsteamloco.network.PacketUpdateRail;
import cn.zbx1425.mtrsteamloco.network.PacketVersionCheck;
import com.google.gson.JsonParser;
import mtr.CreativeModeTabs;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.RegistryUtilities;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.BiConsumer;

public class Main {

	public static final String MOD_ID = "mtrsteamloco";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final JsonParser JSON_PARSER = new JsonParser();

	public static final boolean enableRegistry;
	static {
		boolean enableRegistry1;
		try {
			String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation()
					.toURI().getPath().toLowerCase(Locale.ROOT);
			enableRegistry1 = !jarPath.endsWith("-client.jar");
		} catch (URISyntaxException ignored) {
			enableRegistry1 = true;
		}
		enableRegistry = enableRegistry1;
	}

	public static final RegistryObject<Block> BLOCK_DEPARTURE_BELL = new RegistryObject<>(BlockDepartureBell::new);

	public static final RegistryObject<Block> BLOCK_EYE_CANDY = new RegistryObject<>(BlockEyeCandy::new);
	public static final RegistryObject<BlockEntityType<BlockEyeCandy.BlockEntityEyeCandy>>
			BLOCK_ENTITY_TYPE_EYE_CANDY = new RegistryObject<>(() ->
			RegistryUtilities.getBlockEntityType(
					BlockEyeCandy.BlockEntityEyeCandy::new,
					BLOCK_EYE_CANDY.get()
			));
	public static final RegistryObject<Block> BLOCK_STATISTIC_TURNSTILE = new RegistryObject<>(BlockStatisticTurnstile::new);
	public static final RegistryObject<BlockEntityType<BlockStatisticTurnstile.BlockEntityStatisticTurnstile>>
			BLOCK_ENTITY_TYPE_STATISTIC_TURNSTILE = new RegistryObject<>(() ->
			RegistryUtilities.getBlockEntityType(
					BlockStatisticTurnstile.BlockEntityStatisticTurnstile::new,
					BLOCK_STATISTIC_TURNSTILE.get()
			));
	public static final RegistryObject<Block> BLOCK_FEEDBACK_BOX = new RegistryObject<>(BlockFeedbackBox::new);
	public static final RegistryObject<BlockEntityType<BlockFeedbackBox.BlockEntityFeedbackBox>>
			BLOCK_ENTITY_TYPE_FEEDBACK_BOX = new RegistryObject<>(() ->
			RegistryUtilities.getBlockEntityType(
					BlockFeedbackBox.BlockEntityFeedbackBox::new,
					BLOCK_FEEDBACK_BOX.get()
			));

	public static final SoundEvent SOUND_EVENT_BELL = RegistryUtilities.createSoundEvent(new ResourceLocation("mtrsteamloco:bell"));

	public static SimpleParticleType PARTICLE_STEAM_SMOKE;

	public static void init(
			RegisterBlockItem registerBlockItem,
			BiConsumer<String,RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
			BiConsumer<String, SoundEvent> registerSoundEvent
	) {
		LOGGER.info("MTR-NTE " + BuildConfig.MOD_VERSION + " built at "
				+ DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()).format(BuildConfig.BUILD_TIME));
		if (enableRegistry) {
			registerBlockItem.accept("departure_bell", BLOCK_DEPARTURE_BELL, CreativeModeTabs.RAILWAY_FACILITIES);
			// registerBlockItem.accept("statistic_turnstile", BLOCK_STATISTIC_TURNSTILE, ItemGroups.RAILWAY_FACILITIES);
			// registerBlockEntityType.accept("statistic_turnstile", BLOCK_ENTITY_TYPE_STATISTIC_TURNSTILE);
			// registerBlockItem.accept("feedback_box", BLOCK_FEEDBACK_BOX, ItemGroups.RAILWAY_FACILITIES);
			// registerBlockEntityType.accept("feedback_box", BLOCK_ENTITY_TYPE_FEEDBACK_BOX);
			registerBlockItem.accept("eye_candy", BLOCK_EYE_CANDY, CreativeModeTabs.STATION_BUILDING_BLOCKS);
			registerBlockEntityType.accept("eye_candy", BLOCK_ENTITY_TYPE_EYE_CANDY);
			registerSoundEvent.accept("bell", SOUND_EVENT_BELL);

			mtr.Registry.registerNetworkReceiver(PacketUpdateBlockEntity.PACKET_UPDATE_BLOCK_ENTITY,
					PacketUpdateBlockEntity::receiveUpdateC2S);
			mtr.Registry.registerNetworkReceiver(PacketUpdateRail.PACKET_UPDATE_RAIL,
					PacketUpdateRail::receiveUpdateC2S);
			mtr.Registry.registerNetworkReceiver(PacketUpdateHoldingItem.PACKET_UPDATE_HOLDING_ITEM,
					PacketUpdateHoldingItem::receiveUpdateC2S);

			mtr.Registry.registerPlayerJoinEvent(PacketVersionCheck::sendVersionCheckS2C);
		}
	}

	@FunctionalInterface
	public interface RegisterBlockItem {
		void accept(String string, RegistryObject<Block> block, CreativeModeTabs.Wrapper tab);
	}
}
