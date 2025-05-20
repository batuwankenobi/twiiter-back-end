package com.workintech.Twitter.repository;

import com.workintech.Twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// User entity'si için JPA repository arayüzü.
// JpaRepository sayesinde temel CRUD operasyonları (kaydetme, silme, güncelleme, bulma) hazır gelir.
public interface UserRepository extends JpaRepository<User, Long> {

    // Kullanıcı adını kullanarak kullanıcıyı veritabanından bulmak için özel sorgu.
    // :username parametresi metot parametresi ile eşleşir.
    // Optional döner, böylece kullanıcı bulunamazsa null yerine boş bir Optional döner.
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);
}
