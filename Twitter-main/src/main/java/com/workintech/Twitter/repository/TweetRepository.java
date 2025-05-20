package com.workintech.Twitter.repository;

import com.workintech.Twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Tweet entity'si için JpaRepository arayüzü.
// Temel CRUD işlemleri hazırdır.
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    // Belirli bir kullanıcıya ait tweetleri kullanıcı ID'sine göre bulur.
    List<Tweet> findByUserId(Long userId);

    // Kullanıcının tüm tweetlerini ID'ye göre azalan sırada (son atılan en üstte) listeler.
    @Query("SELECT t FROM Tweet t WHERE t.user.id = :userId ORDER BY t.id DESC")
    List<Tweet> findAllByUserId(Long userId);
}
