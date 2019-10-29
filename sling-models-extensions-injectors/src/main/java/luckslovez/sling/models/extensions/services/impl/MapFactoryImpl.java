package luckslovez.sling.models.extensions.services.impl;

import luckslovez.sling.models.extensions.services.MapFactory;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapFactoryImpl implements MapFactory {

    @Override
    public Map<String, Object> keyValuesToMap(String[] keyValues) {
        return Arrays.stream(keyValues)
                .collect(Collectors.toMap(keyValue -> StringUtils.substringBefore(keyValue, "="), keyValue -> StringUtils.substringAfter(keyValue, "=")));
    }

}
