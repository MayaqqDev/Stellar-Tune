package dev.mayaqq.stellartune.dataStorage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {

    public HashMap<UUID, PlayerState> players = new HashMap<>();
    public int[] spawnCoords = new int[]{0, 100, 0};
    public String spawnDimension = "minecraft:overworld";

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Putting the 'players' hashmap, into the 'nbt' which will be saved.
        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            NbtCompound playerStateNbt = new NbtCompound();

            // ANYTIME YOU PUT NEW DATA IN THE PlayerState CLASS YOU NEED TO REFLECT THAT HERE!!!
            playerStateNbt.putLong("lastRtpUse", playerSate.lastRtpUse);
            playerStateNbt.putLong("lastHomeUse", playerSate.lastHomeUse);

            NbtCompound homesNbtCompound = new NbtCompound();
            playerSate.homes.forEach(homesNbtCompound::putIntArray);
            playerStateNbt.put("homes", homesNbtCompound);

            NbtCompound homesDimensionNbtCompound = new NbtCompound();
            playerSate.homesDimension.forEach(homesDimensionNbtCompound::putString);
            playerStateNbt.put("homesDimension", homesDimensionNbtCompound);

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.putIntArray("spawnCoords", spawnCoords);
        nbt.putString("spawnDimension", spawnDimension);
        nbt.put("players", playersNbtCompound);
        return nbt;
    }
    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();
        serverState.spawnCoords = tag.getIntArray("spawnCoords");
        serverState.spawnDimension = tag.getString("spawnDimension");
        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            PlayerState playerState = new PlayerState();

            playerState.lastRtpUse = playersTag.getCompound(key).getLong("lastRtpUse");
            playerState.lastHomeUse = playersTag.getCompound(key).getLong("lastHomeUse");

            NbtCompound homesTag = playersTag.getCompound(key).getCompound("homes");
            homesTag.getKeys().forEach(homeKey -> {
                playerState.homes.put(homeKey, homesTag.getIntArray(homeKey));
            });

            NbtCompound homesDimensionTag = playersTag.getCompound(key).getCompound("homesDimension");
            homesDimensionTag.getKeys().forEach(homeDimensionKey -> {
                playerState.homesDimension.put(homeDimensionKey, homesDimensionTag.getString(homeDimensionKey));
            });

            UUID uuid = UUID.fromString(key);
            serverState.players.put(uuid, playerState);
        });
        return serverState;
    }

    public static ServerState getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        // You need to use a unique string as the key. You should already have a MODID variable defined by you somewhere in your code. Use that.
        ServerState serverState = persistentStateManager.getOrCreate(ServerState::createFromNbt, ServerState::new, "stellartune");

        serverState.markDirty(); // makes stuff work

        return serverState;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public static PlayerState getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(player.getServer());
        serverState.markDirty();
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState());
    }
    public static class PlayerState {
        public long lastRtpUse = 0;
        public long lastHomeUse = 0;
        public HashMap<String, int[]> homes = new HashMap<>();
        public HashMap<String, String> homesDimension = new HashMap<>();
    }
}