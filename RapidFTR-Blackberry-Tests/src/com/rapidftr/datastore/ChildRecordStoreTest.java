package com.rapidftr.datastore;

import com.rapidftr.Key;
import com.rapidftr.form.Forms;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.utilities.ChildSorter;
import org.json.me.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ChildRecordStoreTest {

    private ChildrenRecordStore childrenStore;
    private Forms forms;
    @Mock
    private ChildSorter childSorter;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        childrenStore = new ChildrenRecordStore(new MockStore(new Key("children")), childSorter);
        forms = new Forms(new JSONArray("[{'fields':[{'name':'name','enabled':true,'type':'text_field','display_name':'Name'}]}]"));
    }

    @Test
    public void saveShouldAppendNewChildAndSaveInStore() {
        int count = childrenStore.getAll().count();
        childrenStore.addOrUpdate(ChildFactory.newChild());
        childrenStore.addOrUpdate(ChildFactory.newChild());
        assertEquals(count + 2, childrenStore.getAll().count());
    }

    @Test
    public void saveShouldUpdateTheChildIfItExists() {
        Child childOne = ChildFactory.newChild();
        childOne.setField("name", "NewChild");
        childOne.setField("_id", "1");
        childrenStore.addOrUpdate(childOne);
        int initial = childrenStore.getAll().count();

        Child updatedChildOne = ChildFactory.newChild();
        updatedChildOne.setField("name", "UpdatedChild");
        updatedChildOne.setField("_id", "1");
        childrenStore.addOrUpdate(updatedChildOne);

        assertEquals(initial, childrenStore.getAll().count());
    }

    @Test
    public void ifPersistentStoreIsEmptySearchShouldResultAnEmptyArray() {
        assertEquals(childrenStore.search("Tom").count(), 0);
    }

    @Test
    public void ifChildIsNotPresentInPersistentStoreSearchShouldReturnEmptyResults() {
        assertEquals(childrenStore.search("Harry").count(), 0);
    }

    @Test
    public void ifChildWithNamePresentInPersistentStoreSearchShouldReturnTheChild() {
        Child Tom = ChildFactory.newChild();
        String childName = "Tom";
        Tom.setField("name", childName);
        childrenStore.addOrUpdate(Tom);

        Children searchResults = childrenStore.search(childName);

        assertEquals(searchResults.count(), 1);
        assertEquals(searchResults.toArray()[0], Tom);
    }

    @Test
    public void searchShouldReturnAllTheChildrenWithSearchedName() {
        String childName = "Tom";
        Child child = ChildFactory.newChild();
        child.setField("name", childName);
        childrenStore.addOrUpdate(child);

        Child child2 = ChildFactory.newChild();
        child2.setField("name", childName);
        childrenStore.addOrUpdate(child2);

        Children searchResults = childrenStore.search(childName);

        assertEquals(searchResults.count(), 2);
        assertEquals(searchResults.toArray()[0], child);
        assertEquals(searchResults.toArray()[1], child2);
    }

    @Test
    public void ifChildWithIdPresentInPersistentStoreSearchShouldReturnTheChild() {
        Child child = ChildFactory.newChild();
        child.setField("name", "Tom");
        String childUID = "1";
        child.setField("unique_identifier", childUID);
        childrenStore.addOrUpdate(child);

        Children searchResults = childrenStore.search(childUID);

        assertEquals(searchResults.count(), 1);
        assertEquals(searchResults.toArray()[0], child);
    }

    @Test
    public void ifNameofOneChildAndIdOfOtherAreEqualSearchShouldReturnBothTheChildren() {
        String childNameAndUID = "1";

        Child child = ChildFactory.newChild();
        child.setField("name", childNameAndUID);
        childrenStore.addOrUpdate(child);

        Child child2 = ChildFactory.newChild();
        child2.setField("unique_identifier", childNameAndUID);
        childrenStore.addOrUpdate(child2);

        Children searchResults = childrenStore.search(childNameAndUID);

        assertEquals(searchResults.count(), 2);
        assertEquals(searchResults.toArray()[0], child);
        assertEquals(searchResults.toArray()[1], child2);
    }

    @Test
    public void shouldSortChildrenByRecentlyAdded() {
        Child child = ChildFactory.newChild("2010-11-2 01:00:00GMT");
        child.setField("name", "child1");
        childrenStore.addOrUpdate(child);

        childrenStore.getAllSortedByRecentlyAdded();
        verify(childSorter).sort(new Child[]{child}, false, new DateField(Child.CREATED_AT_KEY));
    }

    @Test
    public void shouldSortChildrenByName() {
        Child child = ChildFactory.newChild("2010-11-2 01:00:00GMT");
        child.setField("name", "child1");
        childrenStore.addOrUpdate(child);

        childrenStore.getAllSortedByName();
        verify(childSorter).sort(new Child[]{child}, true, new StringField(Child.NAME));
    }

    @Test
    public void shouldSortChildrenBasedOnRecentlyUpdated() {
        Child child = ChildFactory.newChild("2010-11-2 01:00:00GMT");
        child.setField("name", "child1");
        childrenStore.addOrUpdate(child);

        Child secondChild = ChildFactory.newChild("2010-11-2 01:00:00GMT");
        secondChild.setField("name", "child2");
        childrenStore.addOrUpdate(secondChild);

        secondChild.update(forms, "2010-11-2 04:00:00GMT");
        childrenStore.addOrUpdate(secondChild);

        childrenStore.getAllSortedByRecentlyUpdated();
        verify(childSorter).sort(new Child[]{child, secondChild}, false, new DateField(Child.LAST_UPDATED_KEY));
    }

}
