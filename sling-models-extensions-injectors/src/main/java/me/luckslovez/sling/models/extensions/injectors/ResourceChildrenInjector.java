package me.luckslovez.sling.models.extensions.injectors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.spi.DisposalCallbackRegistry;
import org.apache.sling.models.spi.Injector;
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessorFactory2;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;

import me.luckslovez.sling.models.extensions.injectors.annotations.ResourceChildren;

@Component
public class ResourceChildrenInjector implements Injector, InjectAnnotationProcessorFactory2 {

    @Override
    public String getName() {
        return "resource-children";
    }

    @Override
    public Object getValue(@NotNull Object adaptable, @NotNull String fieldName, @NotNull Type type,
            @NotNull AnnotatedElement annotatedElement, @NotNull DisposalCallbackRegistry disposalCallbackRegistry) {

        final ResourceChildren resourceChildrenAnnotation = annotatedElement.getAnnotation(ResourceChildren.class);

        if (resourceChildrenAnnotation != null) {
            final Resource resource = adaptable instanceof SlingHttpServletRequest
                    ? ((SlingHttpServletRequest) adaptable).getResource()
                    : (Resource) adaptable;

            final String name = resourceChildrenAnnotation.name();

            final Optional<Class> adapter = Optional.of(annotatedElement)
                    .map(Field.class::cast)
                    .map(Field::getGenericType)
                    .flatMap(this::resolveClass)
                    .filter(clazz -> clazz != Resource.class);

            Stream<Resource> result;

            if (StringUtils.isBlank(name)) {
                result = StreamSupport.stream(resource.getChildren().spliterator(), false);
            } else if (resource.getChild(name) != null) {
                result = StreamSupport.stream(resource.getChild(name).getChildren().spliterator(), false);
            } else {
                result = Stream.empty();
            }

            return adapter.isPresent()
                    ? result
                    .map(child -> child.adaptTo(adapter.get()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
                    : result.collect(Collectors.toList());
        }

        return null;
    }

    private Optional<Class> resolveClass(Type type) {
        return Optional.of(type)
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .map(ParameterizedType::getActualTypeArguments)
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .findAny()
                .map(Class.class::cast);
    }

    @Override
    public InjectAnnotationProcessor2 createAnnotationProcessor(@NotNull Object adaptable, @NotNull AnnotatedElement annotatedElement) {

        return Optional.of(annotatedElement)
                .map(element -> element.getAnnotation(ResourceChildren.class))
                .map(ChildResourceChildrenProcessor::new)
                .orElse(null);

    }

    private static class ChildResourceChildrenProcessor extends AbstractInjectAnnotationProcessor2 {

        private final ResourceChildren annotation;

        ChildResourceChildrenProcessor(@NotNull ResourceChildren annotation) {
            this.annotation = annotation;
        }

        @Override
        public String getName() {
            return annotation.name();
        }

        @Override
        public InjectionStrategy getInjectionStrategy() {
            return annotation.injectionStrategy();
        }

    }

}
