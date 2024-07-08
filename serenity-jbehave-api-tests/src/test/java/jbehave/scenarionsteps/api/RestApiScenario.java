package com.hillel.ua.jbehave.scenarionsteps.api;

import com.hillel.ua.api.PostsApiSteps;
import com.hillel.ua.api.dto.PostsDTO;
import com.hillel.ua.logging.Logger;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.List;
import java.util.Map;

public class RestApiScenario {

    @Steps
    private PostsApiSteps postsApiSteps;

    private static final String CREATED_POST_KEY = "created_post_key";
    private static final String EXPECTED_POST_DATA_KEY = "expected_post_data_key";
    private static final String UPDATED_POST_DATA_KEY = "updated_post_data_key";
    private static final String FILTERED_POSTS_BY_QUERY_PARAMS_KEY = "filtered_posts_by_query_params_key";

    private static final Integer EXPECTED_POST_ID = 1;

    @BeforeScenario
    public void deleteApiResources() {

        Logger.out.debug("! ----------- Removing Rest API Created Resources ----------- !");

        postsApiSteps.removePostById(EXPECTED_POST_ID);

        Logger.out.debug("! ----------- Removing Rest API Created Resources Done ----------- !");
    }

    @Given("user creates new 'Post', using API: $postData")
    public void createNewPost(final ExamplesTable newPostInfo) {
        final PostsDTO newPostsData = newPostInfo.getRowsAs(PostsDTO.class).get(0);
        final Response newPostResponse = postsApiSteps.createNewPost(newPostsData);
        Serenity.setSessionVariable(EXPECTED_POST_DATA_KEY).to(newPostsData);
        Serenity.setSessionVariable(CREATED_POST_KEY).to(newPostResponse);
    }

    @When("user filters retrieved Posts by next filter params: $filterParams")
    public void filterAllPostsByQueryParameters(final ExamplesTable filterParams) {
        final Map<String, String> queryParams = filterParams.getRow(0);
        final List<PostsDTO> filteredByQueryParams = postsApiSteps.getByQueryParams(queryParams);
        Serenity.setSessionVariable(FILTERED_POSTS_BY_QUERY_PARAMS_KEY).to(filteredByQueryParams);
    }

    @When("user update existing post, using following data: $newPostData")
    public void updateExistingPost(final ExamplesTable requestBody) {
        final PostsDTO body = requestBody.getRowsAs(PostsDTO.class).get(0);

        final Response createdPost = Serenity.sessionVariableCalled(CREATED_POST_KEY);

        final Integer createdPostId = createdPost.jsonPath().get("id");

        final PostsDTO updatedPost = postsApiSteps.updatePostById(body, createdPostId);

        Serenity.setSessionVariable(UPDATED_POST_DATA_KEY).to(updatedPost);
    }

    @Then("each filtered Post should contain such data only: $filteredData")
    public void arePostsFilteredCorrectly(final ExamplesTable filteredData) {
        final Map<String, String> expectedFilteredPosts = filteredData.getRow(0);

        final List<PostsDTO> actualFilteredPosts = Serenity.sessionVariableCalled(FILTERED_POSTS_BY_QUERY_PARAMS_KEY);

        Assertions.assertThat(actualFilteredPosts)
                .as("There are no filtered Posts retrieved!")
                .isNotEmpty();

        actualFilteredPosts.forEach(post -> {

            Assertions.assertThat(post.getAuthor())
                    .as("There is incorrect Author!")
                    .isEqualTo(expectedFilteredPosts.get("author"));

            Assertions.assertThat(post.getTitle())
                    .as("There is incorrect Title!")
                    .isEqualTo(expectedFilteredPosts.get("title"));
        });
    }

    @Then("following post should be updated")
    public void isCreatedPostUpdatedByNewData() {
        final PostsDTO expectedPostData = Serenity.sessionVariableCalled(UPDATED_POST_DATA_KEY);

        final PostsDTO actualPostData = postsApiSteps.getPostById(expectedPostData.getId());

        ReflectionAssert.assertReflectionEquals("There is incorrect 'updated' Post!",
                expectedPostData, actualPostData);
    }

    @Then("new post should be created")
    public void isNewPostCreated() {
        final Response createdPostResponse = Serenity.sessionVariableCalled(CREATED_POST_KEY);

        final PostsDTO actualPost = createdPostResponse.as(PostsDTO.class);
        final PostsDTO expectedPost = Serenity.sessionVariableCalled(EXPECTED_POST_DATA_KEY);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(createdPostResponse.getStatusCode())
                .as("Incorrect status code!")
                .isEqualTo(201);

        softAssertions.assertThat(actualPost.getTitle())
                .as("There is incorrect title!")
                .isEqualTo(expectedPost.getTitle());

        softAssertions.assertThat(actualPost.getAuthor())
                .as("There is incorrect author!")
                .isEqualTo(expectedPost.getAuthor());

        softAssertions.assertThat(actualPost.getAge())
                .as("There is incorrect age!")
                .isEqualTo(expectedPost.getAge());

        softAssertions.assertAll();
    }
}
