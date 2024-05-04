package com.dreamsol.threads;

import com.dreamsol.entities.Department;
import com.dreamsol.repositories.DepartmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentDataSaveThread extends Thread
{
    private final DepartmentRepository departmentRepository;
    private final List<Department> departmentList;
    public DepartmentDataSaveThread(DepartmentRepository departmentRepository, List<Department> departmentList)
    {
        this.departmentRepository = departmentRepository;
        this.departmentList = departmentList;
    }
    public void run()
    {
        for(Department d : departmentList)
        {
            try {
                departmentRepository.save(d);
            }catch(Exception e)
            {
                continue;
            }
        }
    }
}
