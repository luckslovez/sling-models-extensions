package me.luckslovez.sling.models.extensions.services.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;

import me.luckslovez.sling.models.extensions.services.MapFactory;

@Component
public class MapFactoryImpl implements MapFactory {

    @Override
    @NotNull
    public Map<String, Object> keyValuesToMap(@NotNull String[] keyValues, @NotNull String separator) {
        return Arrays.stream(keyValues)
                .collect(Collectors.toMap(keyValue -> StringUtils.substringBefore(keyValue, separator), keyValue -> StringUtils.substringAfter(keyValue, separator)));
    }

}
