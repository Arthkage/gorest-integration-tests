package org.globallogic.gorest.smoketests;

import org.apache.commons.lang.RandomStringUtils;
import org.globallogic.gorest.api.CommentAPI;
import org.globallogic.gorest.api.PostAPI;
import org.globallogic.gorest.api.UserAPI;
import org.globallogic.gorest.core.ApiTestMain;
import org.globallogic.gorest.dtos.comment.CommentRequestDTO;
import org.globallogic.gorest.dtos.comment.CommentResponseDTO;
import org.globallogic.gorest.dtos.post.PostRequestDTO;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CommentApiSmokeTest extends ApiTestMain {

    @BeforeClass
    public void commentClassSetup() {
        token = System.getProperty("token");
        userAPI = new UserAPI(token);
        postAPI = new PostAPI(token);
        commentAPI = new CommentAPI(token);
    }

    @BeforeMethod
    public void commentTestSetup() {
        String email = String.format("commenttester%s@mail.com", RandomStringUtils.randomAlphanumeric(5));

        userRequestPayload = new UserRequestDTO("Comment Tester", email, "male", "active");
        userResponse = userAPI.createUser(userRequestPayload);

        postRequestPayload = new PostRequestDTO(userResponse.id(), "Test Post Title", "Test Post Body");
        postResponse = postAPI.createPost(userResponse,postRequestPayload);

        commentRequestPayload = new CommentRequestDTO(postResponse.id(), userResponse.name(), userResponse.email(), "Test Comment body!");
    }

    @AfterMethod
    public void cleanCommentTest() {
        postAPI.deletePost(postResponse.id(), postRequestPayload);
        userAPI.deleteUser(userResponse.id(), userRequestPayload);
    }

    @Test
    public void testGetComments() {
        commentAPI.getComments();
        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), 200);
    }

    @Test
    public void testGetCommentsFromTargetPage() {
        int targetPage = 2;
        int commentsPerPage = 1;

        List<CommentResponseDTO> comments = commentAPI.getComments(targetPage, commentsPerPage);
        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), 200);
        Assert.assertEquals(comments.size(), commentsPerPage);
    }

    @Test
    public void testCreatingComments() {

        commentResponse = commentAPI.createComment(postResponse, commentRequestPayload);
        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), 201);
        Assert.assertNotNull(commentResponse.id());
    }

    @Test
    public void testUpdateComment() {
        commentResponse = commentAPI.createComment(postResponse, commentRequestPayload);

        CommentRequestDTO updatedCommentPayload = new CommentRequestDTO(postResponse.id(), userResponse.name(), userResponse.email(), "Test Updated Comment Body");
        CommentResponseDTO newComment = commentAPI.updateComment(commentResponse.id(), updatedCommentPayload);

        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), 200);
        Assert.assertEquals(newComment.body(), "Test Updated Comment Body");
    }

    @Test
    public void testDeleteComment() {

        commentResponse = commentAPI.createComment(postResponse, commentRequestPayload);

        CommentRequestDTO commentDeletePayload = new CommentRequestDTO(commentResponse.post_id(), userResponse.name(), userResponse.email(), commentResponse.body());
        String deleteComment = commentAPI.deleteComment(commentResponse.id(), commentDeletePayload);

        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), 204);
        Assert.assertEquals(deleteComment, "");
    }
}
