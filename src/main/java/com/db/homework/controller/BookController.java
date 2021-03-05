package com.db.homework.controller;

import com.db.homework.entity.*;
import com.db.homework.log.OperLog;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.BookService;
import com.db.homework.service.RedisService;
import com.db.homework.utils.CastUtils;
import com.db.homework.utils.StringUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    RedisService redisService;
    //1.    7. 缓存
    @GetMapping("/books")
    @OperLog(operModul = "前台",operType = "GET",operDesc = "查询全部图书")
    public Result getTotalList() throws Exception {
        List<Book> books;
        String key = "bookLibraryList";
        Object bookCache = redisService.get(key);
        if (bookCache == null) {
            books = bookService.getTotalList();
            redisService.set(key, books);
        } else {
            books = CastUtils.objectConvertToList(bookCache, Book.class);
        }
        return ResultFactory.buildSuccessResult(books);
    }
    //2.

    @GetMapping("/search")
    @OperLog(operModul = "前台",operType = "GET",operDesc = "搜索图书")
    public Result getListByKey(@RequestParam("key") String key){
        if ("".equals(key)) {
            return ResultFactory.buildSuccessResult(bookService.getTotalList());
        } else {
            return ResultFactory.buildSuccessResult(bookService.getListByKey(key));
        }
    }
    //3.
    @GetMapping("/category/{cid}/books")
    @OperLog(operModul = "前台",operType = "GET",operDesc = "获得分类图书")
    public Result getListByCid(@PathVariable("cid")int cid) throws Exception {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(bookService.getListByCid(cid));
        } else {
            return this.getTotalList();
        }
    }
    //4.1
    //4.2 删除缓存
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "增加或修改图书")
    @PostMapping("/back/books")
    public Result addOrUpdate(@RequestBody Book book) throws Exception {
        redisService.delete("bookLibraryList");
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessResult(book);
    }
    //5.
    //6. 删除缓存
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "删除图书")
    @PostMapping("/back/books/delete")
    public Result delete(@RequestBody Book book) throws Exception {
        redisService.delete("bookLibraryList");
        bookService.deleteById(book.getId());
        return ResultFactory.buildSuccessResult(book);
    }
    //7.
    @GetMapping("/books/{sort}/sort")
    public Result setSort(@PathVariable("sort")int sort){
        bookService.setSort(sort);
       return ResultFactory.buildSuccessResult(sort);
    }
    //12.
    @Transient
    @PostMapping("/reserveSearch")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "预约搜索")
    public Result getListByReserveKey(@RequestBody User user){
            return bookService.getListByReserveKey(user);
    }
    //13
    @Transient
    @PostMapping("/books/reserve")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "预约图书")
    public Result reserveBook(@RequestBody User user) throws Exception {
        boolean exist = bookService.existReservation(user.getCardNo(),user.getBid());
        if(exist){
            return ResultFactory.buildFailResult("不能重复预约!");
        }
        bookService.addReservation(user.getCardNo(),user.getBid(),new Date());
        return ResultFactory.buildSuccessResult(user);
    }
    //14
    @Transient
    @PostMapping("/books/cancel")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "取消预约图书")
    public Result cancelBook(@RequestBody User user) throws Exception {
        boolean exist = bookService.existReservation(user.getCardNo(),user.getBid());
        if(!exist){
            return ResultFactory.buildFailResult("未预约!");
        }
        bookService.deleteReservation(user.getCardNo(),user.getBid());
        return ResultFactory.buildSuccessResult(user);
    }
    //15
    @Transient
    @PostMapping("/personBooks")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "获得个人图书")
    public Result getPersonBooks(@RequestBody User user) throws Exception {
        return ResultFactory.buildSuccessResult(bookService.getPersonBooks(user));
    }
    //16
    @Transient
    @PostMapping("/category/{cid}/personBooks")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "获得个人分类图书")
    public Result getPersonBookByCid(@PathVariable("cid")int cid,@RequestBody User user) throws Exception {
        if (0 == cid) {
            return this.getPersonBooks(user);
        }
        List<Book> books = bookService.getPersonBooks(user).stream().filter(entity-> entity.getCategory().getId() == cid).collect(Collectors.toList());
        return ResultFactory.buildSuccessResult(books);
    }
    //17
    @Transient
    @GetMapping("/category/{cid}/search")
    @OperLog(operModul = "前台",operType = "GET",operDesc = "搜索并显示分类图书")
    public Result getSearchBookByCid(@PathVariable("cid")int cid,@RequestParam("key") String key){
        if (0 == cid) {
            return this.getListByKey(key);
        }
        List<Book> bookList = null;
        if ("".equals(key)) {
            bookList = bookService.getTotalList();
        } else {
            bookList = bookService.getListByKey(key);
        }
        List<Book> books = bookList.stream().filter(entity->entity.getCategory().getId()==cid).collect(Collectors.toList());
        return ResultFactory.buildSuccessResult(books);
    }
    //19
    @GetMapping("/back/reservations")
    @OperLog(operModul = "后台-图书管理",operType = "GET",operDesc = "获得预约信息")
    public Result getReservations() throws Exception{
        String key = "reservationList";
        Object bookCache = redisService.get(key);
        List<Reservation> reservations;
        if (bookCache == null) {
            reservations = bookService.getAllReservations();
            redisService.set(key, reservations);
        } else {
            reservations = CastUtils.objectConvertToList(bookCache, Reservation.class);
        }
        return ResultFactory.buildSuccessResult(reservations);
    }
    //20
    @Transient
    @PostMapping("/back/reservations/allow")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "通过预约")
    public Result approveReservation(@RequestBody Reservation reservation) throws Exception {
        redisService.delete("reservationList");
        return bookService.approveReservation(reservation);
    }
    //21
    @Transient
    @PostMapping("/back/reservations/notAllow")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "不通过预约")
    public Result noApproveReservation(@RequestBody Reservation reservation) throws Exception {
        redisService.delete("reservationList");
        return bookService.noApproveReservation(reservation);
    }
    //22
    @GetMapping("/back/loans")
    @OperLog(operModul = "后台-图书管理",operType = "GET",operDesc = "获得借出信息")
    public Result getLoans() throws Exception{
        List<Loan> loans;
        String key = "loanList";
        Object bookCache = redisService.get(key);
        if (bookCache == null) {
            loans = bookService.getAllLoans();
            redisService.set(key, loans);
        } else {
            loans = CastUtils.objectConvertToList(bookCache, Loan.class);
        }
//
        return ResultFactory.buildSuccessResult(loans);
    }
    //23
    @Transient
    @PostMapping("/back/loans/fine")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "超期罚款")
    public Result fineLoan(@RequestBody Loan loan) throws Exception {
        redisService.delete("loanList");
        return bookService.fineLoan(loan);
    }

    //24
    @Transient
    @PostMapping("/back/loans/confirm")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "确认归还")
    public Result confirmBack(@RequestBody Loan loan) throws Exception {
        redisService.delete("loanList");
        return bookService.confirmBack(loan);
    }
    //25
    @Transient
    @PostMapping("/back/loans/remind")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "提醒归还")
    public Result remindLoan(@RequestBody Loan loan) throws Exception {
        return bookService.remindLoan(loan);
    }
    //26
    @GetMapping("/back/stores")
    @OperLog(operModul = "后台-图书管理",operType = "GET",operDesc = "获得库存信息")
    public Result getStores() throws Exception{
        List<Store> stores;
        String key = "storeList";
        Object bookCache = redisService.get(key);
        if (bookCache == null) {
            stores = bookService.getAllStores();
            redisService.set(key, stores);
        } else {
            stores = CastUtils.objectConvertToList(bookCache, Store.class);
        }
        return ResultFactory.buildSuccessResult(stores);
    }
    //27
    @PostMapping("back/stores/add")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "增加库存")
    public Result addStore(@RequestBody Store store) throws Exception{
        redisService.delete("storeList");
        return bookService.addStore(store);
    }
    //28
    @PostMapping("back/stores/sub")
    @OperLog(operModul = "后台-图书管理",operType = "POST",operDesc = "减少库存")
    public Result subStore(@RequestBody Store store) throws Exception{
        redisService.delete("storeList");
        return bookService.subStore(store);
    }
    //43
    //16
    @Transient
    @PostMapping("/category/{cid}/reserveBooks")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "获得预约的分类图书")
    public Result getReserveBookByCid(@PathVariable("cid")int cid,@RequestBody User user) throws Exception {
        if (0 == cid) {
            return ResultFactory.buildSuccessResult(bookService.getReserveBooks(user));
        }
        List<Book> books = bookService.getReserveBooks(user).stream().filter(entity-> entity.getCategory().getId() == cid).collect(Collectors.toList());
        return ResultFactory.buildSuccessResult(books);
    }


    //46 缓存
    @GetMapping("/back/library/books")
    @OperLog(operModul = "后台-图书管理",operType = "GET",operDesc = "查询图书列表")
    public Result getTotalListBack() throws Exception {
        List<Book> books;
        String key = "bookLibraryList";
        Object bookCache = redisService.get(key);
        if (bookCache == null) {
            books = bookService.getTotalList();
            redisService.set(key, books);
        } else {
            books = CastUtils.objectConvertToList(bookCache, Book.class);
        }
        return ResultFactory.buildSuccessResult(books);
    }
    //50
    @Transient
    @PostMapping("/reservationInfo")
    public Result getReservationInfo(@RequestBody User user) throws Exception {
        return ResultFactory.buildSuccessResult(bookService.getReservationInfo(user));
    }

    //51
    @Transient
    @PostMapping("/loanInfo")
    public Result getLoanInfo(@RequestBody User user) throws Exception {
        return ResultFactory.buildSuccessResult(bookService.getLoanInfo(user));
    }
    //52
    @GetMapping("/back/library/book/cache")
    public Result clearCachForBooke() throws Exception{
        redisService.delete("bookLibraryList");
        return ResultFactory.buildSuccessResult("成功");
    }

    //53
    @GetMapping("/back/library/loan/cache")
    public Result clearCacheForLoan() throws Exception{
        redisService.delete("loanList");
        return ResultFactory.buildSuccessResult("成功");
    }

    //54
    @GetMapping("/back/library/store/cache")
    public Result clearCacheForStore() throws Exception{
        redisService.delete("storeList");
        return ResultFactory.buildSuccessResult("成功");
    }

    //55
    @GetMapping("/back/library/reservation/cache")
    public Result clearCacheForReservation() throws Exception{
        redisService.delete("reservationList");
        return ResultFactory.buildSuccessResult("成功");
    }


    //56
    @Transient
    @PostMapping("/book/back")
    public Result backBook(@RequestBody User user) throws Exception {
        return ResultFactory.buildSuccessResult(bookService.backBook(user));
    }
}
