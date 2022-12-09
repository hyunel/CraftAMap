import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyun.craftamap.elements.base.BlockVector2Deserializer;
import com.hyun.craftamap.elements.Elements;
import com.sk89q.worldedit.math.BlockVector2;

import java.io.IOException;
import java.io.InputStreamReader;

public class ElementsReadFromFileTest {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BlockVector2.class, new BlockVector2Deserializer())
                .create();

        try(var mapData = ElementsReadFromFileTest.class.getResourceAsStream("hpu.json")) {
            assert mapData != null;
            Elements elements = gson.fromJson(new InputStreamReader(mapData), Elements.class);
            System.out.println(elements);
        }
    }
}
