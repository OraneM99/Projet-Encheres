package com.eni.eBIDou.article;

import com.eni.eBIDou.categorie.Categorie;
import com.eni.eBIDou.data.EtatVente;
import com.eni.eBIDou.pagination.PaginatedResult;
import com.eni.eBIDou.pagination.PaginationService;
import com.eni.eBIDou.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.eni.eBIDou.service.ServiceConstant.*;

@Service
public class ArticlePaginationService {

    @Autowired
    private ArticleIDAO daoArticle;

    @Autowired
    private PaginationService paginationService;

    private static final int DEFAULT_PAGE_SIZE = 6;
    private static final String DEFAULT_SORT_PROPERTY = "dateFinEncheres";

    // Récupère tous les articles avec pagination
    public ServiceResponse<PaginatedResult<Article>> getAllPaginated(int page) {
        Pageable pageable = paginationService.createPageable(page, DEFAULT_PAGE_SIZE, DEFAULT_SORT_PROPERTY);
        Page<Article> articlePage = daoArticle.selectAllPaginated(pageable);

        PaginatedResult<Article> result = paginationService.createPaginatedResult(articlePage);
        return ServiceResponse.buildResponse(
                CD_SUCCESS,
                "La liste des articles a été récupérée avec succès",
                result);
    }

    // Récupère les articles par nom avec pagination
    public ServiceResponse<PaginatedResult<Article>> getArticlesByNamePaginated(String name, int page) {
        Pageable pageable = paginationService.createPageable(page, DEFAULT_PAGE_SIZE, DEFAULT_SORT_PROPERTY);
        Page<Article> articlePage = daoArticle.selectByNamePaginated(name, pageable);

        PaginatedResult<Article> result = paginationService.createPaginatedResult(articlePage);
        String message = result.isEmpty()
                ? "Article non trouvé par son nom"
                : "Article récupéré par son nom";

        return ServiceResponse.buildResponse(
                result.isEmpty() ? CD_ERR_NOT_FOUND : CD_SUCCESS,
                message,
                result);
    }

    // Récupère les articles par catégorie avec pagination
    public ServiceResponse<PaginatedResult<Article>> getArticlesByCategoriePaginated(Categorie categorie, int page) {
        Pageable pageable = paginationService.createPageable(page, DEFAULT_PAGE_SIZE, DEFAULT_SORT_PROPERTY);
        Page<Article> articlePage = daoArticle.selectByCategoriePaginated(categorie, pageable);

        PaginatedResult<Article> result = paginationService.createPaginatedResult(articlePage);
        String message = result.isEmpty()
                ? "Aucun article pour cette catégorie"
                : "Liste d'article par catégorie récupérée avec succès";

        return ServiceResponse.buildResponse(
                result.isEmpty() ? CD_ERR_NOT_FOUND : CD_SUCCESS,
                message,
                result);
    }

    /**
     * Récupère les enchères en cours avec pagination
     */
    public ServiceResponse<PaginatedResult<Article>> getAllEncheresEnCoursPaginated(int page) {
        Pageable pageable = paginationService.createPageable(page, DEFAULT_PAGE_SIZE, DEFAULT_SORT_PROPERTY);
        Page<Article> articlePage = daoArticle.selectEncheresEnCoursPaginated(EtatVente.EN_COURS, pageable);

        PaginatedResult<Article> result = paginationService.createPaginatedResult(articlePage);
        String message = result.isEmpty()
                ? "Aucune enchère en cours trouvée"
                : "Liste des enchères en cours récupérée avec succès";

        return ServiceResponse.buildResponse(
                result.isEmpty() ? CD_ERR_NOT_FOUND : CD_SUCCESS,
                message,
                result);
    }

    // Recherche des articles selon un critère (nom ou catégorie) avec pagination
    public ServiceResponse<PaginatedResult<Article>> rechercherArticlesPaginated(String search, Categorie categorie, int page) {
        Pageable pageable = paginationService.createPageable(page, DEFAULT_PAGE_SIZE, DEFAULT_SORT_PROPERTY);
        Page<Article> articlePage;

        if ((search == null || search.isBlank()) && categorie == null) {
            articlePage = daoArticle.selectAllPaginated(pageable);
        } else if (categorie != null && (search == null || search.isBlank())) {
            articlePage = daoArticle.selectByCategoriePaginated(categorie, pageable);
        } else if (categorie == null) {
            articlePage = daoArticle.selectByNamePaginated(search, pageable);
        } else {
            articlePage = daoArticle.selectByNameAndCategoriePaginated(search, categorie, pageable);
        }

        PaginatedResult<Article> result = paginationService.createPaginatedResult(articlePage);

        String message = result.isEmpty()
                ? "Aucun article trouvé selon les critères"
                : "Articles récupérés avec succès";

        return ServiceResponse.buildResponse(
                result.isEmpty() ? CD_ERR_NOT_FOUND : CD_SUCCESS,
                message,
                result);
    }
}