@RestController // Bu sınıfın bir REST controller olduğunu belirtir. HTTP isteklerine JSON cevabı verir.
@RequestMapping("/likes") // Bu controller altındaki tüm endpoint'ler "/likes" ile başlar.
@AllArgsConstructor // Lombok: final alanlar için otomatik constructor oluşturur.
public class LikeController {

    private final LikeService likeService; // Beğeni işlemlerini yöneten servis
    private final UserService userService; // Kullanıcı bilgilerini almak için kullanılan servis

    @PostMapping("/{tweetId}") // Tweet beğenme işlemi: POST /likes/{tweetId}
    public ResponseEntity<String> likeTweet(@PathVariable Long tweetId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Giriş yapan kullanıcının username bilgisi üzerinden User nesnesini getiriyoruz
            User user = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with username " + userDetails.getUsername() + " not found"));

            Long authenticatedUserId = user.getId(); // Kullanıcının ID'si alınıyor
            System.out.println("Authenticated User ID: " + authenticatedUserId); // Konsola yazdırma (isteğe bağlı, debug için)

            likeService.likeTweet(authenticatedUserId, tweetId); // Beğeni işlemi servis katmanına gönderiliyor
            return ResponseEntity.ok("Tweet liked successfully"); // 200 OK ve başarı mesajı dönülüyor
        } catch (Exception e) {
            // Eğer beklenmeyen bir hata oluşursa 500 INTERNAL SERVER ERROR dönülüyor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/dislike/{tweetId}") // Tweet beğenmeme işlemi: POST /likes/dislike/{tweetId}
    public ResponseEntity<String> dislikeTweet(@PathVariable Long tweetId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Aynı şekilde kullanıcı doğrulama yapılıyor
            User user = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User with username " + userDetails.getUsername() + " not found"));

            Long authenticatedUserId = user.getId();
            System.out.println("Authenticated User ID: " + authenticatedUserId);

            likeService.dislikeTweet(authenticatedUserId, tweetId); // Dislike işlemi servis katmanına gönderiliyor
            return ResponseEntity.ok("Tweet disliked successfully"); // 200 OK ve başarı mesajı dönülüyor
        } catch (Exception e) {
            // Hatalı durumda yine 500 hatası döndürülüyor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Bir tweet'in kaç beğeni aldığını döndüren endpoint
    @GetMapping("/count/{tweetId}") // GET /likes/count/{tweetId}
    public ResponseEntity<Long> countLikes(@PathVariable Long tweetId) {
        try {
            Long likeCount = likeService.countLikes(tweetId); // Beğeni sayısı servis katmanından alınır
            return ResponseEntity.ok(likeCount); // 200 OK ve beğeni sayısı dönülür
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Hata durumunda null içerikle 500 hatası dönülür
        }
    }
}
