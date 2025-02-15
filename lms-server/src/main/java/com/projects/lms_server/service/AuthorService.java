package com.projects.lms_server.service;

import com.projects.lms_server.dto.AuthorDTO;
import com.projects.lms_server.entites.AuthorEntity;
import com.projects.lms_server.exceptions.ResourceAlreadyExistsException;
import com.projects.lms_server.exceptions.ResourceNotFoundException;
import com.projects.lms_server.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;


    /* add the new Author into the DataBase */
    @Transactional
    public AuthorDTO addNewAuthor( AuthorDTO authorDTO) {
        /* fetching the email of from the author DTO */
        String email = authorDTO.getEmail();

        /* checking that is author already exist in DB */
        Optional<AuthorEntity> optAuthor = authorRepository.findByEmail(email);
        if (optAuthor.isPresent())
            throw new ResourceAlreadyExistsException("Author Already exist ! ID:"+ optAuthor.get().getAuthorId());

        /* creating the new Author object and saving it into the database */
        AuthorEntity toBeCreated = modelMapper.map(authorDTO,AuthorEntity.class);
        AuthorEntity savedAuthor = authorRepository.save(toBeCreated);

        /* returning the saved author dto */
        return  modelMapper.map(savedAuthor,AuthorDTO.class);
    }


    /* get All Author Details*/
    public Page<AuthorDTO> getAllAuthorDetails(int page) {
        Pageable pageable = PageRequest.of(page,6);
        Page<AuthorEntity> allAuthors = authorRepository.findAll(pageable);
        return allAuthors.map((author)->modelMapper.map(author,AuthorDTO.class));
    }

    /* get single author details by their id*/
    public AuthorDTO getAuthorById(String id) {
        Optional<AuthorEntity> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isEmpty())
            throw new ResourceNotFoundException("Invalid author ID:"+ id);

        /* return the DTO of current author */
        return  modelMapper.map(optionalAuthor.get(),AuthorDTO.class);

    }

}
