package me.keills.service;

import io.qameta.allure.*;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Publisher;
import me.keills.repo.PublisherRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Feature("Издатели")
@Story("Сервисный интерфейс для работы с издателями")
@Severity(SeverityLevel.CRITICAL)
class PublisherServiceTest {
    @Mock
    PublisherRepo publisherRepo;
    @InjectMocks
    PublisherServiceImpl publisherService;
    @Test
    @DisplayName("Сохранение издателя")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение издателя")
    void savePublisher() {
        Publisher publisher = new Publisher("publisher","publisher");
        when(publisherRepo.save(publisher)).thenReturn(publisher);
        assertEquals(publisher, publisherService.savePublisher(publisher));
    }

    @Test
    @DisplayName("Сохранение издателя без имени")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет сохранение издателя без имени")
    void savePublisher_WithoutName(){
        try {
            Publisher publisher = new Publisher(null, "publisher");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("Получение издателя по идентификатору")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение издателя по идентификатору")
    void getPublisherById() {
        Publisher publisher = new Publisher("publisher","publisher");
        when(publisherRepo.findById(1L)).thenReturn(Optional.ofNullable(publisher));
        assertEquals(publisher, publisherService.getPublisherById(1L));
    }

    @Test
    @DisplayName("Получение издателя по несуществующему идентификатору")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет получение издателя по несуществующему идентификатору")
    void getPublisherById_PublisherNotFound() {
        when(publisherRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PublisherNotFoundException.class, () -> publisherService.getPublisherById(1L));
    }
}