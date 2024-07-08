package com.hillel.ua.api;

import com.hillel.ua.api.dto.PostsDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.String.*;

public class PostsApiSteps extends AbstractApiSteps {

    private static final String POSTS_API_PATH = "/posts";

    @Step
    public Response createNewPost(final PostsDTO postRequest) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .post(POSTS_API_PATH);
    }

    @Step
    public void removePostById(final Integer postId) {
        RestAssured.delete(format("%s/%s", POSTS_API_PATH, postId));
    }

    @Step
    public List<PostsDTO> getAllPosts() {
        return Arrays.asList(RestAssured.given()
                .contentType(ContentType.JSON)
                .get(POSTS_API_PATH)
                .as(PostsDTO[].class));
    }

    @Step
    public PostsDTO getPostById(final Integer postId) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .get(format("%s/%s", POSTS_API_PATH, postId))
                .as(PostsDTO.class);
    }

    @Step
    public List<PostsDTO> getByQueryParams(final Map<String, String> params) {
        return Arrays.asList(RestAssured.given()
                .queryParams(params)
                .contentType(ContentType.JSON)
                .get(POSTS_API_PATH)
                .as(PostsDTO[].class));
    }

    @Step
    public PostsDTO updatePostById(final PostsDTO newPostData, final Integer existingPostId) {
        return RestAssured.given()
                .body(newPostData)
                .contentType(ContentType.JSON)
                .put(format("%s/%s", POSTS_API_PATH, existingPostId))
                .as(PostsDTO.class);
    }
}
