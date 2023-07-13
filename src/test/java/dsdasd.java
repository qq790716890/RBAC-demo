import com.rbac_demo.RbacApplication;
import com.rbac_demo.dao.EmployeeMapper;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//@SpringBootTest
//@ContextConfiguration(classes = RbacApplication.class)
//@ActiveProfiles("dev")
//@Transactional()
//@Rollback(value = true)
//@ExtendWith(MockitoExtension.class)
public class dsdasd {

    @Autowired
    private EmployeeMapper employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void t(){
        List<Employee> employees = employeeService.selectByPage(10, 0, null);
        for(Employee e: employees){
            if (e.getId() == 1) continue;
            e.setPassword(passwordEncoder.encode(e.getPassword()));
            employeeService.updateOne(e);
        }
    }
}
