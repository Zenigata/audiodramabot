package fr.zenigata.util;

import org.junit.Assert;
import org.junit.Test;

public class SpecUtilsTest {

  @Test
  public void display_duration_for_a_minute() {
    String duration = SpecUtils.displayDuration(60f);
    Assert.assertEquals("0h 1m", duration);
  }

  @Test
  public void display_duration_for_half_an_hour() {
    String duration = SpecUtils.displayDuration(2220f);
    Assert.assertEquals("0h 37m", duration);
  }

  @Test
  public void display_duration_for_several_hours() {
    String duration = SpecUtils.displayDuration(12420f);
    Assert.assertEquals("3h 27m", duration);
  }
}