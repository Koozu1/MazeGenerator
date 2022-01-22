package net.koozumaa.mazegenerator.Utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Map;


public class LocationJsonDeserializer  extends StdDeserializer<Location> {

    public LocationJsonDeserializer(){
        this(null);
    }

    public LocationJsonDeserializer(Class<?> clazz){
        super(clazz);
    }

    @Override
    public Location deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {

        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        int x = Integer.parseInt(node.get("x").asText());
        int z = Integer.parseInt(node.get("z").asText());
        return new Location(null, x, 0, z);

    }
}
