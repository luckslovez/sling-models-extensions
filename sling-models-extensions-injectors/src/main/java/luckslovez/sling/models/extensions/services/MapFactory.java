package luckslovez.sling.models.extensions.services;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface MapFactory {

    @NotNull
    Map<String, Object> keyValuesToMap(@NotNull String[] keyValues);

}
