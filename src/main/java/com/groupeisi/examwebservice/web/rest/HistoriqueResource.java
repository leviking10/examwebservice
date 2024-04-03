package com.groupeisi.examwebservice.web.rest;
import com.groupeisi.examwebservice.service.DateInfoService;
import com.groupeisi.examwebservice.service.SearchHistoryService;
import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
import com.groupeisi.examwebservice.service.dto.SearchHistoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/historique")
public class HistoriqueResource {

    private final SearchHistoryService searchHistoryService; // ou SearchHistoryRepository

    public HistoriqueResource(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SearchHistoryDTO>> getAllSearchHistories() {
        // Récupérez l'historique depuis le service ou repository
        List<SearchHistoryDTO> history = searchHistoryService.findAll();
        return ResponseEntity.ok(history);
    }
}
