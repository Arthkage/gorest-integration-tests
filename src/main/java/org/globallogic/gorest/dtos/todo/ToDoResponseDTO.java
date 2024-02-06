package org.globallogic.gorest.dtos.todo;

public record ToDoResponseDTO (Integer id, Integer user_id, String title, String due_on, String status) {
}
