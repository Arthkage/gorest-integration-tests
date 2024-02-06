package org.globallogic.gorest.smoketests;

import org.apache.commons.lang.RandomStringUtils;
import org.globallogic.gorest.api.PostAPI;
import org.globallogic.gorest.api.ToDoAPI;
import org.globallogic.gorest.api.UserAPI;
import org.globallogic.gorest.core.ApiTestMain;
import org.globallogic.gorest.dtos.todo.ToDoRequestDTO;
import org.globallogic.gorest.dtos.todo.ToDoResponseDTO;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ToDoApiSmokeTest extends ApiTestMain {

    @BeforeClass
    public void toDoClassSetup() {
        token = System.getProperty("token");
        userAPI = new UserAPI(token);
        postAPI = new PostAPI(token);
        toDoAPI = new ToDoAPI(token);
    }

    @BeforeMethod
    public void toDoTestSetup() {
        String email = String.format("todotester%s@mail.com", RandomStringUtils.randomAlphanumeric(5));
        DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentDateTime = dateTime.format(date);

        userRequestPayload = new UserRequestDTO("ToDo Tester", email, "male", "active");
        userResponse = userAPI.createUser(userRequestPayload);

        toDoRequestPayload = new ToDoRequestDTO(userResponse.id(), "Test ToDo Title", currentDateTime, "pending");
    }

    @AfterMethod
    public void cleanToDoTest() {
        userAPI.deleteUser(userResponse.id(), userRequestPayload);
    }

    @Test

    public void testRetrieveAllToDos() {
        toDoAPI.getToDos();
        Assert.assertEquals(toDoAPI.getResponse().getStatusCode(), 200);
    }

    @Test
    public void testRetrieveToDosFromTargetPage () {
        int targetPage = 1;
        int commentsPerPage = 2;

        List<ToDoResponseDTO> todos = toDoAPI.getToDos(targetPage, commentsPerPage);
        Assert.assertEquals(toDoAPI.getResponse().statusCode(), 200);
        Assert.assertEquals(todos.size(), commentsPerPage);
    }

    @Test
    public void testCreateNewToDoItem() {

        toDoResponse = toDoAPI.createTodo(userResponse, toDoRequestPayload);

        Assert.assertEquals(toDoAPI.getResponse().statusCode(), 201);
        Assert.assertNotNull(toDoResponse.id());
    }

    @Test
    public void testUpdateTargetToDoItem() {
        toDoResponse = toDoAPI.createTodo(userResponse, toDoRequestPayload);

        DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String updatedDateTime = dateTime.format(date);

        ToDoRequestDTO updatedToDoPayload = new ToDoRequestDTO(userResponse.id(), "Test Updated ToDo Title", updatedDateTime, "completed");
        ToDoResponseDTO updatedToDo = toDoAPI.updateToDo(toDoResponse.id(), updatedToDoPayload);

        Assert.assertEquals(toDoAPI.getResponse().statusCode(), 200);
        Assert.assertEquals(updatedToDo.title(), "Test Updated ToDo Title");
        Assert.assertEquals(updatedToDo.status(), "completed");
    }

    @Test
    public void testDeleteTargetToDoItem() {
        toDoResponse = toDoAPI.createTodo(userResponse, toDoRequestPayload);

        ToDoRequestDTO deleteToDoPayload = new ToDoRequestDTO(userResponse.id(), toDoResponse.title(), toDoResponse.due_on(), toDoResponse.status());
        String deletedToDo = toDoAPI.deletePost(toDoResponse.id(), deleteToDoPayload);

        Assert.assertEquals(toDoAPI.getResponse().statusCode(), 204);
        Assert.assertEquals(deletedToDo, "");
    }
}
