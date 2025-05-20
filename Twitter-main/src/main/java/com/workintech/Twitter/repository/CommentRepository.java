package com.workintech.Twitter.repository;

import com.workintech.Twitter.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// Comment entity'si için JpaRepository arayüzü.
// Temel CRUD işlemleri sağlar.
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Belirli bir tweet'e ait tüm yorumları listeler.
    List<Comment> findByTweetId(Long tweetId);

    // Belirli bir yorum id'sine ve kullanıcı id'sine sahip yorumu döner.
    // Yani, kullanıcının kendi yorumunu sorgulamak için kullanılır.
    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);

    // Bir yorumu silmek için kullanılan sorgu.
    // Yorum, ya yorumu yapan kullanıcı tarafından ya da tweet sahibi tarafından silinebilir.
    // Bu nedenle, yorum id'si ile birlikte ya yorum sahibi userId ya da tweet sahibi tweetOwnerId kontrol edilir.
    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND (c.user.id = :userId OR c.tweet.user.id = :tweetOwnerId)")
    void deleteByCommentIdAndUserIdOrTweetOwnerId(Long commentId, Long userId, Long tweetOwnerId);
}
