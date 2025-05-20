package com.workintech.Twitter.repository;

import com.workintech.Twitter.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// Like entity'si için JpaRepository arayüzü.
// Temel CRUD işlemleri sağlanır.
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Kullanıcının belirli bir tweeti beğenip beğenmediğini kontrol eder.
    // Eğer beğenmişse ilgili Like nesnesini Optional olarak döner.
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.tweet.id = :tweetId")
    Optional<Like> findByUserAndTweet(Long userId, Long tweetId);

    // Belirtilen tweetin aldığı toplam beğeni sayısını döner.
    @Query("SELECT COUNT(l) FROM Like l WHERE l.tweet.id = :tweetId")
    Long countByTweetId(Long tweetId);

    // Kullanıcının belirli bir tweet için yaptığı beğeniyi siler.
    // Bu işlem veri tabanında değişiklik yaptığı için @Modifying ve @Transactional anotasyonları gerekir.
    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.user.id = :userId AND l.tweet.id = :tweetId")
    void deleteByUserAndTweet(Long userId, Long tweetId);
}
