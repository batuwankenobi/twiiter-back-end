@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;                // Kullanıcı işlemleri için servis katmanı
    private final PasswordEncoder passwordEncoder;        // Şifreleri hashlemek için encoder
    private final AuthenticationManager authenticationManager;  // Spring Security'de kimlik doğrulama yöneticisi

    // Kullanıcı kayıt endpoint'i
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody User user) {
        // Gelen kullanıcının şifresini hash'leyerek güvenli hale getir
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Kullanıcıyı veritabanına kaydet
        User createdUser = userService.save(user);

        // Kayıt sonrası geri dönecek cevap objesi oluştur
        RegisterResponse registerResponse = new RegisterResponse(
                createdUser.getUsername(),
                createdUser.getPassword(),
                createdUser.getTweets(),
                createdUser.getComments(),
                createdUser.getRetweets()
        );

        // HTTP 201 Created statüsü ile kayıt edilen kullanıcı bilgilerini döndür
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    // Kullanıcı giriş endpoint'i
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> credentials) {
        // İstekten kullanıcı adı ve şifre al
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Eksik bilgi varsa 400 Bad Request döndür
        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
        }

        try {
            // Spring Security ile kimlik doğrulama işlemi yap
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Başarılıysa 200 OK ve mesaj döndür
            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            // Doğrulama başarısızsa 401 Unauthorized döndür
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Kullanıcı çıkış endpoint'i
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        // Mevcut HTTP oturumunu sonlandır
        session.invalidate();

        // Başarı mesajı döndür
        return ResponseEntity.ok("User logged out successfully.");
    }

    // Oturum açık mı kontrol etmek için profil endpoint'i
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(HttpSession session) {
        // Oturumdan kullanıcı bilgisi al
        User user = (User) session.getAttribute("user");

        // Kullanıcı yoksa 401 Unauthorized döndür
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Kullanıcı varsa profil bilgisini döndür
        return ResponseEntity.ok(user);
    }
}
