package com.groupeisi.examwebservice.service.impl;

import com.groupeisi.examwebservice.domain.DateInfo;
import com.groupeisi.examwebservice.repository.DateInfoRepository;
import com.groupeisi.examwebservice.service.DateInfoService;
import com.groupeisi.examwebservice.service.dto.DateInfoDTO;
import com.groupeisi.examwebservice.service.mapper.DateInfoMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.groupeisi.examwebservice.domain.DateInfo}.
 */
@Service
@Transactional
public class DateInfoServiceImpl implements DateInfoService {

    private final Logger log = LoggerFactory.getLogger(DateInfoServiceImpl.class);

    private final DateInfoRepository dateInfoRepository;

    private final DateInfoMapper dateInfoMapper;

    public DateInfoServiceImpl(DateInfoRepository dateInfoRepository, DateInfoMapper dateInfoMapper) {
        this.dateInfoRepository = dateInfoRepository;
        this.dateInfoMapper = dateInfoMapper;
    }

    @Override
    public DateInfoDTO save(DateInfoDTO dateInfoDTO) {
        log.debug("Request to save DateInfo : {}", dateInfoDTO);
        DateInfo dateInfo = dateInfoMapper.toEntity(dateInfoDTO);
        dateInfo = dateInfoRepository.save(dateInfo);
        return dateInfoMapper.toDto(dateInfo);
    }

    @Override
    public DateInfoDTO update(DateInfoDTO dateInfoDTO) {
        log.debug("Request to update DateInfo : {}", dateInfoDTO);
        DateInfo dateInfo = dateInfoMapper.toEntity(dateInfoDTO);
        dateInfo = dateInfoRepository.save(dateInfo);
        return dateInfoMapper.toDto(dateInfo);
    }

    @Override
    public Optional<DateInfoDTO> partialUpdate(DateInfoDTO dateInfoDTO) {
        log.debug("Request to partially update DateInfo : {}", dateInfoDTO);

        return dateInfoRepository
            .findById(dateInfoDTO.getId())
            .map(existingDateInfo -> {
                dateInfoMapper.partialUpdate(existingDateInfo, dateInfoDTO);

                return existingDateInfo;
            })
            .map(dateInfoRepository::save)
            .map(dateInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DateInfoDTO> findAll() {
        log.debug("Request to get all DateInfos");
        return dateInfoRepository.findAll().stream().map(dateInfoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DateInfoDTO> findOne(Long id) {
        log.debug("Request to get DateInfo : {}", id);
        return dateInfoRepository.findById(id).map(dateInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DateInfo : {}", id);
        dateInfoRepository.deleteById(id);
    }
    public DateInfoDTO findDayOfWeek(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayOfWeekFrench = translateDayOfWeek(dayOfWeek); // Implémentez cette méthode selon vos besoins

        DateInfo dateInfo = new DateInfo();
        dateInfo.setDate(dateString);
        dateInfo.setDayOfWeek(dayOfWeekFrench);
        dateInfoRepository.save(dateInfo);

        return new DateInfoDTO(dateInfo); // Assurez-vous que le constructeur de DateInfoDTO accepte un objet DateInfo
    }
    private String translateDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "Lundi";
            case TUESDAY:
                return "Mardi";
            case WEDNESDAY:
                return "Mercredi";
            case THURSDAY:
                return "Jeudi";
            case FRIDAY:
                return "Vendredi";
            case SATURDAY:
                return "Samedi";
            case SUNDAY:
                return "Dimanche";
            default:
                throw new IllegalArgumentException("Unknown DayOfWeek: " + dayOfWeek);
        }
    }
}
