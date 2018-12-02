package com.github.invictum.reportportal.handler;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.LogUnitsHolder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.thucydides.core.model.Story;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class FlatHandlerTest {

    @Mock
    private Launch launchMock;

    @Mock
    private LogUnitsHolder logUnitsHolderMock;

    private Injector injector;

    @Before
    public void prepareInjection() {
        TestHandlerModule module = new TestHandlerModule(launchMock, logUnitsHolderMock);
        injector = Guice.createInjector(module);
    }

    @Test
    public void startClassSuite() {
        // Execute
        FlatHandler handler = injector.getInstance(FlatHandler.class);
        handler.startSuite(FakeTestClass.class);
        handler.finishSuite();
        // Verification
        Mockito.verify(launchMock).startTestItem(ArgumentMatchers.argThat(item ->
                item.getName().equals("FakeTestClass")
                        && item.getType().equals("TEST")
                        && item.getStartTime() != null));
    }

    @Test
    public void startStorySuite() {
        // Mock setup
        Story storyMock = Mockito.mock(Story.class);
        Mockito.when(storyMock.getDisplayName()).thenReturn("Story");
        Mockito.when(storyMock.getNarrative()).thenReturn("Narrative");
        // Execute
        FlatHandler handler = injector.getInstance(FlatHandler.class);
        handler.startSuite(storyMock);
        handler.finishSuite();
        // Verification
        Mockito.verify(launchMock).startTestItem(ArgumentMatchers.argThat(item ->
                item.getName().equals("Story")
                        && item.getDescription().equals("Narrative")
                        && item.getType().equals("TEST")
                        && item.getStartTime() != null));
    }

    @Test
    public void finishSuite() {
        FlatHandler handler = injector.getInstance(FlatHandler.class);
        handler.startSuite(Mockito.mock(Story.class));
        handler.finishSuite();
        // Verification
        Mockito.verify(launchMock).finishTestItem(Mockito.any(), ArgumentMatchers.argThat(item ->
                item.getStatus().equals("PASSED") && item.getEndTime() != null
        ));
    }

    @Test
    public void startTest() {
        // Execute
        FlatHandler handler = injector.getInstance(FlatHandler.class);
        handler.startSuite(FakeTestClass.class);
        handler.startTest("Test name");
        // Verification
        Mockito.verify(launchMock).startTestItem(Mockito.any(), ArgumentMatchers.argThat(item ->
                item.getName().equals("Test name")
                        && item.getType().equals("STEP")
                        && item.getStartTime() != null));
    }
}
