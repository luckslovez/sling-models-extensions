package me.luckslovez.sling.models.extensions.services;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface MapFactory {

    /**
     * Given a {@link String[]} of <i>key{separator}value</i>, splits each by <i>{separator}</i> and builds a map.
     *
     * @param keyValues
     * @return respective map
     */
    @NotNull
    Map<String, Object> keyValuesToMap(@NotNull String[] keyValues, @NotNull String separator);

}
