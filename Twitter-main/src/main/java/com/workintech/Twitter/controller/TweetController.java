@AllArgsConstructor // Lombok: final alanlar için constructor oluşturur
@RestController // REST API olduğunu belirtir
@RequestMapping("/tweets") // Bu controller'daki tüm endpoint'ler "/tweets" ile başlar
public class TweetController {

    private final TweetService tweetService; // İş mantığı bu serviste

    // 1. Tweet Oluşturma
    @PostMapping("/user/{userId}")
    public ResponseEntity<TweetResponse> createTweet(
            @PathVariable Long userId,
            @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody Tweet tweet) {

        // Giriş yapan kullanıcı, sadece kendi adına tweet atabilir
        if (!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        Tweet createdTweet = tweetService.createTweet(userId, tweet); // Tweet servise iletilir
        TweetResponse tweetResponse = new TweetResponse( // DTO hazırlanır
                createdTweet.getId(),
                createdTweet.getText(),
                createdTweet.getUser().getUsername(),
                createdTweet.getLikeCount(),
                createdTweet.getRetweetCount()
        );
        return new ResponseEntity<>(tweetResponse, HttpStatus.CREATED); // 201 Created
    }

    // 2. Belirli Kullanıcının Tüm Tweet'lerini Getir
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TweetResponse>> getAllTweetsByUserId(@PathVariable Long userId) {
        List<Tweet> tweets = tweetService.getAllTweetsByUserId(userId);
        List<TweetResponse> tweetResponses = tweets.stream()
                .map(tweet -> new TweetResponse(
                        tweet.getId(),
                        tweet.getText(),
                        tweet.getUser().getUsername(),
                        tweet.getLikeCount(),
                        tweet.getRetweetCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tweetResponses);
    }

    // 3. Belirli Bir Tweet’i Getir
    @GetMapping("/{id}")
    public ResponseEntity<TweetResponse> getTweetById(@PathVariable Long id) {
        Optional<Tweet> tweet = tweetService.getTweetById(id);

        return tweet.map(t -> new ResponseEntity<>(new TweetResponse(
                        t.getId(),
                        t.getText(),
                        t.getUser().getUsername(),
                        t.getLikeCount(),
                        t.getRetweetCount()), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build()); // Tweet yoksa 404 döner
    }

    // 4. Tüm Tweet’leri Listele
    @GetMapping
    public ResponseEntity<List<TweetResponse>> getAllTweets() {
        List<Tweet> tweets = tweetService.getAllTweets();
        List<TweetResponse> tweetResponses = tweets.stream()
                .map(tweet -> new TweetResponse(
                        tweet.getId(),
                        tweet.getText(),
                        tweet.getUser().getUsername(),
                        tweet.getLikeCount(),
                        tweet.getRetweetCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tweetResponses);
    }

    // 5. Tweet Güncelleme (Sadece Sahibi Güncelleyebilir)
    @PutMapping("/user/{userId}/tweet/{id}")
    public ResponseEntity<TweetResponse> updateTweet(
            @PathVariable Long id,
            @RequestBody Tweet updatedTweet,
            @PathVariable Long userId) {

        Tweet tweet = tweetService.updateTweet(id, updatedTweet, userId); // Servis tweet’in sahibini kontrol eder
        TweetResponse tweetResponse = new TweetResponse(
                tweet.getId(),
                tweet.getText(),
                tweet.getUser().getUsername(),
                tweet.getLikeCount(),
                tweet.getRetweetCount());

        return ResponseEntity.ok(tweetResponse); // 200 OK
    }

    // 6. Tweet Silme (Sadece Sahibi Silebilir)
    @DeleteMapping("/user/{userId}/tweet/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable Long id, @PathVariable Long userId) {
        tweetService.deleteTweet(id, userId); // Sahiplik kontrolü serviste yapılır
        return ResponseEntity.ok("Tweet successfully deleted");
    }
}
