package dev.mayaqq.stellartune.dataStorage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.sql.Time;
import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {

    public HashMap<UUID, PlayerState> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Putting the 'players' hashmap, into the 'nbt' which will be saved.
        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            NbtCompound playerStateNbt = new NbtCompound();

            // ANYTIME YOU PUT NEW DATA IN THE PlayerState CLASS YOU NEED TO REFLECT THAT HERE!!!
            playerStateNbt.putString("lastRtpUse", playerSate.lastRtpUse.toString());

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.put("players", playersNbtCompound);
        return nbt;
    };
    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();
        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            PlayerState playerState = new PlayerState();

            playerState.lastRtpUse = Time.valueOf(playersTag.getCompound(key).getString("lastRtpUse"));

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
        ServerState serverState = getServerState(player.world.getServer());
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState());
    }
    public static class PlayerState {
        public Time lastRtpUse = new Time(0);
    }
}
