package org.globallogic.gorest.api;

import org.globallogic.gorest.dtos.comment.CommentRequestDTO;
import org.globallogic.gorest.dtos.comment.CommentResponseDTO;
import org.globallogic.gorest.core.ApiMain;
import org.globallogic.gorest.dtos.post.PostResponseDTO;

import java.util.List;

public class CommentAPI extends ApiMain {

    private static final String ENDPOINT = property.getConfig("comment_endpoint");

    public CommentAPI(String token) {
        super(token);
    }

    public void getComments() {
        configuration(ENDPOINT);
        LOGGER.info("...getting comments...");
        response = request
                .get()
                .then()
                .extract()
                .response();
        LOGGER.info("...list of comments: " + response.getBody().asString());
    }

    public List<CommentResponseDTO> getComments(int targetPage, int commentsPerPage) {
        configuration(ENDPOINT);
        LOGGER.info("...getting comments from page " + targetPage + " with a number of " + commentsPerPage + " entries per page...");
        response = request
                .queryParam("page", targetPage)
                .queryParam("per_page", commentsPerPage)
                .get()
                .then()
                .extract()
                .response();
        LOGGER.info("...list of comments: " + response.getBody().asString());
        return response.jsonPath().getList("", CommentResponseDTO.class);
    }

    public CommentResponseDTO createComment(PostResponseDTO post, CommentRequestDTO commentRequestPayload) {
        configuration(ENDPOINT);
        LOGGER.info("...creating comment...");
        response = request
                .body(commentRequestPayload)
                .queryParam("{post_id}", post.id())
                .post()
                .then()
                .extract()
                .response();
        LOGGER.info("...comment with id={} created...", response.jsonPath().getInt("id"));
        LOGGER.info("...comment contains: " + response.getBody().asString());
        return response.as(CommentResponseDTO.class);
    }

    public CommentResponseDTO updateComment(int id, CommentRequestDTO updatedCommentPayload) {
        configuration(ENDPOINT);
        LOGGER.info("...updating comment with id={}...", id);
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(updatedCommentPayload)
                .put()
                .then()
                .extract()
                .response();
        LOGGER.info("...updated comment contains: " + response.getBody().asString());
        return response.as(CommentResponseDTO.class);
    }

    public String deleteComment(int id, CommentRequestDTO commentDeletePayload) {
        configuration(ENDPOINT);
        LOGGER.info("...deleting target comment...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(commentDeletePayload)
                .delete()
                .then()
                .extract()
                .response();
        LOGGER.info("...comment with id={} deleted...", id);
        return response.getBody().asString();
    }
}
