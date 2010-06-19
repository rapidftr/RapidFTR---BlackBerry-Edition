package com.rapidftr.controllers;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;

public class LoginControllerTest {

	@Test
	public void should_initalise_and_display_the_login_form(){
		LoginScreen loginScreen = mock(LoginScreen.class);
		UiStack uiStack = mock(UiStack.class);
		
		LoginController controller = new LoginController(loginScreen, uiStack);
		
		controller.show();
		verify(uiStack).pushScreen(loginScreen);
		
	}
}
