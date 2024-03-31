package me.keills.service;

import me.keills.model.Publisher;

public interface PublisherService {

    void savePublisher(Publisher publisher);
    void deletePublisher(Long id);
    Publisher getPublisherById(Long id);
}
