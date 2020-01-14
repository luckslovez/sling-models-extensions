package me.luckslovez.sling.models.extensions.injectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import me.luckslovez.sling.models.extensions.injectors.annotations.ResourceChildren;

@ExtendWith(SlingContextExtension.class)
class ResourceChildrenInjectorTest {

    private static final String CHILD = "child";
    private static final String PROP = "prop";

    private final SlingContext context = new SlingContext();

    private Resource testResource;

    @BeforeEach
    public void setUp() {
        context.addModelsForPackage("me.luckslovez.sling.models.extensions.injectors");
        context.registerInjectActivateService(new ResourceChildrenInjector());
        testResource = context.create().resource("/resource");
    }

    @Test
    public void testResourceList() {
        context.create().resource("/resource/child", Collections.singletonMap(PROP, PROP));
        ResourceListModel testModel = context.getService(ModelFactory.class).createModel(testResource, ResourceListModel.class);

        assertNotNull(testModel);
        assertEquals(CHILD, testModel.children.get(0).getName());
        assertEquals(PROP, testModel.children.get(0).getValueMap().get(PROP, String.class));
    }

    @Test
    public void testSimpleModelList() {
        context.create().resource("/resource/children/" + CHILD, Collections.singletonMap(PROP, PROP));
        SimpleModelListModel testModel = context.getService(ModelFactory.class).createModel(testResource, SimpleModelListModel.class);

        assertNotNull(testModel);
        assertEquals(PROP, testModel.children.get(0).prop);
    }

    @Test
    public void testChildResourceSimpleModelListModel() {
        context.create().resource("/resource/relative/path/" + CHILD, Collections.singletonMap(PROP, PROP));
        ChildResourceSimpleModelListModel testModel = context.getService(ModelFactory.class)
                .createModel(testResource, ChildResourceSimpleModelListModel.class);

        assertNotNull(testModel);
        assertEquals(PROP, testModel.children.get(0).prop);
    }

    @Model(adaptables = Resource.class)
    public static final class ResourceListModel {
        @ResourceChildren(name = "")
        public List<Resource> children;
    }

    @Model(adaptables = Resource.class)
    public static final class SimpleModelListModel {
        @ResourceChildren(name = "children")
        public List<SinglePropModel> children;
    }

    @Model(adaptables = Resource.class)
    public static final class ChildResourceSimpleModelListModel {
        @ResourceChildren(name = "relative/path")
        public List<SinglePropModel> children;
    }

    @Model(adaptables = Resource.class)
    public static final class SinglePropModel {
        @ValueMapValue
        public String prop;
    }

}