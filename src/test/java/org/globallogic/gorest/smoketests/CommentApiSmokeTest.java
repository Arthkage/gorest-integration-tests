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

import static org.globallogic.gorest.core.ApiMain.property;

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
        String email = String.format(property.getTestData("comment_user_mail"),
                RandomStringUtils.randomAlphanumeric(5));

        userRequestPayload = new UserRequestDTO(property.getTestData("comment_user_name"), email,
                property.getTestData("gender_male"), property.getTestData("status_inactive"));

        userResponse = userAPI.createUser(userRequestPayload);

        postRequestPayload = new PostRequestDTO(userResponse.id(), property.getTestData("post_title"),
                property.getTestData("post_body"));

        postResponse = postAPI.createPost(userResponse, postRequestPayload);

        commentRequestPayload = new CommentRequestDTO(postResponse.id(), userResponse.name(), userResponse.email(),
                property.getTestData("comment_body"));
    }

    @AfterMethod
    public void cleanCommentTest() {
        postAPI.deletePost(postResponse.id(), postRequestPayload);
        userAPI.deleteUser(userResponse.id(), userRequestPayload);
    }

    @Test
    public void testGetComments() {
        commentAPI.getComments();
        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), OK_STATUS);
    }

    @Test
    public void testGetCommentsFromTargetPage() {
        int targetPage = 2;
        int commentsPerPage = 1;

        List<CommentResponseDTO> comments = commentAPI.getComments(targetPage, commentsPerPage);
        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), OK_STATUS);
        Assert.assertEquals(comments.size(), commentsPerPage);
    }

    @Test
    public void testCreatingComments() {

        commentResponse = commentAPI.createComment(postResponse, commentRequestPayload);
        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), CREATED_STATUS);
        Assert.assertNotNull(commentResponse.id());
    }

    @Test
    public void testUpdateComment() {
        commentResponse = commentAPI.createComment(postResponse, commentRequestPayload);

        CommentRequestDTO updatedCommentPayload = new CommentRequestDTO(postResponse.id(), userResponse.name(),
                userResponse.email(), property.getTestData("comment_upd_body"));
        CommentResponseDTO newComment = commentAPI.updateComment(commentResponse.id(), updatedCommentPayload);

        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), UPDATED_STATUS);
        Assert.assertEquals(newComment.body(), property.getTestData("comment_upd_body"));
    }

    @Test
    public void testDeleteComment() {

        commentResponse = commentAPI.createComment(postResponse, commentRequestPayload);

        CommentRequestDTO commentDeletePayload = new CommentRequestDTO(commentResponse.post_id(),
                userResponse.name(), userResponse.email(), commentResponse.body());
        String deleteComment = commentAPI.deleteComment(commentResponse.id(), commentDeletePayload);

        Assert.assertEquals(commentAPI.getResponse().getStatusCode(), DELETED_STATUS);
        Assert.assertEquals(deleteComment, "");
    }
}
