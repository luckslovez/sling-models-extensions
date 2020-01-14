package me.luckslovez.sling.models.extensions.injectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import me.luckslovez.sling.models.extensions.injectors.annotations.ResourceChildrenValueMapValue;

@ExtendWith(SlingContextExtension.class)
class ResourceChildrenValueMapValueInjectorTest {

    private static final String PROP = "prop";
    private final Calendar CALENDAR = Calendar.getInstance();

    private final SlingContext context = new SlingContext();

    private Resource testResource;

    @BeforeEach
    public void setUp() {
        context.addModelsForClasses("me.luckslovez.sling.models.extensions.injectors");
        context.registerInjectActivateService(new ResourceChildrenValueMapValueInjector());
        testResource = context.create().resource("/resource");
    }

    @Test
    public void testResourceChildrenStringProperty() {
        context.create().resource("/resource/child/resource", Collections.singletonMap(PROP, PROP));
        StringPropModel testModel = context.getService(ModelFactory.class).createModel(testResource, StringPropModel.class);

        assertEquals(Collections.singletonList(PROP), testModel.property);
    }

    @Model(adaptables = Resource.class)
    public static final class StringPropModel {
        @ResourceChildrenValueMapValue(name = "child", property = PROP)
        public List<String> property;
    }

    @Test
    public void testResourceChildrenCalendarProperty() {
        context.create().resource("/resource/child/resource", Collections.singletonMap(PROP, CALENDAR));
        CalendarPropModel testModel = context.getService(ModelFactory.class).createModel(testResource, CalendarPropModel.class);

        assertEquals(Collections.singletonList(CALENDAR), testModel.property);
    }

    @Model(adaptables = Resource.class)
    public static final class CalendarPropModel {
        @ResourceChildrenValueMapValue(name = "child", property = PROP)
        public List<Calendar> property;
    }

}