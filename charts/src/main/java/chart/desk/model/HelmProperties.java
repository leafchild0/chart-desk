package chart.desk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum HelmProperties
{
  DESCRIPTION("description"),
  ENGINE("engine"),
  HOME("home"),
  ICON("icon"),
  APP_VERSION("appVersion"),
  KEYWORDS("keywords"),
  MAINTAINERS("maintainers"),
  NAME("name"),
  SOURCES("sources"),
  VERSION("version");

  @Getter
  private String propertyName;

  public static Optional<HelmProperties> findByPropertyName(String propertyName) {
    return Arrays.stream(HelmProperties.values())
        .filter(properties -> propertyName.equals(properties.getPropertyName()))
        .findAny();
  }
}
