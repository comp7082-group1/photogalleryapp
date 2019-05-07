package com.example.photogalleryapp;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

    private static final String TEST_STRING = "Hello World!";
    //As we don't have access to Context in our JUnit test classes, we need to mock it
    @Mock
    Context mMockContext;

    @Test
    public void readStringFromContext() {
        //Returns the TEST_STRING when getString(R.string.hello_world) is called
        when(mMockContext.getString(R.string.text_hello_word)).thenReturn(TEST_STRING);
        //Creates an object of the ClassUnderTest with the mock context
        ClassUnderTest objectUnderTest = new ClassUnderTest(mMockContext);
        //Stores the return value of getHelloWorldString() in result
        String result = objectUnderTest.getHelloWorldString();
        //Asserts that result is the value of TEST_STRING
        assertThat(result, is(TEST_STRING));
    }
}

