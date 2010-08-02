//package com.rapidftr.controllers;
//
//import com.rapidftr.model.Child;
//import com.rapidftr.screens.UiStack;
//import com.rapidftr.screens.ViewChildScreen;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
//public class ViewChildControllerTest {
//    private UiStack uiStack;
//    private ViewChildScreen viewChildScreen;
//
//    @Before
//    public void setUp() {
//        uiStack = mock(UiStack.class);
//        viewChildScreen = mock(ViewChildScreen.class);
//    }
//
//    @Test
//    public void should_initalise_and_display_the_view_child_form() {
//        ViewChildController controller = new ViewChildController(viewChildScreen, uiStack);
//
//        Child child = new Child("Wai");
//        controller.show(child);
//        verify(viewChildScreen).setChild(child);
//        verify(uiStack).pushScreen(viewChildScreen);
//
//    }
//}
