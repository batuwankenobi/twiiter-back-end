@AllArgsConstructor // Lombok anotasyonu. Constructor ile bağımlılıkların (CommentService) otomatik enjekte edilmesini sağlar.
@RestController // Bu sınıfın bir REST controller olduğunu belirtir. @Controller + @ResponseBody birleşimidir.
@RequestMapping("/comment") // Bu controller altındaki tüm endpoint'ler "/comment" URL'si ile başlar.
public class CommentController {

    private final CommentService commentService; // Yorum işlemleri için gerekli servis sınıfı.

    @PostMapping
    public ResponseEntity<Comment> addComment(
            @RequestParam Long tweetId, // Yorum yapılacak tweet’in ID'si.
            @RequestParam Long userId,  // Yorumu yapan kullanıcının ID'si.
            @RequestBody String content) { // Yorumun içeriği, HTTP body'sinden alınır.
        Comment createdComment = commentService.addComment(tweetId, userId, content); // Servis katmanında yorum eklenir.
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        // 201 Created yanıtı dönülür. Bu, yeni bir kaynak başarıyla oluşturulduğunu belirtir.
    }

    // Yorum güncelleme işlemi yapılırken yorumun sahibi olma durumu kontrol ediliyor.
    @PutMapping("/{id}") // Yorumun güncelleneceği endpoint. Örnek: PUT /comment/5
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id, // Güncellenecek yorumun ID'si.
            @RequestParam Long userId, // Yorumu güncellemek isteyen kullanıcının ID'si.
            @RequestBody String newContent) { // Yeni yorum içeriği.
        Comment updatedComment = commentService.updateComment(id, userId, newContent); // Servis katmanında güncelleme yapılır.
        return ResponseEntity.ok(updatedComment);
        // 200 OK yanıtı dönülür. Güncelleme işlemi başarılı olmuştur ve güncel veri döndürülür.
    }

    // Yorum silme işlemi yapılırken yorumun sahibi ya da tweetin sahibi olma durumu kontrol ediliyor.
    @DeleteMapping("/{id}") // Yorum silme endpoint'i. Örnek: DELETE /comment/5
    public ResponseEntity<Comment> deleteComment(
            @PathVariable Long id, // Silinecek yorumun ID’si.
            @RequestParam Long userId) { // Yorumu silmek isteyen kullanıcının ID’si.
        commentService.deleteComment(id, userId); // Servis katmanında silme işlemi yapılır.
        return ResponseEntity.noContent().build();
        // 204 No Content döndürülür. Bu, isteğin başarılı olduğunu fakat dönecek bir içerik olmadığını belirtir.
        // .build() metodu ile boş bir response nesnesi oluşturulur.
    }

}
