package org.DmitryGontsa.common;

public enum EnvironmentProperties {

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
