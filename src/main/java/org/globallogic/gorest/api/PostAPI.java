package org.globallogic.gorest.api;

import org.globallogic.gorest.core.ApiMain;
import org.globallogic.gorest.dtos.post.PostRequestDTO;
import org.globallogic.gorest.dtos.post.PostResponseDTO;
import org.globallogic.gorest.dtos.user.UserResponseDTO;

import java.util.List;

public class PostAPI extends ApiMain {

    private static final String ENDPOINT = property.getConfig("post_endpoint");
    public PostAPI(String token) {
        super(token);
    }

    public void getPosts() {
        configuration(ENDPOINT);
        LOGGER.info("...getting posts...");
        response = request
                .get()
                .then()
                .extract()
                .response();
        LOGGER.info("...list of posts: " + response.getBody().asString());
    }

    public List<PostResponseDTO> getPosts(int targetPage, int postsPerPage) {

        configuration(ENDPOINT);
        LOGGER.info("...getting posts from page " + targetPage + " with a number of "+ postsPerPage +" entries per page...");
        response = request
                .queryParam("page", targetPage)
                .queryParam("per_page", postsPerPage)
                .get()
                .then()
                .extract()
                .response();
        LOGGER.info("...list of posts: " + response.getBody().asString());
        return response.jsonPath().getList("", PostResponseDTO.class);
    }

    public PostResponseDTO createPost(UserResponseDTO newUser, PostRequestDTO postRequestPayload) {
        configuration(ENDPOINT);
        LOGGER.info("...creating new post...");
        response = request
                .body(postRequestPayload)
                .queryParam("{user_id}", newUser.id())
                .post()
                .then()
                .extract()
                .response();
        LOGGER.info("...post with id={} has been created...", response.jsonPath().getInt("id"));
        LOGGER.info("...post content: " + response.getBody().asString());
        return response.as(PostResponseDTO.class);
    }

    public PostResponseDTO updatePost(int id, PostRequestDTO postUpdatePayload) {
        configuration(ENDPOINT);
        LOGGER.info("...updating post...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(postUpdatePayload)
                .put()
                .then()
                .extract()
                .response();
        LOGGER.info("...update post contains: " + response.getBody().asString());
        return response.as(PostResponseDTO.class);
    }

    public String deletePost(int id, PostRequestDTO deletePayload) {
        configuration(ENDPOINT);
        LOGGER.info("...deleting target post...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(deletePayload)
                .delete()
                .then()
                .extract()
                .response();
        LOGGER.info("...post with id={} deleted...", id);
        return response.getBody().asString();
    }
}
