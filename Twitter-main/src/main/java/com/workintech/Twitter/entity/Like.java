@AllArgsConstructor  // Tüm alanları parametre alan constructor (Lombok)
@NoArgsConstructor   // Parametresiz constructor (Lombok)
@Data                // Getter, Setter, toString, equals, hashCode metodlarını otomatik oluşturur (Lombok)
@EqualsAndHashCode   // equals ve hashCode metodlarını otomatik oluşturur (Lombok)
@Entity              // JPA entity olduğunu belirtir
@Table(
        name = "likes", // Veritabanındaki tablo adı
        schema = "twitter", // Şema adı
        uniqueConstraints = { // Tablo üzerinde benzersiz kısıtlama (unique constraint) tanımlanıyor
                @UniqueConstraint(
                        name = "unique_user_tweet", // Constraint adı
                        columnNames = {"user_id", "tweet_id"} // Bu iki kolon kombinasyonu benzersiz olmalı, yani aynı kullanıcı aynı tweet'i sadece bir kez beğenebilir
                )
        }
)
// Kullanıcı bir tweet'i sadece bir kez beğenebilir kuralını sağlar
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID otomatik artan birincil anahtar olarak oluşturulur
    @Column(name = "like_id") // Veritabanındaki sütun adı
    private Long id;

    // Bir tweet'in birden çok beğenisi olabilir (Many-to-One ilişkisi)
    @ManyToOne(
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH },
            fetch = FetchType.LAZY // Tweet verisi yalnızca gerektiğinde yüklenir
    )
    @JoinColumn(name = "tweet_id") // likes tablosunda tweet_id sütunu foreign key olarak kullanılır
    private Tweet tweet;

    // Bir kullanıcının birden fazla beğenisi olabilir (Many-to-One ilişkisi)
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "user_id") // likes tablosunda user_id sütunu foreign key olarak kullanılır
    private User user;

}
