package net.koozumaa.mazegenerator.Utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import org.bukkit.Location;

import java.io.IOException;

public class LocationJsonDeserializer  extends JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonParser parser, DeserializationContext deserializer)
            throws IOException, JsonProcessingException {

        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        int x = node.get(0).asInt();
        int z = node.get(1).asInt();
        return new Location(null, x, 0, z);
    }
}
