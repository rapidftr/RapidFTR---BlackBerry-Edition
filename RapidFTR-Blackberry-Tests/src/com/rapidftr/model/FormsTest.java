package com.rapidftr.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Vector;

import org.junit.Test;

import com.rapidftr.screens.ManageChildScreen;

public class FormsTest {

	@Test
	public void initiliazeLayOutOnFormsShouldCallInitializeLayOutOnFormObjects() {
		final Form mockForm = mock(Form.class);
		ManageChildScreen screen = mock(ManageChildScreen.class);
		Vector formsVector = new Vector() {
			{
				add(mockForm);
			}
		};
		Forms forms = new Forms(formsVector);
		forms.initializeLayout(screen);
		verify(mockForm).initializeLayout(screen);
	}

	@Test
	public void toArrayShouldRetrunAnArrayOfAllTheForms() {
		final Form form = new Form("testForm", "testForm", new Vector());
		Vector formsVector = new Vector() {
			{
				add(form);
			}
		};
		Forms forms = new Forms(formsVector);
		assertEquals(form, forms.toArray()[0]);
	}

	@Test
	public void isEmptyWhenCalledOnEmptyFormsShouldBeTrue() {
		Forms forms = new Forms(new Vector());
		assertTrue(forms.isEmpty());
	}

	@Test
	public void isEmptyWhenCalledOnAFormsWithAnEmptyFormShouldBeTrue() {
		final Form form = mock(Form.class);
		when(form.isEmpty()).thenReturn(true);
		Vector formsVector = new Vector() {
			{
				add(form);
			}
		};
		Forms forms = new Forms(formsVector);
		assertTrue(forms.isEmpty());
	}

	@Test
	public void isEmptyWhenCalledOnAFormsWithANonEmptyFormShouldBeFalse() {
		final Form form = mock(Form.class);
		final Form emptyForm = mock(Form.class);
		when(form.isEmpty()).thenReturn(false);
		when(emptyForm.isEmpty()).thenReturn(true);
		ManageChildScreen screen = mock(ManageChildScreen.class);
		Vector formsVector = new Vector() {
			{
				add(emptyForm);
				add(form);
			}
		};
		Forms forms = new Forms(formsVector);
		assertFalse(forms.isEmpty());
	}
}
