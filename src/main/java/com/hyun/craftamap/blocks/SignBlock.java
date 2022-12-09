/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.hyun.craftamap.blocks;

import com.sk89q.worldedit.util.gson.GsonUtil;
import com.sk89q.worldedit.util.nbt.CompoundBinaryTag;
import com.sk89q.worldedit.util.nbt.StringBinaryTag;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;

/**
 * Represents a sign block.
 *
 */
public class SignBlock extends BaseBlock {

    private String[] text;

    private static String EMPTY = "{\"text\":\"\"}";

    /**
     * Construct the sign with text.
     *
     * @param blockState The block state
     * @param text       lines of text
     */
    public SignBlock(BlockState blockState, String[] text) {
        super(blockState);
        if (text == null) {
            this.text = new String[]{EMPTY, EMPTY, EMPTY, EMPTY};
            return;
        }
        for (int i = 0; i < text.length; i++) {
            if (text[i].isEmpty()) {
                text[i] = EMPTY;
            } else {
                text[i] = "{\"text\":" + GsonUtil.stringValue(text[i]) + "}";
            }
        }
        this.text = text;
    }

    /**
     * Get the text.
     *
     * @return the text
     */
    public String[] getText() {
        return text;
    }

    /**
     * Set the text.
     *
     * @param text the text to set
     */
    public void setText(String[] text) {
        if (text == null) {
            throw new IllegalArgumentException("Can't set null text for a sign");
        }
        this.text = text;
    }

    @Override
    public String getNbtId() {
        return "minecraft:sign";
    }

    @Override
    public CompoundBinaryTag getNbt() {
        return CompoundBinaryTag
            .builder()
            .put("Text1", StringBinaryTag.of(text[0]))
            .put("Text2", StringBinaryTag.of(text[1]))
            .put("Text3", StringBinaryTag.of(text[2]))
            .put("Text4", StringBinaryTag.of(text[3]))
            .build();
    }
}
