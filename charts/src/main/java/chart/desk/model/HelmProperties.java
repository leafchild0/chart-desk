package chart.desk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @deprecated replace with ChartEntity
 */
@Deprecated
@AllArgsConstructor
public enum HelmProperties {
    NAME("name"),
    VERSION("version"),
    DESCRIPTION("description"),
    APP_VERSION("appVersion"),
    ICON("icon"),
    ENGINE("engine"),
    HOME("home"),
    KEYWORDS("keywords"),
    SOURCES("sources"),
    MAINTAINERS("maintainers");

    @Getter
    private final String propertyName;

    public static Optional<HelmProperties> findByPropertyName(String propertyName) {
        return Arrays.stream(HelmProperties.values())
                .filter(properties -> propertyName.equals(properties.getPropertyName()))
                .findAny();
    }
}
