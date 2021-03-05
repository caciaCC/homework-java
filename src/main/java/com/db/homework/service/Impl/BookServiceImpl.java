package com.db.homework.service.Impl;

import com.db.homework.entity.*;
import com.db.homework.mapper.BookMapper;
import com.db.homework.mapper.StudentMapper;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.BookService;
import com.db.homework.service.RedisService;
import com.db.homework.utils.CastUtils;
import com.db.homework.utils.StringAndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    RedisService redisService;
    private int sort;

    public BookServiceImpl() {
        this.sort = 0;
    }

    //1     7
    @Override
    public List<Book> getTotalList() {
        return bookMapper.getList();
//        return bookMapper.getList();
//        if(sort==0)
//        return bookMapper.getList();
//        if(sort==1) return bookMapper.getListSortedByTitle();
//        else if(sort==2) return bookMapper.getListSortedByAuthor();
//        else if(sort==3) return bookMapper.getListSortedByDate();
//        else if(sort==4) return bookMapper.getListSortedByPress();
//        else if(sort==5) return bookMapper.getListSortedByCategory();
//        return null;
    }
    //2
    @Override
    public List<Book> getListByCid(int cid) {
        return bookMapper.getAllByCid(cid);
    }
    //3
    @Override
    public List<Book> getListByKey(String key) {
        return bookMapper.getAllByTitleLikeOrAuthorLike('%'+key +'%', '%'+key+'%');
    }
    //4
    @Override
    public void addOrUpdate(Book book) throws ParseException {
        //4.1
        if(bookMapper.getByTitle(book.getTitle())!=null) bookMapper.update(book.getCover(),book.getTitle(),book.getAuthor(),book.getDate(),book.getPress(),book.getAbs(),book.getCategory().getId());
        else {
            //4.2
            System.out.println(book.getId());
            bookMapper.add(book.getCover(),book.getTitle(),book.getAuthor(),book.getDate(),book.getPress(),book.getAbs(),book.getCategory().getId());
            Book bookInDB = bookMapper.getByTitle(book.getTitle());
            bookMapper.insertStore(bookInDB.getId(),0,0,StringAndDate.getDetailedString(new Date()));
        }
    }
    //5
    @Override
    public void deleteById(int id) {
        bookMapper.deleteById(id);
        bookMapper.deleteStoreByBId(id);
    }
    //7
    public void setSort(int sort){
        this.sort = sort;
    }
    //12
    @Override
    public Book getStoreInfo(int bid) {
        return bookMapper.getStoreInfo(bid);
    }
    //13
    @Override
    public void addReservation(String cardno, int bid, Date date) {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        String time = sdf.format(date);
        bookMapper.addReservation(cardno,bid,time);
    }

    @Override
    public boolean existReservation(String cardno, int bid) {
        return bookMapper.getReservation(cardno,bid) != null;
    }
    //12
    @Override
    public Book getReserveInfo(String cardno, int bid) {
        return bookMapper.getReservation(cardno,bid);
    }
    //14
    @Override
    public void deleteReservation(String cardno, int bid) {
        bookMapper.deleteReservation(cardno,bid);
    }
    //15
    @Override
    public List<Book> getPersonBooks(User user) {
        List<Loan> loanList = bookMapper.getLoans(user.getCardNo());
        List<Loan> loans = loanList.stream().filter(loan -> loan.getStatus() == 0).collect(Collectors.toList());
        List<Book> loanBooks = loans.stream().map(entity -> {
            Book book = bookMapper.getOneBookById(entity.getBid());
            book.setLoanDate(entity.getLoanDate());
            book.setDueDate(entity.getDueDate());
            book.setStatus(2);
            return book;
        }).collect(Collectors.toList());

        return loanBooks;
    }
    //19
    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = bookMapper.getAllReservations();
        for(Reservation reservation: reservations){
            Book book =  bookMapper.getStoreInfo(reservation.getBid());
            reservation.setTotal(book.getTotal());
            reservation.setLeft(book.getTotal()-book.getBorrowed());
        }
        return reservations;
    }
    //20
    @Override
    //TODO  有没有并发风险
    public Result approveReservation(Reservation reservation) {
        Reservation reservationInDB = bookMapper.getReservationById(reservation.getId());
        Store storeInDB = bookMapper.getStore(reservationInDB.getBid());
        if(storeInDB.getBorrowed()>=storeInDB.getTotal()) return ResultFactory.buildFailResult("馆存不足!");
//        ReservationResponse reservationResponse = new ReservationResponse();
//        reservationResponse.setBid(reservationInDB.getBid());
//        reservationResponse.setCardNo(reservationInDB.getCardNo());
        bookMapper.deleteReservation(reservationInDB.getCardNo(),reservationInDB.getBid());
        bookMapper.setStore(storeInDB.getId(),storeInDB.getBorrowed()+1);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date now = new Date();
        String date = sdf.format(now);
        Calendar cd = Calendar.getInstance();
        cd.setTime(now);
        cd.add(Calendar.MONTH,1);
        Date last =  cd.getTime();
        String dueDate = sdf.format(last);
//        reservationResponse.setOptTime(date);
//        reservationResponse.setReason("");
//        reservationResponse.setReserveTime(reservationInDB.getTime());
//        reservationResponse.setStatus(true);
        bookMapper.addReservationResp(true,"",reservationInDB.getTime(),date,
                reservationInDB.getCardNo(),reservationInDB.getBid());
        bookMapper.addLoan(reservationInDB.getCardNo(),reservationInDB.getBid(),date,dueDate);
        return ResultFactory.buildSuccessResult(reservation);
    }
    //12
    @Override
    public Result getListByReserveKey(User user) {
        List<Book> books = null;
        if ("".equals(user.getKey())) books = this.getTotalList();
        else books = this.getListByKey(user.getKey());
        List<Book> bookList = books.stream().filter(book -> bookMapper.getLoan(user.getCardNo(), book.getId()) == null).collect(Collectors.toList());
        if(bookList.size()==0) return ResultFactory.buildFailResult("搜索无结果!");
        bookList.forEach(e->{
            int bid = e.getId();
            Book bookstore = this.getStoreInfo(bid);
            e.setTotal(bookstore.getTotal());
            e.setBorrowed(bookstore.getBorrowed());
            Book reserve = this.getReserveInfo(user.getCardNo(),bid);
            e.setCanReserve(reserve==null);
        });
        return ResultFactory.buildSuccessResult(bookList);
    }
    //21
    @Override
    public Result noApproveReservation(Reservation reservation) {
        Reservation reservationInDB = bookMapper.getReservationById(reservation.getId());
        bookMapper.deleteReservation(reservationInDB.getCardNo(),reservationInDB.getBid());
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date now = new Date();
        String date = sdf.format(now);
        bookMapper.addReservationResp(false,reservation.getReason(),reservationInDB.getTime(),date,
                reservationInDB.getCardNo(),reservationInDB.getBid());
        return ResultFactory.buildSuccessResult(reservation);
    }
    //22
    @Override
    public List<Loan> getAllLoans() throws ParseException {
        List<Loan> loans = bookMapper.getAllLoans();
        for(Loan loan: loans){
            loan.setDueDate(StringAndDate.getSimpleStringFromDetailedString(loan.getDueDate()));
            loan.setLoanDate(StringAndDate.getSimpleStringFromDetailedString(loan.getLoanDate()));
            String dueTime = loan.getDueDate();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
            Date dueDate = sdf.parse(dueTime);
            if(loan.getBackDate()==null){
                loan.setBackDate("还未归还");
                loan.setOverDue(dueDate.after(new Date())?"否":"是");
            }
            else {
                loan.setBackDate(StringAndDate.getSimpleStringFromDetailedString(loan.getBackDate()));
                loan.setOverDue(dueDate.after(StringAndDate.getSimpleDate(loan.getBackDate()))?"否":"是");
            }



            Book book = bookMapper.getOneBookById(loan.getBid());
            loan.setCategory(book.getCategory());
            if(loan.getOverDue().equals("是") && !loan.getBackDate().equals("还未归还")){
                loan.setOverDate(StringAndDate.getOverTime(loan.getBackDate(),loan.getDueDate())+"天");
//                System.out.println("设置还未归还时间");
            }
            if(loan.getOverDue().equals("是") && loan.getBackDate().equals("还未归还")){
//                System.out.println("设置还未归还时间和学生电话");
                loan.setNowOverDue(StringAndDate.getOverTime(StringAndDate.getSimpleString(new Date()),loan.getDueDate())+"天");
                Student student = studentMapper.getByCardNo(loan.getCardNo());
                loan.setPhone(student.getPhone());
            }
        }
        return loans;
    }
    //23
    @Override
    public Result fineLoan(Loan loan) throws ParseException {
        Loan loanInDB = bookMapper.getLoanById(loan.getId());
        String backTime = loanInDB.getBackDate();
        String dueTime = loanInDB.getDueDate();
        if(backTime==null) return ResultFactory.buildFailResult("还未归还,无法罚款!");
        Date backDate = StringAndDate.getSimpleDate(backTime);
        Date dueDate = StringAndDate.getSimpleDate(dueTime);
        if(!backDate.after(dueDate)){
            return ResultFactory.buildFailResult("归还时间未超出期限!");
        }
//        bookMapper.deleteLoanById(loan.getId());
        String optTime = StringAndDate.getDetailedString(new Date());
        bookMapper.addLoanResp("处理超期",loanInDB.getCardNo(),loanInDB.getBid(),loanInDB.getLoanDate(),loanInDB.getDueDate(),optTime,loanInDB.getBackDate(),loan.getFine());
        bookMapper.setLoanStatus(loan.getId(),2);
        return ResultFactory.buildSuccessResult(loan);
    }
    //24
    @Override
    public Result confirmBack(Loan loan) throws ParseException {
        Loan loanInDB = bookMapper.getLoanById(loan.getId());
        //删除
        bookMapper.deleteLoanById(loanInDB.getId());
        //添加到loan_resp
        String optTime = StringAndDate.getDetailedString(new Date());
        bookMapper.addLoanResp("确认归还",loanInDB.getCardNo(),loanInDB.getBid(),loanInDB.getLoanDate()
        ,loanInDB.getDueDate(),optTime,loanInDB.getBackDate(),"");
        Store store = bookMapper.getStore(loanInDB.getBid());
        if(store.getBorrowed()<=0){
            return ResultFactory.buildFailResult("错误,超出总量!");
        }
        bookMapper.setStore(store.getId(),store.getBorrowed()-1);
        return ResultFactory.buildSuccessResult(loan);
    }
    // 25(未实现)
    // TODO
    // 应该也要加入loanResponse
    @Override
    public Result remindLoan(Loan loan) throws ParseException {
        if(loan.getStyle()==1) {
            System.out.println("短信提醒");
        }
        else if(loan.getStyle()==2){
            System.out.println("电话提醒");
        }
        else if(loan.getStyle()==3){
            Loan loanInDB = bookMapper.getLoanById(loan.getId());
            bookMapper.addLoanResp("提醒归还",loanInDB.getCardNo(),loanInDB.getBid(),loanInDB.getLoanDate(),loanInDB.getDueDate(),StringAndDate.getDetailedString(new Date()),loanInDB.getBackDate(),loanInDB.getFine());
        }
        return ResultFactory.buildSuccessResult("提醒成功");
    }
    //26
    @Override
    public List<Store> getAllStores() {
        List<Store> stores = bookMapper.getAllStores();
        for(Store store: stores){
            store.setLeft(store.getTotal()-store.getBorrowed());
            Book book = bookMapper.getOneBookById(store.getBid());
            String name = book.getTitle();
            if(name!=null) store.setName(name);
        }
        return stores;
    }
   //27
    @Override
    public Result addStore(Store store) throws ParseException {
        Store storeInDB = bookMapper.getStoreById(store.getId());
        bookMapper.setTotalStore(storeInDB.getTotal()+store.getChange(),StringAndDate.getDetailedString(new Date()),store.getId());
        return ResultFactory.buildSuccessResult(store);
    }
  //28
    @Override
    public Result subStore(Store store) throws ParseException {
        Store storeInDB = bookMapper.getStoreById(store.getId());
        if(storeInDB.getBorrowed()-store.getBorrowedChange()<0){
            return ResultFactory.buildFailResult("借出量小于0!");
        }
        if(storeInDB.getTotal()-store.getChange()-store.getBorrowedChange()<0){
            return ResultFactory.buildFailResult("馆存总量小于0!");
        }
        if(storeInDB.getTotal()-store.getChange()-store.getBorrowedChange()<storeInDB.getBorrowed()-store.getBorrowedChange()){
            return ResultFactory.buildFailResult("馆存总量小于借出数目!");
        }
        bookMapper.setTotalAndBorrowed(storeInDB.getTotal()-store.getChange()-store.getBorrowedChange(),storeInDB.getBorrowed()-store.getBorrowedChange(),StringAndDate.getDetailedString(new Date()),store.getId());
        return ResultFactory.buildSuccessResult(store);
    }
    //43
    @Override
    public List<Book> getReserveBooks(User user) {
        List<Reservation> reservetionList = bookMapper.getReservations(user.getCardNo());
        List<Book> reserveBooks = reservetionList.stream().map(entity -> {
            Book book = bookMapper.getOneBookById(entity.getBid());
            book.setTime(entity.getTime());
            book.setStatus(1);
            return book;
        }).collect(Collectors.toList());
        return reserveBooks;
    }
    //50
    @Override
    public List<ReservationResponse> getReservationInfo(User user) {
        String cardNo = user.getCardNo();
        List<ReservationResponse> reservationResponses = bookMapper.getReservationRespsByCardNo(cardNo);
        for(ReservationResponse resp: reservationResponses){
            if(resp.isStatus()) {
                resp.setOk("成功");
                resp.setReason("无");
            }else {
                resp.setOk("失败");
            }
            Book book = bookMapper.getOneBookById(resp.getBid());
            resp.setBook(book);
        }
        return reservationResponses;
    }
    //51
    @Override
    public List<LoanResponse> getLoanInfo(User user) {
        String cardNo = user.getCardNo();
        List<LoanResponse> loanResponses = bookMapper.getLoanRespsByCardNo(cardNo);
        for(LoanResponse resp: loanResponses){
            if(resp.getMessage().equals("确认归还")) {
                resp.setOk("归还成功");
                resp.setFine("无");
            }else if (resp.getMessage().equals("处理超期")){
                resp.setOk("超期罚款");
            }else if(resp.getMessage().equals("提醒归还")){
                resp.setOk("提醒归还");
                resp.setFine("无");
                resp.setBackDate("还未归还");
            }
            Book book = bookMapper.getOneBookById(resp.getBid());
            resp.setBook(book);
        }
        return loanResponses;
    }
    //56
    @Override
    public User backBook(User user) throws ParseException {
        String cardNo = user.getCardNo();
        int bid = user.getBid();
        Loan loanInDB = bookMapper.getLoan(cardNo,bid);
        Date dueDate = StringAndDate.getSimpleDate(loanInDB.getDueDate());
        Date nowDate = StringAndDate.getSimpleDateFromDetailedDate(new Date());
        if(nowDate.after(dueDate)){
            bookMapper.updateLoan(cardNo,bid,StringAndDate.getDetailedString(new Date()),1);
        }else {
            bookMapper.updateLoan(cardNo,bid,StringAndDate.getDetailedString(new Date()),2);
        }
        return user;
    }
}
