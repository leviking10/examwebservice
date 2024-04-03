package com.groupeisi.examwebservice.web.rest;

import com.groupeisi.examwebservice.service.DateInfoService;
import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/calendar")
public class DayFinderResource {

    private final DateInfoService dateInfoService; // ou DateInfoRepository si vous n'utilisez pas de service

    public DayFinderResource(DateInfoService dateInfoService) {
        this.dateInfoService = dateInfoService;
    }

    @GetMapping("/dayfinder")
    public ResponseEntity<DateInfoDTO> getDayOfWeek(@RequestParam String date) {
        // Ici, utilisez votre service pour calculer le jour de la semaine et sauvegarder la requête
        DateInfoDTO result = dateInfoService.findDayOfWeek(date);
        return ResponseEntity.ok(result);
    }
}
