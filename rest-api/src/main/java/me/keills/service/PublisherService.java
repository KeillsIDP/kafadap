package me.keills.service;

import me.keills.model.Publisher;

/**
 * Сервисный интерфейс для работы с издателями игр.
 */
public interface PublisherService {

    /**
     * Сохранить издателя.
     * @param publisher {@link Publisher} - объект издателя для сохранения
     * @return {@link Publisher} сохраненный объект издателя
     */
    Publisher savePublisher(Publisher publisher);

    /**
     * Удалить издателя по идентификатору.
     * @param id {@link Long} - идентификатор издателя для удаления
     */
    void deletePublisher(Long id);

    /**
     * Получить издателя по идентификатору.
     * @param id {@link Long} - идентификатор издателя
     * @return {@link Publisher} объект издателя с указанным идентификатором
     */
    Publisher getPublisherById(Long id);
}
