package com.example.restApi.domain.article.service;

import com.example.restApi.domain.article.controller.ApiV1ArticleController;
import com.example.restApi.domain.article.entity.Article;
import com.example.restApi.domain.article.repository.ArticleRepository;
import com.example.restApi.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public RsData<Article> findById(Long id) {

        return articleRepository.findById(id)
                .map(
                    article -> {
                        return RsData.of("S-1","조회완료",article);
                    }
                )
                .orElseGet(
                        () -> RsData.of("F-1", "존재하지 않는 게시글 입니다.")
                );

    }

    public Article save(ApiV1ArticleController.ArticleSaveReq req) {

        Article article = Article.builder().title(req.getTitle()).content(req.getContent()).build();
        articleRepository.save(article);

        return article;
    }

    public RsData delete(Long id) {

        return articleRepository.findById(id)
                .map(
                        article -> {
                            articleRepository.delete(article);
                            return RsData.of("S-3","삭제완료");
                        }
                )
                .orElseGet(
                        () -> RsData.of("F-1", "존재하지 않는 게시글 입니다.")
                );
    }

    public RsData<Article> update(ApiV1ArticleController.ArticleEditReq req) {

        return articleRepository.findById(req.getId())
                .map(
                        article -> {
                            Article updateArticle = article.toBuilder().title(req.getTitle()).content(req.getContent()).build();
                            articleRepository.save(updateArticle);
                            return RsData.of("S-3","삭제완료",updateArticle);
                        }
                )
                .orElseGet(
                        () -> RsData.of("F-1", "존재하지 않는 게시글 입니다.")
                );

    }
}
