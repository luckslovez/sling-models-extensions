package me.luckslovez.sling.models.extensions.injectors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.spi.DisposalCallbackRegistry;
import org.apache.sling.models.spi.Injector;
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessorFactory2;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import me.luckslovez.sling.models.extensions.injectors.annotations.SuffixParameter;
import me.luckslovez.sling.models.extensions.services.MapFactory;

@Component
public class SuffixParameterInjector implements Injector, InjectAnnotationProcessorFactory2 {

    @Reference
    private MapFactory mapFactory;

    @Override
    @NotNull
    public String getName() {
        return "suffix-parameter";
    }

    @Override
    public Object getValue(@NotNull Object adaptable, @NotNull String fieldName, @NotNull Type type,
            @NotNull AnnotatedElement annotatedElement, @NotNull DisposalCallbackRegistry disposalCallbackRegistry) {

        final SuffixParameter suffixParameterAnnotation = annotatedElement.getAnnotation(SuffixParameter.class);

        if (suffixParameterAnnotation != null) {
            final String name = Optional.of(suffixParameterAnnotation.name())
                    .filter(StringUtils::isNotEmpty)
                    .orElseGet(() -> Optional.of(annotatedElement)
                            .map(Field.class::cast)
                            .map(Field::getName)
                            .orElse(null));

            final String suffixSplitSeparator = suffixParameterAnnotation.suffixSplitSeparator();
            final String keyValueSplitSeparator = suffixParameterAnnotation.keyValueSplitSeparator();

            return name != null
                    ? Optional.of(adaptable)
                    .filter(SlingHttpServletRequest.class::isInstance)
                    .map(SlingHttpServletRequest.class::cast)
                    .map(SlingHttpServletRequest::getRequestPathInfo)
                    .map(RequestPathInfo::getSuffix)
                    .map(suffix -> StringUtils.substring(suffix, 1))
                    .map(suffix -> StringUtils.split(suffix, suffixSplitSeparator))
                    .map((String[] keyValues) -> mapFactory.keyValuesToMap(keyValues, keyValueSplitSeparator))
                    .map(map -> map.get(name))
                    .orElse(null)
                    : "";
        }

        return null;

    }

    @Override
    public InjectAnnotationProcessor2 createAnnotationProcessor(@NotNull Object adaptable, @NotNull AnnotatedElement annotatedElement) {

        return Optional.of(annotatedElement)
                .map(element -> element.getAnnotation(SuffixParameter.class))
                .map(RequestParameterAnnotationProcessor::new)
                .orElse(null);

    }

    private static class RequestParameterAnnotationProcessor extends AbstractInjectAnnotationProcessor2 {

        private final SuffixParameter annotation;

        RequestParameterAnnotationProcessor(@NotNull SuffixParameter annotation) {
            this.annotation = annotation;
        }

        @Override
        public InjectionStrategy getInjectionStrategy() {
            return annotation.injectionStrategy();
        }

        @Override
        public String getName() {
            return annotation.name();
        }

    }

}
