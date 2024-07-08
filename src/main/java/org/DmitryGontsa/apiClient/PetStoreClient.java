package org.DmitryGontsa.apiClient;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import org.DmitryGontsa.api.ApiClient;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.DmitryGontsa.api.ApiClient.Config.apiConfig;
import static org.DmitryGontsa.api.ApiClient.api;
import static org.DmitryGontsa.api.GsonObjectMapper.gson;
import static org.DmitryGontsa.constants.Constants.Path.PET_STORE_PATH;
import static org.DmitryGontsa.constants.Constants.ServerURL.PET_STORE_URL;

public class PetStoreClient {

    public static PetStoreClient petStoreClient;
    public static ApiClient apiClient;

    private PetStoreClient() {
    }

    public static ApiClient getApiClient() {
        if (petStoreClient == null) {
            petStoreClient = new PetStoreClient();
            apiClient = api(apiConfig().reqSpecSupplier(() -> new RequestSpecBuilder()
                    .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                    .addFilter(new ErrorLoggingFilter())
                    .setBaseUri(PET_STORE_URL)
                    .setBasePath(PET_STORE_PATH)));
        }
        return apiClient;
    }
}
