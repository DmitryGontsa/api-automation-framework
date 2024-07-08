package org.DmitryGontsa.tests.rest_assured_tests;

import org.DmitryGontsa.actions.PetStore.PetsActions;
import org.DmitryGontsa.api.model.Pet;
import org.DmitryGontsa.basetest.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.util.List;

import static io.restassured.http.Method.PATCH;
import static java.lang.Long.parseLong;
import static org.DmitryGontsa.api.api.PetApi.FindPetsByStatusOper.STATUS_QUERY;
import static org.DmitryGontsa.api.api.PetApi.FindPetsByTagsOper.TAGS_QUERY;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.DmitryGontsa.api.model.Pet.StatusEnum.AVAILABLE;

public class PetTests extends BaseTest {

    private static PetsActions petsActions;
    private Pet expectedPet;

    @BeforeAll
    public static void setUp() {
        petsActions = new PetsActions();
    }

    @BeforeEach
    void getPet() {
        expectedPet = petsActions.getPet();
    }

    @Test
    public void createNewPetTest() {
        Pet actualPet = petsActions.createNewPet(expectedPet);
        assertEquals(expectedPet, actualPet);
    }

    @Test
    public void shouldSee405AfterCreateNewPetTest() {
        petsActions.checkStatusCode(expectedPet, PATCH);
    }

    @Test
    public void deletePetTest() {
        Pet actualPet = petsActions.createNewPet(expectedPet);
        petsActions.deletePet(actualPet.getId());
        assertFalse(petsActions.isPetExists(actualPet.getId()));
    }

    @Test
    public void findPetsByStatusTest() {
        List<Pet> pets = petsActions.findPetsByStatus(AVAILABLE);
        pets.forEach(pet -> assertEquals(pet.getStatus(), AVAILABLE));
    }

    @Test
    public void shouldSee400AfterFindPetsByStatusTest() {
        petsActions.findPetsBy(STATUS_QUERY);
    }

    @Test
    public void findPetsByTagsTest() {
        expectedPet.tags(List.of(petsActions.getTag()));
        Pet actualPet = petsActions.createNewPet(expectedPet);

        List<Pet> petsByTags = petsActions.findPetsByTags(actualPet.getTags().getFirst());
        petsByTags.forEach(pet -> assertEquals(pet, actualPet));
    }

    @Test
    public void shouldSee400AfterFindPetsByTagsTest() {
        petsActions.findPetsBy(TAGS_QUERY);
    }

    @Test
    public void getPetByIdTest() {
        Pet newPet = petsActions.createNewPet(expectedPet);
        Pet actualPet = petsActions.getPetById(newPet.getId());
        assertEquals(newPet, actualPet);
    }

    @Test
    public void shouldSee404AfterGetPetByIdTest() {
        petsActions.getPetBy(parseLong(randomNumeric(5)));
    }

    @Test
    public void updatePetTest() {
        String newPetName = randomAlphanumeric(5);
        Pet newPet = petsActions.createNewPet(expectedPet).name(newPetName);
        Pet actualPet = petsActions.updatePet(newPet);
        assertEquals(newPet, actualPet);
    }
}
