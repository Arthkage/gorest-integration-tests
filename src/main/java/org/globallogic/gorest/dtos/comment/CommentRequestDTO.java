package org.globallogic.gorest.dtos.comment;

public record CommentRequestDTO (Integer post_id, String name, String email, String body) {
}
