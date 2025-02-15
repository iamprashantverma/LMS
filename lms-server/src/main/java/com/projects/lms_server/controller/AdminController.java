package com.projects.lms_server.controller;

import com.projects.lms_server.dto.*;
import com.projects.lms_server.entites.enums.Condition;
import com.projects.lms_server.entites.enums.Status;
import com.projects.lms_server.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    /* injecting the all the  dependencies related to Admins */
    private final UserService userService ;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final BookService bookService;
    private final BookCopyService bookCopyService;
    private final RecordService recordService;

    /* Users */

    /* get all member registration request */
    @GetMapping("/mem-reg")
    public ResponseEntity<Page<UserDTO>> getAllMemberRegistrationDetails(@RequestParam(value = "page", defaultValue = "0") int page){
        Page<UserDTO> reqs = userService.getAllMemberRegistrationDetails(page);
        return ResponseEntity.ok(reqs);
    }

    /* get single member registration request*/
    @GetMapping("/mem-reg/{id}")
    public ResponseEntity<UserDTO> getMemberRegistrationDetails(@PathVariable String id){
        UserDTO reqs = userService.getMemberRegistrationDetails(id);
        return ResponseEntity.ok(reqs);
    }

    /* Approve Member Registration  Request*/
    @PostMapping("/approve-reg-req/{regId}")
    public ResponseEntity<UserDTO> approvedMemberRegistrationRequest(@PathVariable String regId ){
            UserDTO member = userService.approveMemberRegistrationRequest(regId);
            return ResponseEntity.ok(member);
    }

    /* Delete Member Registration  Request*/
    @DeleteMapping("/reject-reg-req/{regId}")
    public ResponseEntity<UserDTO> rejectMemberRegistrationRequest(@PathVariable String regId) {
        UserDTO deletedUser = userService.rejectMemberRegistrationRequest(regId);
        return ResponseEntity.ok(deletedUser);
    }

    /* Members */

    /* Deactivating the Member */
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<UserDTO> deactivatingTheMember(@PathVariable String id) {
        UserDTO deactivatedUser =  userService.deactivatingMember(id) ;
        return ResponseEntity.ok(deactivatedUser);
    }

    /*activating the Member */
    @PutMapping("/activate/{id}")
    public ResponseEntity<UserDTO> activatingTheMember(@PathVariable String id) {
        UserDTO deactivatedUser =  userService.activatingMember(id) ;
        return ResponseEntity.ok(deactivatedUser);
    }

    /* Get single member Details */
    @GetMapping("/member/{id}")
    public ResponseEntity<UserDTO> getMemberDetails(@PathVariable String id) {
        UserDTO member =  userService.getMemberDetails(id) ;
        return ResponseEntity.ok(member);
    }

    /* get the all member details */
    @GetMapping("/member")
    public ResponseEntity<Page<UserDTO>> getAllMemberDetails(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<UserDTO> allMembers = userService.getAllMembers(page) ;
        return ResponseEntity.ok(allMembers);
    }

    /*  Authors */

    /* adding new Author into the */
    @PostMapping("/author")
    public ResponseEntity<AuthorDTO> addNewAuthor(@Valid @RequestBody AuthorDTO authorDTO ) {
        AuthorDTO savedAuthor = authorService.addNewAuthor(authorDTO);
        return ResponseEntity.ok(savedAuthor);
    }

    /* find the details of one author */
    @GetMapping("/author/{id}")
    public ResponseEntity<AuthorDTO> getAuthorDetails(@PathVariable String id ) {
        AuthorDTO author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    /* fetching the all authors*/
    @GetMapping("/author")
    public ResponseEntity<Page<AuthorDTO>> getAuthorDetails(@RequestParam(defaultValue = "0",required = false) int page ) {
        Page<AuthorDTO> allAuthors = authorService.getAllAuthorDetails(page);
        return ResponseEntity.ok(allAuthors);
    }

    /* Publisher */

    /* adding the new publisher  */
    @PostMapping("publisher")
    public ResponseEntity<PublisherDTO> addNewPublisher(@Valid @RequestBody PublisherDTO publisherDTO) {
        PublisherDTO savedPublisher =  publisherService.addNewPublisher(publisherDTO);
        return ResponseEntity.ok(savedPublisher);
    }

    /* fetching the new publisher by their id */
    @GetMapping("/publisher/{id}")
    public ResponseEntity<PublisherDTO> getPublisherDetails(@PathVariable String id) {
        PublisherDTO publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisher);
    }

    /* fetching the all publisher*/
    @GetMapping("/publisher")
    public ResponseEntity<Page<PublisherDTO>> getAllPublisherDetails(@RequestParam(defaultValue = "0",required = false)int page) {
        Page<PublisherDTO> publisher = publisherService.getAllPublisher(page);
        return ResponseEntity.ok(publisher);
    }

    /* Book */


    /* add new Book */
    @PostMapping("/book")
    public ResponseEntity<BookDTO> addNewBook( @Valid @RequestPart("bookData") BookDTO bookDTO, @RequestParam("image")MultipartFile file) throws IOException {
        BookDTO  savedBook = bookService.addNewBook(bookDTO,bookDTO.getCount(), file);
        return ResponseEntity.ok(savedBook);
    }

    /* get Damaged book Records in the library */
    @GetMapping("/discard-book")
    public ResponseEntity<Page<RecordDTO>> getAllDamagedBooks(@RequestParam(required = false,defaultValue = "0")int page) {
            Page<RecordDTO> bks = bookCopyService.getAllDamagedBooks(page);
            return ResponseEntity.ok(bks);
    }

    /* get single book details by isbn*/
    @GetMapping("/book/{isbn}")
    public ResponseEntity<BookDTO> getSingleBookDetails(@PathVariable String isbn) {
        BookDTO book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    /* get All books in library */
    @GetMapping("/book")
    public  ResponseEntity<Page<BookDTO>> getAllBooksByIsbn( @RequestParam(required = false ,defaultValue = "0") int page) {
        Page<BookDTO> allBks = bookService.getAllBooksInPage(page);
        return  ResponseEntity.ok(allBks);
    }

    /* returning the book */
    @PutMapping("/return-book/{bookId}/{condition}")
    public ResponseEntity<RecordDTO> returnBook(@PathVariable String bookId, @PathVariable Condition condition){
        RecordDTO bookCopyDTO = bookCopyService.returnBook(bookId,condition);
        return ResponseEntity.ok(bookCopyDTO);
    }

    /* approve or reject the Member Book request */
    @DeleteMapping("/approveBook/{id}/{status}")
    public ResponseEntity<RecordDTO> approveReservedBook(@PathVariable String id, @PathVariable Status status) {
        RecordDTO recordDTO = bookCopyService.approveReservedBook(id,status);
        return ResponseEntity.ok(recordDTO);
    }

    /* get member borrowed books*/
    @GetMapping("/mem-borrow-rec/{id}")
    public ResponseEntity<List<RecordDTO>> getMemberBorrowedBooks(@PathVariable String id){
        List<RecordDTO> records  = recordService.getMemberBorrowedBooks(id);
        return ResponseEntity.ok(records);

    }

    /* get all borrowed books in library */
    @GetMapping("/borrow-btw")
    public ResponseEntity<Page<RecordDTO>> getAllBorrowedRecordBetweenTheDate(@RequestParam(value = "startDate", required = false) LocalDate startDate, @RequestParam(value = "endDate", required = false) LocalDate endDate,@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<RecordDTO> records = recordService.getAllBorrowedBooks(startDate,endDate,page);
        return ResponseEntity.ok(records);
    }

    /* fetch the record between the dates or all */
    @GetMapping("/record-btw")
    public ResponseEntity< Page<RecordDTO>> getAllRecord(@RequestParam(value = "startDate", required = false) LocalDate startDate, @RequestParam(value = "endDate", required = false) LocalDate endDate,@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<RecordDTO> records = recordService.getAllRecord(startDate,endDate,page);
        return ResponseEntity.ok(records);
    }

    /* get the all due records */
    @GetMapping("/due-record")
    public ResponseEntity< Page<RecordDTO>> getAllDueRecord(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<RecordDTO> records = recordService.getAllDueRecord(page);
        return ResponseEntity.ok(records);
    }

    /* get the All Member Reserved Book Request*/
    @GetMapping("/reserve")
    public ResponseEntity<Page<ReservedBookDTO>> getAllReservedBooks(@RequestParam(required = false,defaultValue = "0")int page) {
        Page<ReservedBookDTO> allBooks = bookCopyService.getAllReservedBooks(page);
        return ResponseEntity.ok(allBooks);
    }

    /* get all Reserved book of a member */
    @GetMapping("/reserve/{id}")
    public ResponseEntity<List<ReservedBookDTO>> getReservedBook(@PathVariable String id) {
        List<ReservedBookDTO> reservedBooks = bookCopyService.getAllReservedBooks(id);
        return ResponseEntity.ok(reservedBooks);
    }

    /* get borrowers of  a book who have not return till now */
    @GetMapping("/borrower/{isbn}")
    public ResponseEntity<Page<UserDTO>> getBorrowerOfBook(@PathVariable String isbn, @RequestParam(required = false , defaultValue = "0") int page) {
            Page<UserDTO> borrowers = recordService.getAllBorrowers(isbn,page);
            return  ResponseEntity.ok(borrowers);
    }

    /* get all the approved Member By the Admin */
    @GetMapping("/approved-members/{id}")
    public ResponseEntity<Page<UserDTO>> getAllApprovedMember(@PathVariable String id ,@RequestParam(defaultValue = "0",required = false) int page) {
        Page<UserDTO> resp = userService.getAllApprovedMembers(id,page);
        return ResponseEntity.ok(resp);
    }

    /* get access token for the admin */
    @GetMapping("/accessToken")
    public ResponseEntity<LoginResDTO> getAccessToken(){
         LoginResDTO login = userService.getAccessToken();
         return ResponseEntity.ok(login);
    }

//     i m doing changes hers
//     doing chnage from the temp again nanu

}

