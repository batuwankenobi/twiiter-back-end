@AllArgsConstructor  // Tüm alanları parametre alan constructor oluşturur (Lombok)
@NoArgsConstructor   // Parametresiz constructor oluşturur (Lombok)
@Getter              // Tüm alanlar için getter metotları oluşturur (Lombok)
@Setter              // Tüm alanlar için setter metotları oluşturur (Lombok)
@Entity              // Bu sınıfın bir JPA entity’si olduğunu belirtir
@Table(name = "tweets", schema = "twitter")  // Veritabanında twitter şemasındaki tweets tablosuna karşılık gelir
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID veritabanınca otomatik artan olarak atanır
    @Column(name = "tweet_id")  // Veritabanındaki sütun adı
    private Long id;

    @NotBlank(message = "Tweet metni boş olamaz.")  // Tweet metni boş olamaz, validation kuralı
    @Size(max = 280, message = "Tweet 280 karakterden fazla olamaz.")  // Maksimum 280 karakter sınırı
    @Column(name = "text")  // Veritabanındaki sütun adı
    private String text;

    // Bir kullanıcı birçok tweet atabilir (Many-to-One ilişkisi)
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY  // Kullanıcı bilgisi sadece ihtiyaç halinde yüklenir
    )
    @JoinColumn(name = "user_id")  // tweets tablosundaki user_id foreign key olarak kullanılır
    private User user;

    // Bir tweetin birden çok beğenisi olabilir (One-to-Many ilişkisi)
    @OneToMany(
            mappedBy = "tweet",  // Like entity'sindeki tweet alanı ile eşleşir
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER // Beğeniler tweet ile birlikte hemen yüklenir
    )
    private List<Like> likes;

    // Beğeni sayısını tutar (performans için önceden hesaplanabilir)
    @Column(name = "like_count")
    private int likeCount;

    // Bir tweetin birden çok yorumu olabilir (One-to-Many ilişkisi)
    @OneToMany(
            mappedBy = "tweet",  // Comment entity'sindeki tweet alanı ile eşleşir
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER // Yorumlar tweet ile birlikte hemen yüklenir
    )
    private List<Comment> comments;

    // Bir tweet birden çok retweet alabilir (One-to-Many ilişkisi)
    @OneToMany(
            mappedBy = "tweet",  // Retweet entity'sindeki tweet alanı ile eşleşir
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER // Retweetler tweet ile birlikte hemen yüklenir
    )
    private List<Retweet> retweets;

    // Retweet sayısını tutar (performans için önceden hesaplanabilir)
    @Column(name = "retweet_count")
    private int retweetCount;

    @Override
    public String toString() {
        return "Tweet{id=" + id + ", text='" + text + "'}";
    }
}
