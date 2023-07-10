package com.rbac_demo.VO;

import javax.validation.constraints.NotBlank;

/**
 * @author : lzy
 * @date : 2023/7/10
 * @effect :
 */
public class LoginVO {

    @NotBlank(message = "员工账号不能为空")
    private String userName;
    @NotBlank(message = "员工密码不能为空")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
