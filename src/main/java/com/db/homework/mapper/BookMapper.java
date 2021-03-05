package com.db.homework.mapper;

import com.db.homework.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {
    //1.返回book集
    @Select("select * from book")
    @Results(id = "book",value = {
            @Result(column = "card_no",property = "cardNo"),
            @Result(column = "can_reserve",property = "canReserve"),
            @Result(column = "loan_date",property = "loanDate"),
            @Result(column = "due_date",property = "dueDate"),
            @Result(property="category",column="cid",one=@One(select="com.db.homework.mapper.CategoryMapper.getCategoryById"))
    })
    public List<Book> getList();
    //2.返回keyword搜索到的book集
    @Select("select * from book where title like #{keyword1} or author like #{keyword2}")
    @ResultMap(value = {"book"})
    List<Book> getAllByTitleLikeOrAuthorLike(@Param("keyword1")String keyword1, @Param("keyword2")String keyword2);
    //3.返回指定分类的book集
    @Select("select * from book where cid= #{id}")
    @ResultMap(value = {"book"})
    public List<Book> getAllByCid(int id);
    //4.1添加一本书到DB
    @Insert("insert into book (cover,title,author,date,press,abs,cid) values(#{cover}, #{title}, #{author}, #{date}, #{press}, #{abs}, #{cid})")
    public void add(@Param("cover")  String cover, @Param("title")String title,
                    @Param("author")String author, @Param("date")String date,
                    @Param("press")  String press, @Param("abs")String abs, @Param("cid")int cid);
    //4.2更新一本书到DB
    @Update("update book set cover=#{cover} , author=#{author} , date=#{date} , press=#{press} , abs=#{abs} , cid=#{cid} where title=#{title}")
    public void update(@Param("cover")  String cover, @Param("title")String title,
                       @Param("author")String author, @Param("date")String date,
                       @Param("press")  String press, @Param("abs")String abs, @Param("cid")int cid);
    //5.删除一本书从DB
    @Delete("delete from book where id= #{id}")
    public void deleteById(int id);
    //判断书是否在数据库中，在的话更新，不在的话添加。
    @Select("select * from book where title= #{title}")
    @ResultMap(value = {"book"})
    public Book getByTitle(String title);
    //7.按拼音大小或日期先后排序(前台同步也会排序)
    @Select("select * from book ORDER BY CONVERT(title USING gbk)")
    @ResultMap(value = {"book"})
    List<Book> getListSortedByTitle();
    @Select("select * from book ORDER BY CONVERT(author USING gbk)")
    @ResultMap(value = {"book"})
    List<Book> getListSortedByAuthor();
    @Select("select * from book ORDER BY STR_TO_DATE(concat(date,'-30'),'%Y-%m-%d')")
    @ResultMap(value = {"book"})
    List<Book> getListSortedByDate();

    @Select("select * from book ORDER BY CONVERT(press USING gbk)")
    @ResultMap(value = {"book"})
    List<Book> getListSortedByPress();
    @Select("select * from book ORDER BY cid")
    @ResultMap(value = {"book"})
    List<Book> getListSortedByCategory();
    //12
    @Select("select * from store where bid = #{bid} limit 1")
    @ResultMap(value = {"book"})
    Book getStoreInfo(int bid);
    //13
    @Insert("insert into reservation (card_no,bid,time) values(#{card_no},#{bid},#{time})")
    void addReservation(@Param("card_no") String cardNo,@Param("bid") int bid, @Param("time") String time);
    //13  12
    //不需要映射,因为只是查找存在....其实可以修改一下
    // TODO 修改
    @Select("select * from reservation where card_no = #{card_no} and bid = #{bid} limit 1")
    Book getReservation(@Param("card_no") String cardNo, @Param("bid") int bid);
    //14
    @Delete("delete from reservation where card_no = #{card_no} and bid = #{bid} limit 1")
    void deleteReservation(@Param("card_no") String cardNo,@Param("bid") int bid);
    //15
    @Select("select * from reservation where card_no = #{cardNo}")
    @Results(id = "reservation",value = {
            @Result(column = "card_no",property = "cardNo"),
    })
    List<Reservation> getReservations(String cardNo);
    //15
    @Select("select * from book where id = #{id} limit 1")
    @ResultMap(value = {"book"})
    Book getOneBookById(int id);
    //15
    @Select("select * from loan where card_no = #{cardNo}")
    @Results(id = "loan",value = {
            @Result(column = "card_no",property = "cardNo"),
            @Result(column = "loan_date",property = "loanDate"),
            @Result(column = "due_date",property = "dueDate"),
            @Result(column = "back_date",property = "backDate"),
    })
    List<Loan> getLoans(String cardNo);
    //19
    @Select("select r.* from reservation r")
    @ResultMap(value = {"reservation"})
    List<Reservation> getAllReservations();
    //20
    @Select("select r.* from reservation r where r.id = #{id} limit 1")
    @ResultMap(value = {"reservation"})
    Reservation getReservationById(int id);
    //20
    @Select("select s.* from store s where s.bid = #{bid} limit 1")
    @ResultMap(value = {"store"})
    Store getStore(int bid);
    //20
    @Update("update store set borrowed = #{num} where id = #{id}")
    void setStore(@Param("id") int id,@Param("num") int num);
    //20
    @Insert("insert into reservation_resp (status,reason,reserve_time,opt_time,card_no,bid) values(#{b},#{s},#{time},#{date},#{cardNo},#{bid})")
    void addReservationResp(@Param("b") boolean b,@Param("s") String s,@Param("time") String time,@Param("date") String date,@Param("cardNo") String cardNo,@Param("bid") int bid);
    //20
    @Insert("insert into loan (card_no,bid,loan_date,due_date) values(#{cardNo},#{bid},#{date},#{dueDate})")
    void addLoan(@Param("cardNo") String cardNo,@Param("bid") int bid, @Param("date") String date, @Param("dueDate") String dueDate);
    //12
    @Select("select * from loan where bid = #{bid} and card_no = #{cardNo} limit 1")
    @ResultMap(value = {"loan"})
    Loan getLoan(@Param("cardNo") String cardNo, @Param("bid") int bid);
    //22
    @Select("select l.* from loan l")
    @ResultMap(value = {"loan"})
    List<Loan> getAllLoans();
    //23
    @Select("select l.* from loan l where id = #{id}")
    @ResultMap(value = {"loan"})
    Loan getLoanById(int id);
    //23
    @Delete("delete from loan where id = #{id}")
    void deleteLoanById(int id);
    //23
    @Insert("insert into loan_resp (message,card_no,bid,loan_date,due_date,opt_time,back_date,fine) values(#{message},#{cardNo},#{bid},#{loanDate},#{dueDate},#{optTime},#{backDate},#{fine})")
    void addLoanResp(@Param("message") String message,@Param("cardNo") String cardNo, @Param("bid")int bid, @Param("loanDate")String loanDate,@Param("dueDate") String dueDate, @Param("optTime") String optTime,@Param("backDate")String backDate, @Param("fine")String fine);
    //23
    @Update("update loan set status = #{status} where id = #{id}")
    void setLoanStatus(@Param("id") int id,@Param("status") int status);
    //26
    @Select("select s.* from store s")
    @Results(id = "store",value = {
            @Result(column = "last_time",property = "lastTime")
    })
    List<Store> getAllStores();
    //27
    @Select("select * from store where id = #{id} limit 1")
    @ResultMap(value = {"store"})
    Store getStoreById(int id);
    //27
    @Update("update store set total = #{total}, last_time = #{lastTime} where id = #{id} limit 1")
    void setTotalStore(@Param("total") int total, @Param("lastTime") String lastTime, @Param("id") int id);
    //28
    @Update("update store set total = #{total}, borrowed = #{borrowed},last_time = #{lastTime} where id = #{id} limit 1")
    void setTotalAndBorrowed(@Param("total") int total, @Param("borrowed") int borrowed, @Param("lastTime") String lastTime, @Param("id") int id);
    //4.2
    @Insert("insert into store (bid,total,borrowed,last_time) values(#{bid},#{total},#{borrowed},#{last_time})")
    void insertStore(@Param("bid") int bid, @Param("total") int total, @Param("borrowed") int borrowed, @Param("last_time") String last_time);
    //5
    @Delete("delete from store where bid = #{bid} limit 1")
    void deleteStoreByBId(int bid);
    //50
    @Select("select * from reservation_resp where card_no = #{cardNo} order by str_to_date(opt_time,'%Y-%m-%d %H:%i:%s') desc")
    @Results(id = "reservation_resp",value = {
            @Result(column = "reserve_time",property = "reserveTime"),
            @Result(column = "opt_time",property = "optTime"),
            @Result(column = "card_no",property = "cardNo")
    })
    List<ReservationResponse> getReservationRespsByCardNo(String cardNo);
    //51
    @Select("select * from loan_resp where card_no = #{cardNo} order by str_to_date(opt_time,'%Y-%m-%d %H:%i:%s') desc")
    @Results(id = "loan_resp",value = {
            @Result(column = "card_no",property = "cardNo"),
            @Result(column = "loan_date",property = "loanDate"),
            @Result(column = "due_date",property = "dueDate"),
            @Result(column = "opt_time",property = "optTime"),
            @Result(column = "back_date",property = "backDate")
    })
    List<LoanResponse> getLoanRespsByCardNo(String cardNo);
    //56
    @Update("update loan set back_date=#{backDate},status=#{status} where card_no = #{cardNo} and bid = #{bid} limit 1")
    void updateLoan(@Param("cardNo") String cardNo, @Param("bid") int bid, @Param("backDate") String backDate, @Param("status") int status);
}
