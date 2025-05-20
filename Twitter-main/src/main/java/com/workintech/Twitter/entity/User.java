@AllArgsConstructor  // Tüm alanları parametre alan constructor oluşturur (Lombok)
@NoArgsConstructor   // Parametresiz constructor oluşturur (Lombok)
@Getter              // Tüm alanlar için getter metotları oluşturur (Lombok)
@Setter              // Tüm alanlar için setter metotları oluşturur (Lombok)
@Entity              // Bu sınıfın bir JPA entity’si olduğunu belirtir
@Table(name = "users", schema = "twitter")  // Veritabanında twitter şemasındaki users tablosuna karşılık gelir
public class User implements UserDetails {    // Spring Security’nin UserDetails arayüzünü implement eder

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID veritabanı tarafından otomatik artan olarak atanır
    @Column(name = "user_id")  // Veritabanındaki sütun adı
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz.")  // Kullanıcı adı boş olmamalı, validation kuralı
    @Size(min = 3, max = 20, message = "Kullanıcı adı 3-20 karakter arasında olmalıdır.") // Uzunluk sınırı
    @Column(name = "user_name")  // Veritabanındaki sütun adı
    private String username;

    @NotBlank(message = "Şifre boş olamaz.")  // Şifre boş olmamalı, validation kuralı
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır.")  // Minimum şifre uzunluğu
    @Column(name = "password")  // Veritabanındaki sütun adı
    private String password;

    // Bir kullanıcı birçok tweet atabilir (One-to-Many ilişkisi)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tweet> tweets;

    // Kullanıcının yazdığı yorumlar (One-to-Many ilişkisi)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    // Kullanıcının yaptığı beğeniler (One-to-Many ilişkisi)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;

    // Kullanıcının yaptığı retweetler (One-to-Many ilişkisi)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Retweet> retweets;

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }

    // Aşağıdaki metotlar Spring Security için UserDetails arayüzünden gelir

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();  // Kullanıcının rol ya da yetkisi yoksa boş liste döner
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Hesap süresi dolmamış olarak kabul edilir
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Hesap kilitli değil
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Kimlik bilgileri süresi dolmamış
    }

    @Override
    public boolean isEnabled() {
        return true;  // Kullanıcı etkin (aktif)
    }
}
