package com.example.restApi.domain.article.controller;

import com.example.restApi.domain.article.entity.Article;
import com.example.restApi.domain.article.service.ArticleService;
import com.example.restApi.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ApiV1ArticleController {

    private final ArticleService articleService;


    // 다건조회
    @Getter
    @AllArgsConstructor
    public static class ArticlesRes{
        private List<Article> articles;

    }

    @GetMapping("")
    public RsData<ArticlesRes> getArticles(){

        return RsData.of("S-1","조회 완료",new ArticlesRes(articleService.findAll()));

    }

    // 단건 조회
    @Getter
    public static class ArticleRes{

        private Article article;
        ArticleRes(Article article){
            this.article = article;
        }
    }

    @GetMapping("/{id}")
    public RsData<ArticleRes> getArticle(@PathVariable(value = "id")Long id){

        RsData result = articleService.findById(id);

        if(result.getIsFail()) return result;

        return RsData.of("S-1","조회 완료",new ArticleRes((Article) result.getData()));

    }

    // 생성
    @Getter
    @Builder
    public static class ArticleSaveReq{

        @NotBlank(message = "제목은 필수")
        private String title;

        @NotBlank(message = "내용도 필수")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class ArticleSaveRes{
        private Article article;
    }

    @PostMapping("")
    public RsData<ArticleSaveRes> save(@Valid @RequestBody ArticleSaveReq req, BindingResult br){

        if(br.hasErrors()){
            return RsData.of("F-1","유효하지 않은 입력", null);
        }

        return RsData.of("S-2","저장 완료",new ArticleSaveRes(articleService.save(null, req)));
    }


    // 삭제
    @DeleteMapping("/{id}")
    public RsData delete(@PathVariable(value = "id")Long id){

        return articleService.delete(id);
    }

    // 수정
    @Getter
    public static class ArticleEditReq{

        @NotNull(message = "유효하지 않은 접근")
        private Long id;

        @NotBlank(message = "제목은 필수")
        private String title;

        @NotBlank(message = "내용도 필수")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class ArticleEditRes{
        private Article article;
    }
    @PatchMapping("")
    public RsData<ArticleEditRes> update(@Valid @RequestBody ArticleEditReq req, BindingResult br){

        if(br.hasErrors()){
            return RsData.of("F-1","유효하지 않은 입력", null);
        }

        RsData<Article> result = articleService.update(req);

        if(result.getIsFail()){
            return RsData.of(result.getResultCode(), result.getMsg());
        }

        return RsData.of(result.getResultCode(), result.getMsg(), new ArticleEditRes(result.getData()));
    }
}
