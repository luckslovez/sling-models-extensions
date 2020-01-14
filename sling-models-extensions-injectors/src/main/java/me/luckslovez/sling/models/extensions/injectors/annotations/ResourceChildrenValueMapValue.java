package me.luckslovez.sling.models.extensions.injectors.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotation;

@Target(FIELD)
@Retention(RUNTIME)
@InjectAnnotation
@Source("resource-children-valuemapvalue")
public @interface ResourceChildrenValueMapValue {

    /**
     * Name of the child resource to get children from. Note this can be a relative path.
     * If empty, the adaptable resource will be used.
     *
     * @return name
     */
    String name();

    /**
     * Name of the property to be extracted from child resources.
     *
     * @return name
     */
    String property();

    /**
     * @return the {@link InjectionStrategy}
     * @see <a href="https://sling.apache.org/documentation/bundles/models.html#optional-and-required">Sling Models Injection Strategy</a>
     */
    InjectionStrategy injectionStrategy() default InjectionStrategy.DEFAULT;

}
