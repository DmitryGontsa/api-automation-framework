package org.DmitryGontsa.actions.PetStore;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.DmitryGontsa.api.model.Category;
import org.DmitryGontsa.api.model.Pet;
import org.DmitryGontsa.api.model.Tag;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.EMPTY_LIST;
import static org.DmitryGontsa.api.ApiClient.BASE_URI;
import static org.DmitryGontsa.api.api.PetApi.AddPetOper.REQ_URI;
import static org.DmitryGontsa.api.api.PetApi.UpdatePetOper;
import static org.DmitryGontsa.api.api.PetApi.FindPetsByStatusOper;
import static org.DmitryGontsa.api.api.PetApi.DeletePetOper.API_KEY_HEADER;
import static org.DmitryGontsa.api.api.PetApi.DeletePetOper.PET_ID_PATH;
import static org.DmitryGontsa.api.api.PetApi.FindPetsByStatusOper.STATUS_QUERY;
import static org.DmitryGontsa.api.api.PetApi.FindPetsByTagsOper.TAGS_QUERY;
import static org.DmitryGontsa.api.api.PetApi.FindPetsByTagsOper;
import static org.DmitryGontsa.api.api.PetApi.GetPetByIdOper;
import static org.DmitryGontsa.api.model.Pet.StatusEnum.AVAILABLE;
import static org.DmitryGontsa.apiClient.PetStoreClient.getApiClient;
import static org.DmitryGontsa.authorizations.ApiAccess.getApiKey;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class PetsActions {

    private RequestSpecification requestSpecification;

    public PetsActions() {
        requestSpecification = new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .addHeader(API_KEY_HEADER, getApiKey())
                .setContentType(JSON)
                .log(ALL).build();
    }

    public Pet getPet() {
        return new Pet()
                .id(parseLong(randomNumeric(10)))
                .name("Pet_" + randomAlphanumeric(5))
                .tags(EMPTY_LIST)
                .status(AVAILABLE);
    }

    public Tag getTag() {
        return new Tag()
                .id(parseLong(randomNumeric(5)))
                .name("Tag_" + randomAlphanumeric(5));
    }

    public Pet getDefaultPet() {
        return getApiClient()
                .pet()
                .addPet()
                .body(getPet())
                .executeAs(Response::prettyPeek);
    }

    public Pet createNewPet(long id, Category category, String name, String[] urls,
                            String[] tags, Pet.StatusEnum status) {
        Pet pet = new Pet()
                .id(id)
                .category(category)
                .name(name)
                .status(status);

        if (null != urls) pet.setPhotoUrls(asList(urls));

        final AtomicLong i = new AtomicLong(0);
        if (null != tags) {
            stream(tags)
                    .map(tag -> new Tag().name(tag).id(i.incrementAndGet()))
                    .forEach(pet::addTagsItem);
        }
        return pet;
    }

    public Pet createNewPet(Pet pet) {
        return given(requestSpecification)
                .body(pet)
                .log().all()
                .post(BASE_URI.concat(REQ_URI))
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(Pet.class);
    }

    public void deletePet(Long petId) {
        given(requestSpecification)
                .pathParam(PET_ID_PATH, petId)
                .log().all()
                .delete(BASE_URI.concat(GetPetByIdOper.REQ_URI))
                .then()
                .statusCode(200)
                .log().all();
    }

    public boolean isPetExists(Long petId) {
        return !given(requestSpecification)
                .pathParam(PET_ID_PATH, petId).log().all()
                .get(BASE_URI.concat(GetPetByIdOper.REQ_URI))
                .asString().equals("Pet not found");
    }

    public void checkStatusCode(Pet pet, Method requestMethod) {
        given(requestSpecification)
                .body(pet)
                .log().all()
                .request(requestMethod, BASE_URI.concat(REQ_URI))
                .then()
                .statusCode(405)
                .log().all();
    }

    public List<Pet> findPetsByStatus(Pet.StatusEnum status) {
        return given(requestSpecification)
                .queryParam(STATUS_QUERY, status).log().all()
                .get(BASE_URI.concat(FindPetsByStatusOper.REQ_URI))
                .then().log().all()
                .extract().body()
                .jsonPath().getList("", Pet.class);
    }

    public void findPetsBy(String query) {
        String endpoint = (query.equals(STATUS_QUERY))
                ? FindPetsByStatusOper.REQ_URI : FindPetsByTagsOper.REQ_URI;
        given(requestSpecification)
                .queryParam(query, (Object) null)
                .log().all()
                .get(BASE_URI.concat(endpoint))
                .then().statusCode(400)
                .log().all();
    }

    public List<Pet> findPetsByTags(Tag tag) {
        return given(requestSpecification)
                .queryParam(TAGS_QUERY, tag.getName()).log().all()
                .get(BASE_URI.concat(FindPetsByTagsOper.REQ_URI))
                .then().log().all()
                .extract().body()
                .jsonPath().getList("", Pet.class);
    }

    public Pet getPetById(Long petId) {
        return given(requestSpecification)
                .pathParam(PET_ID_PATH, petId).log().all()
                .get(BASE_URI.concat(GetPetByIdOper.REQ_URI))
                .then().statusCode(200)
                .log().all()
                .extract().as(Pet.class);
    }

    public void getPetBy(Long petId) {
        given(requestSpecification)
                .pathParam(PET_ID_PATH, petId).log().all()
                .get(BASE_URI.concat(GetPetByIdOper.REQ_URI))
                .then().statusCode(404)
                .log().all();
    }

    public Pet updatePet(Pet pet) {
        return given(requestSpecification)
                .body(pet).log().all()
                .put(BASE_URI.concat(UpdatePetOper.REQ_URI))
                .then().statusCode(200)
                .log().all()
                .extract()
                .as(Pet.class);
    }
}
