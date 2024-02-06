package org.globallogic.gorest.dtos.comment;

public record CommentResponseDTO (Integer id, Integer post_id, String name, String email, String body) {
}
