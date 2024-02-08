package org.globallogic.gorest.smoketests;

import org.apache.commons.lang.RandomStringUtils;
import org.globallogic.gorest.api.PostAPI;
import org.globallogic.gorest.api.ToDoAPI;
import org.globallogic.gorest.api.UserAPI;
import org.globallogic.gorest.core.ApiTestMain;
import org.globallogic.gorest.dtos.todo.ToDoRequestDTO;
import org.globallogic.gorest.dtos.todo.ToDoResponseDTO;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.globallogic.gorest.utils.DateAndTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.globallogic.gorest.core.ApiMain.property;

public class ToDoApiSmokeTest extends ApiTestMain {

    DateAndTimeFormatter currentDateAndTime;

    @BeforeClass
    public void toDoClassSetup() {
        token = System.getProperty("token");
        userAPI = new UserAPI(token);
        postAPI = new PostAPI(token);
        toDoAPI = new ToDoAPI(token);
        currentDateAndTime = new DateAndTimeFormatter();
    }

    @BeforeMethod
    public void toDoTestSetup() {
        String email = String.format(property.getTestData("todo_user_mail"),
                RandomStringUtils.randomAlphanumeric(5));

        String currentDateTime = currentDateAndTime.returnCurrentDateAndTime();

        userRequestPayload = new UserRequestDTO(property.getTestData("todo_user_name"), email,
                property.getTestData("gender_female"), property.getTestData("status_active"));
        userResponse = userAPI.createUser(userRequestPayload);

        toDoRequestPayload = new ToDoRequestDTO(userResponse.id(), property.getTestData("todo_title"),
                currentDateTime, property.getTestData("status_pending"));
    }

    @AfterMethod
    public void cleanToDoTest() {
        userAPI.deleteUser(userResponse.id(), userRequestPayload);
    }

    @Test

    public void testRetrieveAllToDos() {
        toDoAPI.getToDos();
        Assert.assertEquals(toDoAPI.getResponse().getStatusCode(), OK_STATUS);
    }

    @Test
    public void testRetrieveToDosFromTargetPage () {
        int targetPage = 1;
        int commentsPerPage = 2;

        List<ToDoResponseDTO> todos = toDoAPI.getToDos(targetPage, commentsPerPage);
        Assert.assertEquals(toDoAPI.getResponse().statusCode(), OK_STATUS);
        Assert.assertEquals(todos.size(), commentsPerPage);
    }

    @Test
    public void testCreateNewToDoItem() {

        toDoResponse = toDoAPI.createTodo(userResponse, toDoRequestPayload);

        Assert.assertEquals(toDoAPI.getResponse().statusCode(), CREATED_STATUS);
        Assert.assertNotNull(toDoResponse.id());
    }

    @Test
    public void testUpdateTargetToDoItem() {
        toDoResponse = toDoAPI.createTodo(userResponse, toDoRequestPayload);

        String updatedDateTime = currentDateAndTime.returnCurrentDateAndTime();

        ToDoRequestDTO updatedToDoPayload = new ToDoRequestDTO(userResponse.id(),
                property.getTestData("todo_upd_title"), updatedDateTime, property.getTestData("status_completed"));
        ToDoResponseDTO updatedToDo = toDoAPI.updateToDo(toDoResponse.id(), updatedToDoPayload);

        Assert.assertEquals(toDoAPI.getResponse().statusCode(), UPDATED_STATUS);
        Assert.assertEquals(updatedToDo.title(), property.getTestData("todo_upd_title"));
        Assert.assertEquals(updatedToDo.status(), property.getTestData("status_completed"));
    }

    @Test
    public void testDeleteTargetToDoItem() {
        toDoResponse = toDoAPI.createTodo(userResponse, toDoRequestPayload);

        ToDoRequestDTO deleteToDoPayload = new ToDoRequestDTO(userResponse.id(), toDoResponse.title(), toDoResponse.due_on(), toDoResponse.status());
        String deletedToDo = toDoAPI.deletePost(toDoResponse.id(), deleteToDoPayload);

        Assert.assertEquals(toDoAPI.getResponse().statusCode(), DELETED_STATUS);
        Assert.assertEquals(deletedToDo, "");
    }
}
