package com.hyun.craftamap.commands;

import com.hyun.craftamap.CraftAMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GenerateTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> result = new ArrayList<>();

        if(args.length == 1) {
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
                    CraftAMap.getInstance().getDataFolder().toPath().resolve("mapdata"), "*.json")) {
                for (Path path : dirStream) {
                    result.add(path.getFileName().toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
