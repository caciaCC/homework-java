package com.db.homework.service;

import com.db.homework.entity.*;
import com.db.homework.result.Result;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface BookService {
    //1
    List<Book> getTotalList();
    //2
    List<Book> getListByKey(String key);
    //3
    List<Book> getListByCid(int cid);
    //4
    void addOrUpdate(Book book) throws ParseException;
    //5
    void deleteById(int id);
    //7
    void setSort(int sort);
    //12
    Book getStoreInfo(int bid);
    //13
    void addReservation(String cardno, int bid, Date date);
    //13
    boolean existReservation(String cardno, int bid);
    //12
    Book getReserveInfo(String cardno, int bid);
    //14
    void deleteReservation(String cardno, int bid);
    //15
    List<Book> getPersonBooks(User user);
    //19
    public List<Reservation> getAllReservations();
    //20
    Result approveReservation(Reservation reservation);
    //12
    Result getListByReserveKey(User user);
    //21
    Result noApproveReservation(Reservation reservation);
    //22
    List<Loan> getAllLoans() throws ParseException;
    //23
    Result fineLoan(Loan loan) throws ParseException;
    //24
    Result confirmBack(Loan loan) throws ParseException;
    //25
    Result remindLoan(Loan loan) throws ParseException;
    //26
    List<Store> getAllStores();
    //27
    Result addStore(Store store) throws ParseException;
    //28
    Result subStore(Store store) throws ParseException;
    //43
    List<Book> getReserveBooks(User user);
    //50
    List<ReservationResponse> getReservationInfo(User user);
    //51
    List<LoanResponse> getLoanInfo(User user);
    //56
    User backBook(User user) throws ParseException;
    //?

}
