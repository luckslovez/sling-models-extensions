package luckslovez.sling.models.extensions.injectors.annotations;

import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@InjectAnnotation
@Source("suffix-parameter")
public @interface SuffixParameter {

    /** Field name, if empty the annotated field name will be used.
     *
     * @return name
     */
    String name() default "";

    /** This annotation is meant to inject suffix parameters. Thus it's {@link InjectionStrategy} defaults to OPTIONAL.
     *
     * @return the {@link InjectionStrategy}
     */
    InjectionStrategy injectionStrategy() default InjectionStrategy.OPTIONAL;

}
