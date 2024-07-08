package org.DmitryGontsa.basetest;

import org.DmitryGontsa.api.ApiClient;
import org.junit.BeforeClass;

import static org.DmitryGontsa.apiClient.PetStoreClient.getApiClient;

public abstract class BaseTest {

    protected static ApiClient apiClient;

    @BeforeClass
    public static void createApiClient() {
        apiClient = getApiClient();
    }


}
