package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary, mockSecondary;

  @BeforeEach
  public void init(){
    // Create mocks for the dependency TorpedoStore.
    mockPrimary = mock(TorpedoStore.class);
    mockSecondary = mock(TorpedoStore.class);

    this.ship = new GT4500(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    // Set the behavior of the mocks: if they are called with
    // parameter 1 then return the value true; if isEmpty is called,
    // they return false.
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Verifying the mock: mockPrimary.fire(1) was called exactly once,
    // while mockSecondary.fire(1) was not called,
    // as it should be in GT4500's initial state and with the set mock behaviors.
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    // Set the behavior of the mocks: if they are called with
    // parameter 1 then the first returns the value false, the second returns the value true.
    when(mockPrimary.fire(1)).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    // Verifying the mock: both mockPrimary.fire(1) and mockSecondary.fire(1) was called exactly once
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireSINGLEreturn() {
    // Arrange
    // Set the behavior of one of the mocks: if it is called with
    // parameter 1 then it returns the value true; if isEmpty is called,
    // it returns false.
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockPrimary.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Verifying the mock: mockPrimary.fire(1) was called exatly once
    // and return value of ship.fireTorpedo(FiringMode.SINGLE) was true.
    verify(mockPrimary, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireSINGLEfirst_time_empty() {
    // Arrange
    // Set the behavior of the mocks: if isEmpty is called
    // then the first returns the value true, the second returns the value false.
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Verifying the mocks: mockPrimary.fire(1) was not called at all
    // and mockSecondary.fire(1) was called exactly once
    verify(mockPrimary, never()).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireSINGLEfirst_time_error() {
    // Arrange
    // Set the behavior of the mocks:
    // if isEmpty is called, the first returns false (to make sure that we fire it; as it's the first time, we'll try that first)
    // if fire(1) is called, the first returns false
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(false);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Verifying the mocks: mockPrimary.fire(1) was called
    // while mockSecondary.fire(1) wasn't.
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, never()).fire(1);
  }

  @Test
  public void fireSINGLEtwo() {
    // Arrange
    // Set the behavior of the mocks:
    // Both return false when isEmpty() is called and the first return true when fire(1) is called
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Verifying the mocks: both were fired only once
    // first the first, then the second
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);

    InOrder inOrder = inOrder(mockPrimary, mockSecondary);
    inOrder.verify(mockPrimary).fire(1);
    inOrder.verify(mockSecondary).fire(1);
  }

  @Test
  public void fireSINGLEtwo_empty() {
    // Arrange
    // Set the behavior of the mocks:
    // The first store returns false when isEmpty() and true when fire(1) are called.
    // The second store returns true when isEmpty() is called.
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Verifying the mocks: the first store was fired both times.
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, never()).fire(1);
  }

  @Test
  public void fire_FirstTime_PrimaryEmpty_SecondaryFail() {
    // Arrange
    // Set the behavior of the mocks:
    // The first store returns true when isEmpty() is called
    // the second returns false both when isEmpty() and fire(1) are called
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    // Check if the return value was false.
    assertEquals(false, result);
  }

}
