package services.util;

import com.rbac_demo.entity.Employee;

public class MockLoginEmp {
    public static Employee staticMethod() {
        // 在这里定义你想要的返回值
        Employee emp = new Employee();
        emp.setId(1L);
        return emp;
    }
}