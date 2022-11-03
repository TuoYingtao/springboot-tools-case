package com.tuoyingtao.easypoiexceltools.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.tuoyingtao.easypoiexceltools.entity.ExcelVerifyEntity;
import com.tuoyingtao.easypoiexceltools.group.ExcelVerify;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * Excel导入验证 会员Vo
 * @author tuoyingtao
 * @create 2022-11-01 16:05
 */
public class MemberVo extends ExcelVerifyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "会员用户ID", width = 10, type = 10, needMerge = true)
    private Long id;

    @Excel(name = "姓名", width = 20, needMerge = true)
    @NotEmpty(message = "姓名不能为空", groups = {ExcelVerify.class})
    private String username;

    private String password;

    @Excel(name = "昵称", width = 20, needMerge = true)
    @NotEmpty(message = "昵称不能为空", groups = {ExcelVerify.class})
    private String nickname;

    @Excel(name = "出生日期", width = 20, format = "yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "时间格式不正确", groups = {ExcelVerify.class})
    private String birthday;

    @Excel(name = "手机号", width = 20, needMerge = true, desensitizationRule = "3_4")
    @Pattern(regexp="^1(3|4|5|7|8)\\d{9}$",message="手机号码格式错误！", groups = {ExcelVerify.class})
    private String phone;

    private String icon;

    @Excel(name = "性别", width = 10, replace = {"男_0", "女_1"})
    @Size(min = 0, max = 1, message = "性别格式不对", groups = {ExcelVerify.class})
    private String gender;

    public MemberVo() {
    }

    public MemberVo(Long id, String username, String password, String nickname, String birthday, String phone, String icon, String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.phone = phone;
        this.icon = icon;
        this.gender = gender;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("MemberVo [");
        builder.append("    id=")
                .append(id);
        builder.append(",    username=")
                .append(username);
        builder.append(",    password=")
                .append(password);
        builder.append(",    nickname=")
                .append(nickname);
        builder.append(",    birthday=")
                .append(birthday);
        builder.append(",    phone=")
                .append(phone);
        builder.append(",    icon=")
                .append(icon);
        builder.append(",    gender=")
                .append(gender);
        builder.append(']');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MemberVo memberVo = (MemberVo) o;
        return Objects.equals(id, memberVo.id) && Objects.equals(username, memberVo.username) && Objects.equals(password, memberVo.password) && Objects.equals(nickname, memberVo.nickname) && Objects.equals(birthday, memberVo.birthday) && Objects.equals(phone, memberVo.phone) && Objects.equals(icon, memberVo.icon) && Objects.equals(gender, memberVo.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username, password, nickname, birthday, phone, icon, gender);
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
