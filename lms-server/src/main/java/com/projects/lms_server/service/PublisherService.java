package com.projects.lms_server.service;

import com.projects.lms_server.dto.PublisherDTO;
import com.projects.lms_server.entites.PublisherEntity;
import com.projects.lms_server.exceptions.ResourceAlreadyExistsException;
import com.projects.lms_server.exceptions.ResourceNotFoundException;
import com.projects.lms_server.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final ModelMapper modelMapper;

    /* adding the new publisher into the database*/
    @Transactional
    public PublisherDTO addNewPublisher(@Valid PublisherDTO publisherDTO) {
        String email = publisherDTO.getEmail();

        /* checking that does any publisher exist with this email */
        Optional<PublisherEntity> optionalPublisher = publisherRepository.findByEmail(email);

        /* if Publisher exist then throwing an error */
        if (optionalPublisher.isPresent())
            throw  new ResourceAlreadyExistsException("Publisher Already exist !,ID:"+optionalPublisher.get().getPublisherId());

        /* creating new publisher and adding them into the database */
        PublisherEntity publisher   = modelMapper.map(publisherDTO,PublisherEntity.class);
        PublisherEntity savedPublisher = publisherRepository.save(publisher);
        /* return the new saved publisher */
        return modelMapper.map(savedPublisher,PublisherDTO.class);
    }

    /* get all Publisher */
    public Page<PublisherDTO> getAllPublisher(int page) {
        Pageable pageable = PageRequest.of(page,6);
        Page<PublisherEntity> publishers = publisherRepository.findAll(pageable);
        return publishers.map((pub)->modelMapper.map(pub,PublisherDTO.class));
    }

    /* searching the details of individual Publisher By their id*/
    public PublisherDTO getPublisherById(String id) {
        Optional<PublisherEntity> optionalPublisher = publisherRepository.findById(id);

        if (optionalPublisher.isEmpty())
            throw  new ResourceNotFoundException("Invalid Publisher Id:"+ id);
        return modelMapper.map(optionalPublisher.get(),PublisherDTO.class);

    }

}
