package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
