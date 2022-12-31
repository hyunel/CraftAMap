package com.hyun.craftamap.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.hyun.craftamap.CraftAMap;
import com.hyun.craftamap.blocks.SignBlock;
import com.hyun.craftamap.elements.*;
import com.hyun.craftamap.elements.base.BlockVector2Deserializer;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class GenerateCommand implements CommandExecutor {
    private void drawRegions(EditSession session, Player player, int horizon, Region[] regions) {
        final var world = BukkitAdapter.adapt(player.getWorld());

        for (int i = 0; i < regions.length; i++) {
            final var region = regions[i];
            var polygonal2DRegions =  region.getRegions(world, horizon - 2, horizon - 1);
            if(polygonal2DRegions.size() == 0) continue;

            player.sendMessage("§7填充区域中(" + (i + 1) + "/" + regions.length + "): 体积=" + polygonal2DRegions.get(0).getVolume());

            for (var polygonal2DRegion: polygonal2DRegions) {
                if(region.getBlockState().isEmpty()) continue;
                session.setBlocks((com.sk89q.worldedit.regions.Region) polygonal2DRegion, BlockState.get(region.getBlockState()));
            }
            session.flushQueue();
        }
    }

    private void drawBuildings(double scale, EditSession session, Player player, int horizon, Building[] buildings, String buildingMode) {
        final var world = BukkitAdapter.adapt(player.getWorld());
        
        for (int i = 0; i < buildings.length; i++) {
            final var building = buildings[i];
            final var height = (int) Math.floor(horizon + building.getAltitude() * scale);

            player.sendMessage("§7造楼中(" + (i + 1) + "/" + buildings.length + "): 高度=" + height);

            var regions = building.getRegions(world, horizon, height);
            if (regions.size() != 1) {
                player.sendMessage("[!] building region size is not 1");
                continue;
            }

            var region = regions.get(0);
            if(buildingMode.equals("shell")) {
                session.makeWalls(region, BlockTypes.LIGHT_GRAY_CONCRETE);

                region.setMinimumY(height);
                session.setBlocks((com.sk89q.worldedit.regions.Region) region, BlockTypes.LIGHT_GRAY_CONCRETE);
            } else if(buildingMode.equals("fill")) {
                session.setBlocks((com.sk89q.worldedit.regions.Region) region, BlockTypes.LIGHT_GRAY_CONCRETE);
            }
            session.flushQueue();
        }
    }

    private void drawRoads(EditSession session, Player player, int horizon, Road[] roads)
     {
        for (int i = 0; i < roads.length; i++) {
            final var road = roads[i];
            player.sendMessage("§7铺路中(" + (i + 1) + "/" + roads.length + "): 宽度=" + road.getWeight());

            final int width = road.getWeight();
            final Iterator<BlockVector2> points = road.getPath().iterator();

            BlockVector2 lastPoint = points.next();
            while (points.hasNext()) {
                BlockVector2 point = points.next();
                session.drawLine(BlockTypes.WHITE_CONCRETE, lastPoint.toBlockVector3(horizon - 1), point.toBlockVector3(horizon - 1), width / 2d, true, true);
                lastPoint = point;
            }
            session.flushQueue();
        }
    }

    private void drawPOIs(EditSession session, Player player, int horizon, POI[] pois) {
        for (int i = 0; i < pois.length; i++) {
            final var poi = pois[i];
            player.sendMessage("§7填充地标信息(" + (i + 1) + "/" + pois.length + "): 地标=" + poi.getName() + ", 位置=" + poi.getPos().getX() + ", " + poi.getPos().getZ());

            SignBlock signBlock = new SignBlock(Objects.requireNonNull(BlockTypes.OAK_SIGN).getDefaultState(), new String[] {
                    "§f§l" + poi.getName(),
                    "§bRank: " + poi.getRank(),
                    "§7POI",
                    ""
            });
            session.setBlock(poi.getPos().toBlockVector3(horizon), (Pattern) signBlock);
        }
    }

    private void drawRoadNames(EditSession session, Player player, int horizon, RoadName[] roadNames) {
        for (int i = 0; i < roadNames.length; i++) {
            final var roadName = roadNames[i];
            player.sendMessage("§7填充路标信息(" + (i + 1) + "/" + roadNames.length + "): 路标=" + roadName.getName());

            for (var point: roadName.getPath()) {
                SignBlock signBlock = new SignBlock(Objects.requireNonNull(BlockTypes.OAK_SIGN).getDefaultState(), new String[] {
                        "§f§l" + roadName.getName(),
                        "§bRank: " + roadName.getRank(),
                        "§7ROAD",
                        ""
                });
                session.setBlock(point.toBlockVector3(horizon), (Pattern) signBlock);
            }
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c只有玩家可以使用此命令");
            return true;
        }

        String fileName = args[0];
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }

        var scale = 1d;
        var buildingMode = "shell";
        var layersStr = "region,road,building,poi,roadName";
        for (var i = 1; i < args.length; i++) {
            if (args[i].equals("-s")) {
                scale = Double.parseDouble(args[++i]);
            }
            if (args[i].equals("-b")) {
                buildingMode = args[++i];
                if(!buildingMode.equals("shell") && !buildingMode.equals("fill")) {
                    player.sendMessage("§c建筑模式只能是 shell 或 fill");
                    return true;
                }
            }
            if (args[i].equals("-l")) {
                layersStr = args[++i];
            }
        }
        var layers = Arrays.stream(layersStr.replace(" ", "").split(",")).toList();

        Location loc = player.getLocation();

        Elements elements;
        try {
            var facingDirection = player.getFacing().getDirection();
            var closestDirection = Direction.findClosest(Vector3.at(facingDirection.getX(), facingDirection.getY(), facingDirection.getZ()), Direction.Flag.CARDINAL);
            assert closestDirection != null;

            var facing = switch (closestDirection) {
                case EAST -> BlockVector2Deserializer.Orientation.EAST;
                case SOUTH -> BlockVector2Deserializer.Orientation.SOUTH;
                case WEST -> BlockVector2Deserializer.Orientation.WEST;
                default -> BlockVector2Deserializer.Orientation.NORTH;
            };

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BlockVector2.class, new BlockVector2Deserializer(scale, loc.getBlockX(), loc.getBlockZ(), facing))
                    .create();

            JsonReader reader = new JsonReader(new FileReader(Path.of(CraftAMap.getInstance().getDataFolder().getAbsolutePath(), "mapData", fileName).toFile()));
            elements = gson.fromJson(reader, Elements.class);
        } catch (FileNotFoundException e) {
            sender.sendMessage("§c没有找到名为 " + fileName + " 的地图数据！");
            return true;
        }

        EditSession session = WorldEdit.getInstance().newEditSessionBuilder()
                .fastMode(true)
                .maxBlocks(-1)
                .world(BukkitAdapter.adapt(loc.getWorld()))
                .build();

        final var horizon = loc.getBlockY();

        double finalScale = scale;
        String finalBuildingMode = buildingMode;
        CompletableFuture.runAsync(()->{
            if(layers.contains("region")) {
                player.sendMessage("§a§l正在绘制地图区域，共计 " + elements.getRegions().length + " 个...");
                drawRegions(session, player, horizon, elements.getRegions());
                session.flushQueue();
            }

            if(layers.contains("road")) {
                player.sendMessage("§a§l正在绘制道路，共计 " + elements.getRoads().length + " 个...");
                drawRoads(session, player, horizon, elements.getRoads());
                session.flushQueue();
            }

            if(layers.contains("building")) {
                player.sendMessage("§a§l正在绘制建筑，共计 " + elements.getBuildings().length + " 个...");
                drawBuildings(finalScale, session, player, horizon, elements.getBuildings(), finalBuildingMode);
                session.flushQueue();
            }

            if(layers.contains("poi")) {
                player.sendMessage("§a§l正在绘制地标，共计 " + elements.getPois().length + " 个...");
                drawPOIs(session, player, horizon, elements.getPois());
                session.flushQueue();
            }

            if(layers.contains("roadName")) {
                player.sendMessage("§a§l正在绘制路标，共计 " + elements.getRoadnames().length + " 个...");
                drawRoadNames(session, player, horizon, elements.getRoadnames());
                session.flushQueue();
            }
        }).thenRun(()-> {
            session.close();
            sender.sendMessage("§a地图生成完成！");
        }).exceptionally(e->{
            session.close();
            sender.sendMessage("§c§l地图生成失败！");
            sender.sendMessage("§4" + e);
            e.printStackTrace();
            return null;
        });

        return true;
    }
}
