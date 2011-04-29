package com.rapidftr.controllers;

import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ViewChildrenControllerTest {


    private ViewChildrenScreen viewChildrenScreen;
    private UiStack uiStack;
    private ChildrenRecordStore recordStore;
    private ViewChildrenController controller;

    @Before
    public void setup() {
        viewChildrenScreen = mock(ViewChildrenScreen.class);
        uiStack = mock(UiStack.class);
        recordStore = mock(ChildrenRecordStore.class);
        controller = new ViewChildrenController(this.viewChildrenScreen, uiStack, recordStore);
    }

    @Test
    public void shouldShowChildrenSortedByName() {
        Children children = new Children(new Child[]{new Child("10/10/2010"), new Child("10/10/2011")});

        when(recordStore.getAllSortedByName()).thenReturn(children);
        controller.sortByName();
        verify(viewChildrenScreen).setChildren(children);
    }

    @Test
    public void shouldShowChildrenSortedByRecentlyAdded() {
        Children children = new Children(new Child[]{new Child("10/10/2010"), new Child("10/10/2011")});

        when(recordStore.getAllSortedByRecentlyAdded()).thenReturn(children);
        controller.sortByRecentlyAdded();
        verify(viewChildrenScreen).setChildren(children);
    }

    @Test
    public void shouldShowChildrenSortedByRecentlyUpdated() {
        Children children = new Children(new Child[]{new Child("10/10/2010"), new Child("10/10/2011")});

        when(recordStore.getAllSortedByRecentlyUpdated()).thenReturn(children);
        controller.sortByRecentlyUpdated();
        verify(viewChildrenScreen).setChildren(children);
    }

    @Test
    public void shouldViewAllChildrenSortedByLastSortState() {
        Children childrenSortedByName = new Children(new Child[]{new Child("10/10/2010"), new Child("10/10/2011")});
        Children childrenSortedByRecentAddition = new Children(new Child[]{new Child("12/11/2011"), new Child("11/11/2011")});

        when(recordStore.getAllSortedByName()).thenReturn(childrenSortedByName);
        when(recordStore.getAllSortedByRecentlyAdded()).thenReturn(childrenSortedByRecentAddition);

        controller.sortByName();
        verify(viewChildrenScreen).setChildren(childrenSortedByName);

        controller.sortByRecentlyAdded();
        controller.viewAllChildren();
        verify(viewChildrenScreen, times(2)).setChildren(childrenSortedByRecentAddition);
    }
}
