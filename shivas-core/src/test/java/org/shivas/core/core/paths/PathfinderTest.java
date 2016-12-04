package org.shivas.core.core.paths;

import org.junit.BeforeClass;
import org.junit.Test;
import org.shivas.core.database.ShivasEntityFactory;
import org.shivas.data.Container;
import org.shivas.data.Repository;
import org.shivas.data.entity.MapTemplate;
import org.shivas.data.loader.XmlLoader;
import org.shivas.protocol.client.enums.OrientationEnum;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.shivas.protocol.client.enums.OrientationEnum.NORTH_EAST;
import static org.shivas.protocol.client.enums.OrientationEnum.NORTH_WEST;

/**
 * @author Blackrush <blackrushx@gmail.com>
 */
public class PathfinderTest {

    private static Repository<MapTemplate> mapTemplates;

    @BeforeClass
    public static void setUpClass() {
        XmlLoader loader = new XmlLoader(new ShivasEntityFactory());
        loader.load(MapTemplate.class, String.join(File.separator, "..", "data", "maps"));
        Container container = loader.create();
        mapTemplates = container.get(MapTemplate.class);
    }


    @Test
    public void find() throws Exception {
        MapTemplate map = mapTemplates.byId(7412);

//        FIXME more similar path calculation to client
//        assertPath(294, 250, NORTH_EAST, map,
//                Node.of(279, NORTH_EAST),
//                Node.of(264, NORTH_WEST),
//                Node.of(250, NORTH_WEST));

        assertPath(307, 264, NORTH_EAST, map,
                Node.of(293, NORTH_EAST),
                Node.of(279, NORTH_EAST),
                Node.of(264, NORTH_WEST));
    }

    private static void assertPath(int start, int end, OrientationEnum dir, MapTemplate map, Node... nodes) throws PathNotFoundException {
        Pathfinder pathfinder = new Pathfinder((short) start, (short) end, dir, map, false);
        Path path = pathfinder.find();
        assertEquals("path incomplete", nodes.length, path.size());
        for (int i = 0; i < nodes.length; i++) {
            assertEquals(th(i+1) + " node", nodes[i], path.get(i));
        }
    }

    private static String th(int i) {
        switch (i % 10) {
            case 1:
                return i + "st";
            case 2:
                return i + "nd";
            case 3:
                return i + "rd";
            default:
                return i + "th";
        }
    }

}