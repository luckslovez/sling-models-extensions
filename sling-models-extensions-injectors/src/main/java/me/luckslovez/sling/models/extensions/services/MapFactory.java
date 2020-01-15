package me.luckslovez.sling.models.extensions.services;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public interface MapFactory {

    /**
     * Given a {@link String[]} of <i>key{separator}value</i>, splits each by <i>{separator}</i> and builds a map.
     *
     * @param keyValues {@link String[]} where each element contains a key, a separator and a value
     * @param separator separator used to split key an value
     * @return respective Map
     */
    @NotNull
    Map<String, Object> keyValuesToMap(@NotNull String[] keyValues, @NotNull String separator);

}
