package me.luckslovez.sling.models.extensions.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.ImmutableMap;

import me.luckslovez.sling.models.extensions.services.MapFactory;

class MapFactoryImplTest {

    private static final String SEPARATOR = "separator";
    private static final String KEY_VALUES_ARRAY = "keyValuesArray";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    private MapFactory mapFactory = new MapFactoryImpl();

    private static Stream<Map<String, String[]>> testKeyValuesToMap() {
        return Stream.of(
                ImmutableMap.of(SEPARATOR, new String[] { "=" }, KEY_VALUES_ARRAY, new String[] { KEY + "=" + VALUE }),
                ImmutableMap.of(SEPARATOR, new String[] { "&" }, KEY_VALUES_ARRAY, new String[] { KEY + "&" + VALUE })
        );
    }

    @ParameterizedTest
    @MethodSource
    void testKeyValuesToMap(Map<String, String[]> input) {
        Map<String, Object> test = mapFactory.keyValuesToMap(input.get(KEY_VALUES_ARRAY), input.get(SEPARATOR)[0]);

        assertEquals(VALUE, test.get(KEY));
    }

}