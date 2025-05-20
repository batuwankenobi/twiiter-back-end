@AllArgsConstructor  // Tüm alanları parametre alan constructor oluşturur (Lombok)
@NoArgsConstructor   // Parametresiz default constructor oluşturur (Lombok)
@Data                // Getter, Setter, toString, equals, hashCode metotlarını otomatik oluşturur (Lombok)
@Entity              // Bu sınıfın bir JPA entity'si olduğunu belirtir
@Table(name = "comments", schema = "twitter")  // Veritabanında twitter şemasındaki comments tablosuna karşılık gelir
public class Comment {

    @Id  // Birincil anahtar (primary key) olduğunu belirtir
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID değerinin veritabanınca otomatik arttırılacağını belirtir
    @Column(name = "comment_id")  // Veritabanındaki sütun adı
    private Long id;

    @NotBlank(message = "Yorum boş olamaz.")  // Boş veya sadece boşluk olamaz, zorunlu alan
    @Column(name = "content")  // Veritabanındaki sütun adı
    private String content;

    // Yorum bir kullanıcıya aittir (Many-to-One ilişkisi)
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")  // comments tablosundaki user_id sütunu, foreign key olarak kullanılır
    private User user;

    // Yorum bir tweete aittir (Many-to-One ilişkisi)
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "tweet_id")  // comments tablosundaki tweet_id sütunu, foreign key olarak kullanılır
    private Tweet tweet;

    @Column(nullable = false, updatable = false)  // Boş olamaz, oluşturulduktan sonra güncellenemez
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;  // Yorumun son güncelleme zamanı (nullable olabilir)

    @PrePersist  // Yorum veritabanına ilk kaydedilmeden önce çalışır
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // createdAt zamanını şimdiki zaman yap
    }

    @PreUpdate  // Yorum güncellendiğinde tetiklenir
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();  // updatedAt zamanını şimdiki zaman yap
    }
}
