package com.anyang.maruni.domain.voice_chat.domain.repository;

import com.anyang.maruni.domain.voice_chat.domain.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    /**
 * Retrieves all Conversation entities associated with the specified user ID.
 *
 * @param userId the unique identifier of the user whose conversations are to be fetched
 * @return a list of Conversation entities linked to the given user ID
 */
List<Conversation> findByUser_UserId(Long userId);
}
