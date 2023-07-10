package com.rbac_demo.VO;

import org.hibernate.validator.constraints.Range;

/**
 * @author : lzy
 * @date : 2023/7/10
 * @effect :
 */
public class StatusVO {
    @Range(min = 0,max = 1)
    Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
