@AllArgsConstructor  // Tüm alanları parametre alan constructor oluşturur (Lombok)
@NoArgsConstructor   // Parametresiz default constructor oluşturur (Lombok)
@Data                // Getter, Setter, toString, equals, hashCode metotlarını otomatik oluşturur (Lombok)
@Entity              // Bu sınıfın JPA entity’si olduğunu belirtir
@Table(name = "retweets", schema = "twitter")  // Veritabanında twitter şemasındaki retweets tablosuna karşılık gelir
public class Retweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID değeri veritabanı tarafından otomatik arttırılır
    @Column(name = "retweet_id")  // Veritabanındaki sütun adı
    private Long id;

    // Retweet bir tweete aittir (Many-to-One ilişkisi)
    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH },
            fetch = FetchType.LAZY // Tweet bilgisi sadece ihtiyaç halinde yüklenir
    )
    @JoinColumn(name = "tweet_id")  // retweets tablosundaki tweet_id foreign key olarak kullanılır
    private Tweet tweet;

    // Retweet bir kullanıcıya aittir (Many-to-One ilişkisi)
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "user_id")  // retweets tablosundaki user_id foreign key olarak kullanılır
    private User user;
}
