package com.workintech.Twitter.repository;

import com.workintech.Twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// Retweet entity'si için JpaRepository arayüzü.
// Temel CRUD işlemleri hazırdır.
public interface Retweetrepository extends JpaRepository<Retweet, Long> {

    // Kullanıcının belirli bir tweeti retweet edip etmediğini kontrol eder.
    // Eğer varsa ilgili Retweet nesnesini Optional olarak döner.
    @Query("SELECT r FROM Retweet r WHERE r.user.id = :userId AND r.tweet.id = :tweetId")
    Optional<Retweet> findByUserAndTweet(Long userId, Long tweetId);

    // Belirtilen tweetin aldığı retweet sayısını döner.
    @Query("SELECT COUNT(r) FROM Retweet r WHERE r.tweet.id = :tweetId")
    Long countByTweetId(Long tweetId);
}
