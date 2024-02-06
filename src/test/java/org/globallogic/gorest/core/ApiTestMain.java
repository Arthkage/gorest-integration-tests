package org.globallogic.gorest.core;

import org.globallogic.gorest.api.CommentAPI;
import org.globallogic.gorest.api.PostAPI;
import org.globallogic.gorest.api.ToDoAPI;
import org.globallogic.gorest.api.UserAPI;
import org.globallogic.gorest.dtos.comment.CommentRequestDTO;
import org.globallogic.gorest.dtos.comment.CommentResponseDTO;
import org.globallogic.gorest.dtos.post.PostRequestDTO;
import org.globallogic.gorest.dtos.post.PostResponseDTO;
import org.globallogic.gorest.dtos.todo.ToDoRequestDTO;
import org.globallogic.gorest.dtos.todo.ToDoResponseDTO;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.globallogic.gorest.dtos.user.UserResponseDTO;

public class ApiTestMain {

    protected String token;
    protected UserAPI userAPI;
    protected PostAPI postAPI;
    protected CommentAPI commentAPI;
    protected ToDoAPI toDoAPI;

    protected UserRequestDTO userRequestPayload;
    protected UserResponseDTO userResponse;

    protected PostRequestDTO postRequestPayload;
    protected PostResponseDTO postResponse;

    protected CommentRequestDTO commentRequestPayload;
    protected CommentResponseDTO commentResponse;

    protected ToDoRequestDTO toDoRequestPayload;
    protected ToDoResponseDTO toDoResponse;

    protected final int okStatusCode = 200;
    protected final int createdStatusCode = 201;
    protected final int updatedStatusCode = 200;
    protected final int deletedStatusCode = 204;
}
