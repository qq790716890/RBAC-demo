package com.rbac_demo.dao;


import com.rbac_demo.entity.LoginTicket;
import org.apache.ibatis.annotations.*;


@Mapper
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    // id数据库自增长且返回给对象
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket=#{ticket}"

    })
    LoginTicket selectByTicket(String ticket);

    // 动态sql语句的写法如下
    @Update("UPDATE login_ticket SET status=#{status} WHERE ticket=#{ticket}")
    int updateStatus(String ticket, int status);


}
