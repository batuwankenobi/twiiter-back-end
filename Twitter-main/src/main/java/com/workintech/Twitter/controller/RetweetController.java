@RestController // Bu sınıfın bir REST Controller olduğunu belirtir. HTTP isteklerini karşılar ve yanıtları JSON olarak döner.
@RequestMapping("/retweet") // Bu sınıf altındaki tüm endpoint'ler "/retweet" ile başlar.
@AllArgsConstructor // Lombok anotasyonu: final değişkenler için otomatik constructor üretir.
public class RetweetController {

    private final RetweetService retweetService; // Retweet işlemlerini gerçekleştirecek olan servis sınıfı

    // Tweet ID ve kullanıcı ID bilgisi ile retweet işlemi yapılır
    @PostMapping("/{userId}/{tweetId}") // Örnek istek: POST /retweet/1/45
    public String retweetTweet(@RequestParam Long userId, @RequestParam Long tweetId) {
        try {
            // Retweet işlemi servis katmanına iletilir
            retweetService.retweet(userId, tweetId);
            return "Tweet retweeted successfully"; // Başarılıysa mesaj döner
        } catch (IllegalArgumentException e) {
            return e.getMessage(); // Hatalı işlem varsa ilgili mesajı döner
        }
    }

    // Retweet'i geri alma işlemi yapılır (unretweet)
    @DeleteMapping("/unretweet/{userId}/{tweetId}") // Örnek istek: DELETE /retweet/unretweet/1/45
    public String unretweetTweet(@RequestParam Long userId, @RequestParam Long tweetId) {
        try {
            // Unretweet işlemi servis katmanına iletilir
            retweetService.unretweet(userId, tweetId);
            return "Tweet unretweeted successfully"; // Başarılıysa mesaj döner
        } catch (IllegalArgumentException e) {
            return e.getMessage(); // Hatalı işlem varsa ilgili mesajı döner
        }
    }
}
