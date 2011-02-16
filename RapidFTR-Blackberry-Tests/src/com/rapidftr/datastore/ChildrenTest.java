package com.rapidftr.datastore;

import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ChildrenTest {

    @Test
    public void shouldIncludeNewChildInTotalForUpload() {
        Children children = new Children(new Child[] { ChildFactory.newChild() });
        assertEquals(1, children.getChildrenToUpload().count());
    }

    @Test
    public void shouldIncludeUpdatedChildInTotalForUpload() {
        final Child child = ChildFactory.existingChild("id");
        child.setField("afield", "original");
        child.setField("afield", "update");
        Children children = new Children(new Child[] {child});
        assertEquals(1, children.getChildrenToUpload().count());
    }
}
