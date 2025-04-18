package com.eni.eBIDou.images;

import com.eni.eBIDou.article.Article;
import com.eni.eBIDou.article.ArticleService;
import com.eni.eBIDou.service.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleUploadController {

    private final ArticleService articleService;
    private final AzureBlobStorageService azureBlobStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceResponse<Article>> uploadArticle(
            @RequestPart("nomArticle") String nomArticle,
            @RequestPart("description") String description,
            @RequestPart(name = "image", required = false) MultipartFile image) {

        Article article = new Article();
        article.setNomArticle(nomArticle);
        article.setDescription(description);

        try {
            if (image != null && !image.isEmpty()) {
                String imageUrl = azureBlobStorageService.uploadFile(image);
                article.setUrlImage(imageUrl);
            }

            ServiceResponse<Article> response = articleService.addArticle(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ServiceResponse.buildResponse(String.valueOf(500), "Erreur upload : " + e.getMessage(), null));
        }
    }
}
