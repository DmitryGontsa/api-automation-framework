package org.DmitryGontsa.common;

public enum EnvironmentProperties {

    SPORTCHEK_BASE_URL("sportchek.base.url"),
    GOOGLE_BASE_URL("google.base.url"),
    LOCAL_API_URL("local.api.url");

    private String propertyKey;

    EnvironmentProperties(final String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String readProperty() {
        final PropertiesReader propertiesReader = new PropertiesReader();
        return propertiesReader.getProperty(propertyKey);
    }
}
