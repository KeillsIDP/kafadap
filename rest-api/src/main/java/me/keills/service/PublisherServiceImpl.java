package me.keills.service;

import me.keills.exception.publisher.PublisherNameNotPresent;
import me.keills.exception.publisher.PublisherNotFoundException;
import me.keills.model.Publisher;
import me.keills.repo.PublisherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl implements PublisherService {
    @Autowired
    private PublisherRepo publisherRepo;
    @Override
    public void savePublisher(Publisher publisher) {
        if(publisher.getName()==null || publisher.getName().length()==0)
            throw new PublisherNameNotPresent("Publisher name can't be empty");
        publisherRepo.save(publisher);
    }

    @Override
    public void deletePublisher(Long id) {
        publisherRepo.deleteById(id);
    }

    @Override
    public Publisher getPublisherById(Long id) {
        return publisherRepo.findById(id).orElseThrow(() -> new PublisherNotFoundException("Publisher not found"));
    }
}
