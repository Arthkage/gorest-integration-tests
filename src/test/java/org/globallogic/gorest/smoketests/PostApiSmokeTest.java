package org.globallogic.gorest.smoketests;

import org.apache.commons.lang.RandomStringUtils;
import org.globallogic.gorest.api.PostAPI;
import org.globallogic.gorest.api.UserAPI;
import org.globallogic.gorest.core.ApiTestMain;
import org.globallogic.gorest.dtos.post.PostRequestDTO;
import org.globallogic.gorest.dtos.post.PostResponseDTO;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.globallogic.gorest.core.ApiMain.property;

public class PostApiSmokeTest extends ApiTestMain {

    @BeforeClass
    public void postClassSetup() {
        token = System.getProperty("token");
        userAPI = new UserAPI(token);
        postAPI = new PostAPI(token);
    }

    @BeforeMethod
    public void postTestSetup() {
        String email = String.format(property.getTestData("post_user_mail"),
                RandomStringUtils.randomAlphanumeric(5));

        userRequestPayload = new UserRequestDTO(property.getTestData("post_user_name"), email,
                property.getTestData("gender_male"), property.getTestData("status_active"));
        userResponse = userAPI.createUser(userRequestPayload);

        postRequestPayload = new PostRequestDTO(userResponse.id(), property.getTestData("post_title"),
                property.getTestData("post_body"));
    }

    @AfterMethod
    public void cleanPostTest() {
        userAPI.deleteUser(userResponse.id(), userRequestPayload);
    }

    @Test
    public void testReturnLatestPosts() {
        postAPI.getPosts();
        Assert.assertEquals(postAPI.getResponse().getStatusCode(), OK_STATUS);
    }

    @Test
    public void testReturnPostsFromTargetPage() {
        int targetPage = 1;
        int postsPerPage = 2;

        List<PostResponseDTO> posts = postAPI.getPosts(targetPage, postsPerPage);
        Assert.assertEquals(postAPI.getResponse().getStatusCode(), OK_STATUS);
        Assert.assertEquals(posts.size(), postsPerPage);
    }

    @Test
    public void testCreatePostForTargetUser() {
        postResponse = postAPI.createPost(userResponse, postRequestPayload);

        Assert.assertEquals(postAPI.getResponse().getStatusCode(), CREATED_STATUS);
        Assert.assertNotNull(postResponse.id());
    }

    @Test
    public void testUpdatePostForTargetUser() {
        postResponse = postAPI.createPost(userResponse, postRequestPayload);

        PostRequestDTO postUpdatePayload = new PostRequestDTO(userResponse.id(),
                property.getTestData("post_upd_title"), property.getTestData("post_upd_body"));
        PostResponseDTO newPost = postAPI.updatePost(postResponse.id(), postUpdatePayload);

        Assert.assertEquals(postAPI.getResponse().getStatusCode(), OK_STATUS);
        Assert.assertEquals(newPost.body(), property.getTestData("post_upd_body"));

    }

    @Test
    public void testDeletePostForTargetUser() {

        postResponse = postAPI.createPost(userResponse, postRequestPayload);

        PostRequestDTO postDeletePayload = new PostRequestDTO(postResponse.user_id(), postResponse.title(),
                postResponse.body());
        String deletePost = postAPI.deletePost(postResponse.id(), postDeletePayload);

        Assert.assertEquals(postAPI.getResponse().getStatusCode(), DELETED_STATUS);
        Assert.assertEquals(deletePost, "");

    }
}
