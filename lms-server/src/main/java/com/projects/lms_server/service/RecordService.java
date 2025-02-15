package com.projects.lms_server.service;

import com.projects.lms_server.dto.RecordDTO;
import com.projects.lms_server.dto.UserDTO;
import com.projects.lms_server.entites.RecordEntity;
import com.projects.lms_server.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {
    private final static Integer PAGE_SIZE = 6;
    private final RecordRepository recordRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /* get all borrowed book record of member*/
    public List<RecordDTO> getMemberBorrowedBooks(String id) {
        if(Objects.equals(id, "NA"))
            id = userService.getCurrentUser().getUserId();
        /* Find records where the user has borrowed books but not returned them*/
        List<RecordEntity> records = recordRepository.findByUser_UserIdAndReturnDateIsNull(id);
        return records.stream()
                .map(rec->{
                    RecordDTO record = modelMapper.map(rec,RecordDTO.class);
                    record.setBookTitle(rec.getBookCopy().getBook().getTitle());
                    record.setMemberName(rec.getUser().getName());
                    record.setIssuedBy(rec.getIssuedBy().getUserId());
                    record.setCondition(rec.getBookCopy().getCondition());
                    record.setFormat(rec.getBookCopy().getFormat());
                    record.setImage(rec.getBookCopy().getBook().getImage());
                    return  record;
                }).toList();
    }

    /*get all borrowed records between the dates */
    public Page<RecordDTO> getAllBorrowedBooks(LocalDate startDate, LocalDate endDate,int page) {
        Pageable pageable = PageRequest.of(page,3);

        Page<RecordEntity> allRecord ;
        /* if user  don't provide start and end date*/
        log.info("start is:{} and end is:{}",startDate,endDate);

        if (startDate != null && endDate != null)
            allRecord = recordRepository.findByBorrowDateBetweenAndReturnDateIsNull(startDate,endDate,pageable);
        else {
            log.info("i m within where start or end is null");
            allRecord  = recordRepository.findByReturnDateIsNull(pageable);
        }

        return allRecord.map(rec -> {
            RecordDTO record = modelMapper.map(rec, RecordDTO.class);
            record.setMemberName(rec.getUser().getName());
            record.setBookTitle(rec.getBookCopy().getBook().getTitle());
            record.setIssuedBy(rec.getIssuedBy().getUserId());
            log.info("record{}",rec);
            return record;
        });
    }

    /* get record between the dates*/
    public Page<RecordDTO> getAllRecord(LocalDate startDate, LocalDate endDate, int page) {

        // Create Pageable object with page number and page size
        Pageable pageable = PageRequest.of(page, 3);

        Page<RecordEntity> allRecord;

        // Check if startDate and endDate are provided
        if (startDate != null && endDate != null) {
            // Get records between startDate and endDate
            allRecord = recordRepository.findByBorrowDateBetween(startDate, endDate, pageable);
        } else {
            allRecord = recordRepository.findAll(pageable);
        }
        log.info("size of the record is {}",allRecord.getSize());


        Page<RecordDTO> recordDTOPage = allRecord.map(rec -> {
            RecordDTO record = modelMapper.map(rec, RecordDTO.class);
            record.setMemberName(rec.getUser().getName());
            record.setBookTitle(rec.getBookCopy().getBook().getTitle());
            record.setIssuedBy(rec.getIssuedBy().getUserId());
            if (rec.getReturnedBy() != null)
                record.setReturnedBy(rec.getReturnedBy().getUserId());
            return record;
        });

        log.info("size of the records is {}", recordDTOPage.getContent().size());
        return recordDTOPage;
    }

    /* get all due records */
    public Page<RecordDTO> getAllDueRecord(int page) {

        // Create Pageable object with the page number and page size
        Pageable pageable = PageRequest.of(page, 5);

        // Calculate the date 30 days ago from the current date
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        // Fetch records where returnDate is null and borrowDate is 30 days ago
        Page<RecordEntity> dueRecordsPage = recordRepository
                .findByReturnDateIsNullAndBorrowDateBefore(thirtyDaysAgo, pageable);

        // Map the Page of RecordEntity to a Page of RecordDTO

        return dueRecordsPage.map(record -> {
            RecordDTO recordDTO = modelMapper.map(record, RecordDTO.class);
            recordDTO.setMemberName(record.getUser().getName());
            recordDTO.setBookTitle(record.getBookCopy().getBook().getTitle());
            recordDTO.setIssuedBy(record.getIssuedBy().getUserId());
            recordDTO.setIsbn(record.getIsbn());
            return recordDTO;
        });
    }

    /* get  all borrower of a books */
    public Page<UserDTO> getAllBorrowers(String isbn, int page) {
        Pageable pageable = PageRequest.of(page, 4);

        // Fetch records using the repository
        Page<RecordEntity> allRecords = recordRepository.findByIsbnAndReturnDateNull(isbn, pageable);

        // If no records found, return an empty Page
        if (allRecords == null || allRecords.getContent().isEmpty()) {
            return Page.empty(pageable);
        }

        // Map each RecordEntity to UserDTO
        return allRecords.map(recordEntity -> modelMapper.map(recordEntity.getUser(), UserDTO.class));
    }



}
