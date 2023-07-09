package utils;

import com.rbac_demo.annotation.Logical;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.PermissionUtils;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author : lzy
 * @date : 2023/7/6
 * @effect :
 */
public class PermissionUtilsTest {

    @Test
    public void testGetAllJobTitlePermissions() {
        String expected = "JOBTITLE_READ,JOBTITLE_UPDATE,JOBTITLE_INSERT,JOBTITLE_DELETE";
        String actual = PermissionUtils.getAllJobTitlePermissions();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetAllEmployeePermissions() {
        String expected = "EMPLOYEE_READ,EMPLOYEE_UPDATE,EMPLOYEE_INSERT,EMPLOYEE_DELETE";
        String actual = PermissionUtils.getAllEmployeePermissions();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetAllDepartmentPermissions() {
        String expected = "DEPARTMENT_READ,DEPARTMENT_UPDATE,DEPARTMENT_INSERT,DEPARTMENT_DELETE";
        String actual = PermissionUtils.getAllDepartmentPermissions();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testString2Arr() {
        String permission = "perm1,perm2,perm3";
        String[] expected = {"perm1", "perm2", "perm3"};
        String[] actual = PermissionUtils.string2Arr(permission);
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testPermissionsCheck_AndLogical() {
        String[] required = {"perm1", "perm2", "perm3"};
        String[] empHas = {"perm1", "perm2"};
        boolean actual = PermissionUtils.permissionsCheck(required, empHas, Logical.AND);
        Assertions.assertFalse(actual);
    }

    @Test
    public void testPermissionsCheck_OrLogical() {
        String[] required = {"perm1", "perm2", "perm3"};
        String[] empHas = {"perm4", "perm5"};
        boolean actual = PermissionUtils.permissionsCheck(required, empHas, Logical.OR);
        Assertions.assertFalse(actual);
    }

    @Test
    public void testCheckEmpRank() {
        Employee employee1 = new Employee();
        employee1.setDepRank(2);
        employee1.setDepartmentId(2);
        employee1.setJobRank(3);

        Employee employee2 = new Employee();
        employee2.setDepRank(1);
        employee2.setDepartmentId(3);
        employee2.setJobRank(4);

        boolean actual = PermissionUtils.checkEmpRank(employee1, employee2);
        Assertions.assertFalse(actual);
    }

    @Test
    public void testCheckJobTitleRank() {
        boolean actual = PermissionUtils.checkJobTitleRank(2, 3);
        Assertions.assertTrue(actual);
    }

    @Test
    public void testCheckDepartmentRank() {
        Employee employee = new Employee();
        employee.setDepRank(2);
        Department department = new Department();
        department.setRank(3);

        boolean actual = PermissionUtils.checkDepartmentRank(employee, department);
        Assertions.assertTrue(actual);
    }

    @Test
    public void testIntComp_Less() {
        boolean actual = PermissionUtils.intComp(2, 3, ConstantUtils.CMP.LESS);
        Assertions.assertTrue(actual);
    }

    @Test
    public void testIntComp_LessEq() {
        boolean actual = PermissionUtils.intComp(2, 2, ConstantUtils.CMP.LESS_EQ);
        Assertions.assertTrue(actual);
    }

    @Test
    public void testIntComp_Eq() {
        boolean actual = PermissionUtils.intComp(2, 2, ConstantUtils.CMP.EQ);
        Assertions.assertTrue(actual);
    }

    @Test
    public void testIntComp_More() {
        boolean actual = PermissionUtils.intComp(3, 2, ConstantUtils.CMP.MORE);
        Assertions.assertTrue(actual);
    }

    @Test
    public void testIntComp_MoreEq() {
        boolean actual = PermissionUtils.intComp(3, 2, ConstantUtils.CMP.MORE_EQ);
        Assertions.assertTrue(actual);
    }
}
