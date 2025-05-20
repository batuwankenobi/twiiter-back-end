@Configuration // Bu sınıfın bir Spring konfigürasyon sınıfı olduğunu belirtir.
@AllArgsConstructor // Lombok anotasyonu. Constructor enjeksiyonu için gerekli tüm final alanlara otomatik constructor üretir.
public class SecurityConfig {

    private final UserService userService; // Kullanıcıya ait bilgileri sağlayan servis. Authentication işlemleri için kullanılır.

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Parola şifreleme işlemleri için BCrypt algoritmasını kullanan bir PasswordEncoder bean’i tanımlanıyor.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP güvenlik yapılandırmaları burada yapılır.
        http.csrf(csrf -> csrf.disable()) // CSRF koruması devre dışı bırakılıyor (genelde stateless API'lerde yapılır).
                .cors(cors -> cors.configurationSource(request -> {
                    // CORS yapılandırması. Frontend'in (örneğin React) backend'e erişebilmesi için gerekli.
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:5173")); // Yalnızca bu adresten gelen istekler kabul edilir.
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // İzin verilen HTTP metodları.
                    corsConfig.setAllowedHeaders(List.of("*")); // Tüm header'lara izin verilir.
                    corsConfig.setAllowCredentials(true); // Kimlik doğrulama bilgileri (cookie, auth header vs.) iletilebilir.
                    return corsConfig;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Bu endpoint'lere (register ve login) kimlik doğrulama gerekmez.
                        .requestMatchers("/user/register", "/user/login").permitAll()
                        // Diğer tüm istekler için kimlik doğrulama gerekir.
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()) // Basic Authentication etkinleştirilir.
                .sessionManagement(session -> session
                        // Oturum her zaman oluşturulur. (Stateless bir yapı için SessionCreationPolicy.STATELESS kullanılabilir.)
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        return http.build(); // Yapılandırılmış güvenlik zinciri döndürülür.
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // AuthenticationManager bean’i oluşturulur. Bu, kimlik doğrulama işlemlerini yöneten bileşendir.
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // Kullanıcı bilgilerini doğrulamak için userService ve passwordEncoder kullanılır.
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build(); // AuthenticationManager örneği döndürülür.
    }
}
