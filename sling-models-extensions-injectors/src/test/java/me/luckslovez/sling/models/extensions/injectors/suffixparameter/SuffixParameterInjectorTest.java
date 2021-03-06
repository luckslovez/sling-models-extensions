package me.luckslovez.sling.models.extensions.injectors.suffixparameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import me.luckslovez.sling.models.extensions.injectors.SuffixParameterInjector;
import me.luckslovez.sling.models.extensions.injectors.annotations.SuffixParameter;
import me.luckslovez.sling.models.extensions.services.MapFactory;
import me.luckslovez.sling.models.extensions.services.impl.MapFactoryImpl;

@ExtendWith(SlingContextExtension.class)
public class SuffixParameterInjectorTest {

    private final SlingContext context = new SlingContext();

    @BeforeEach
    public void setUp() {
        context.addModelsForClasses(NoName.class);
        context.addModelsForClasses(CustomName.class);
        context.registerService(MapFactory.class, new MapFactoryImpl());
        context.registerInjectActivateService(new SuffixParameterInjector());
        context.requestPathInfo().setSuffix("/param=value");
    }

    @Test
    public void testDefaultFieldName() {
        NoName noNameModel = context.getService(ModelFactory.class).createModel(context.request(), NoName.class);

        assertNotNull(noNameModel);
        assertEquals("value", noNameModel.param);
    }

    @Test
    public void testCustomName() {
        CustomName customNameModel = context.getService(ModelFactory.class).createModel(context.request(), CustomName.class);

        assertNotNull(customNameModel);
        assertEquals("value", customNameModel.customName);
    }

    @Test
    public void testCustomSeparators() {
        context.requestPathInfo().setSuffix("/prop:value&another:value");
        CustomSeparators customSeparators = context.getService(ModelFactory.class).createModel(context.request(), CustomSeparators.class);

        assertNotNull(customSeparators);
        assertEquals("value", customSeparators.prop);
        assertEquals("value", customSeparators.another);
    }

    @Model(adaptables = SlingHttpServletRequest.class)
    public static class NoName {
        @SuffixParameter
        public String param;
    }

    @Model(adaptables = SlingHttpServletRequest.class)
    public static class CustomName {
        @SuffixParameter(name = "param")
        public String customName;
    }

    @Model(adaptables = SlingHttpServletRequest.class)
    public static class CustomSeparators {
        @SuffixParameter(suffixSplitSeparator = "&", keyValueSplitSeparator = ":")
        public String prop;
        @SuffixParameter(suffixSplitSeparator = "&", keyValueSplitSeparator = ":")
        public String another;
    }

}