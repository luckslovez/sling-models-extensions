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
@Source("suffix-parameter")
public @interface SuffixParameter {

    /**
     * Field name, if empty the annotated field name will be used.
     *
     * @return name
     */
    String name() default "";

    /**
     * Key, if empty defaults to <i>=</i>
     *
     * @return keyValueSeparator
     */
    String suffixSplitSeparator() default "/";

    /**
     * Key/value separator, if empty defaults to <i>=</i>
     *
     * @return splitSeparator
     */
    String keyValueSplitSeparator() default "=";

    /**
     * This annotation is meant to inject suffix parameters. Thus it's {@link InjectionStrategy} defaults to OPTIONAL.
     *
     * @return the {@link InjectionStrategy}
     */
    InjectionStrategy injectionStrategy() default InjectionStrategy.OPTIONAL;

}
