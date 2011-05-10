package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildBuilder;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SearchChildControllerTest {

    private SearchChildScreen searchChildScreen;
    private UiStack uiStack;
    private ChildrenRecordStore recordStore;
    private Dispatcher dispatcher;

    private SearchChildController controller;

    @Before
    public void setup() {
        searchChildScreen = mock(SearchChildScreen.class);
        uiStack = mock(UiStack.class);
        recordStore = mock(ChildrenRecordStore.class);
        dispatcher = mock(Dispatcher.class);

        controller = new SearchChildController(searchChildScreen, uiStack, recordStore, dispatcher);
    }

    @Test
    public void shouldSearchChildrenBasedOnCertainQueryAndDisplayThemSortedByName() {
        String query = "some search query";

        Child firstChild = new ChildBuilder().withName("aaaaaa").build();
        Child secondChild = new ChildBuilder().withName("cccccc").build();
        Child thirdChild = new ChildBuilder().withName("bbbbbb").build();

        Children children = mock(Children.class);
        Children childrenSortedByName = new Children(new Child[]{firstChild, thirdChild, secondChild});

        when(recordStore.search(query)).thenReturn(children);
        when(children.count()).thenReturn(1);
        when(children.sortByName()).thenReturn(childrenSortedByName);

        controller.searchAndDisplayChildren(query);

        verify(recordStore).search(query);
        verify(dispatcher).viewChildren(childrenSortedByName);
    }

    @Test
    public void shouldShowNoSearchResultAlertWhenNoChildrenFound() {
        String query = "some search query";
        Children emptyChildren = new Children(new Child[0]);

        when(recordStore.search(query)).thenReturn(emptyChildren);
        controller.searchAndDisplayChildren(query);
        verify(searchChildScreen).showNoSearchResultsAlert();
    }
}
