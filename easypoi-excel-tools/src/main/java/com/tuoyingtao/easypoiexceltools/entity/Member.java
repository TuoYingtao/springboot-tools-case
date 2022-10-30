package com.tuoyingtao.easypoiexceltools.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 购物会员
 * @author tuoyingtao
 * @create 2022-10-28 11:08
 */
@ExcelTarget("member")
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name = "会员用户ID", width = 10, type = 10)
    private Long id;
    @Excel(name = "姓名", width = 20, needMerge = true)
    private String username;
    private String password;
    @Excel(name = "昵称", width = 20, needMerge = true)
    private String nickname;
    @Excel(name = "出生日期", width = 20, format = "yyyy-MM-dd")
    private Date birthday;
    @Excel(name = "手机号", width = 20, needMerge = true, desensitizationRule = "3_4")
    private String phone;
    private String icon;
    @Excel(name = "性别", width = 10, replace = {"男_0", "女_1"})
    private Integer gender;

    public Member() {
    }

    public Member(Long id, String username, String password, String nickname, Date birthday, String phone, String icon, Integer gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.phone = phone;
        this.icon = icon;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(username, member.username) && Objects.equals(password, member.password) && Objects.equals(nickname, member.nickname) && Objects.equals(birthday, member.birthday) && Objects.equals(phone, member.phone) && Objects.equals(icon, member.icon) && Objects.equals(gender, member.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, nickname, birthday, phone, icon, gender);
    }
}
