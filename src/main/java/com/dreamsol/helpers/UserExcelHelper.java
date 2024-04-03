package com.dreamsol.helpers;

import com.dreamsol.dto.DepartmentRequestDto;
import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserTypeRequestDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserExcelHelper 
{
	
	// To convert excel to list of users
	public static List<UserRequestDto> convertExcelToListOfUsers(InputStream inputStream)
	{
        List<UserRequestDto> usersList = new ArrayList<>();
		try
		{
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int lastCellIndex = 0;
            for (Row row : sheet)
            {
                if(row.getFirstCellNum()>0)
                    continue;
                if(row.getRowNum()==0)
                {
                    lastCellIndex = row.getLastCellNum()-1;
                    continue;
                }
                UserRequestDto user = new UserRequestDto();
                UserTypeRequestDto userType = new UserTypeRequestDto();
                DepartmentRequestDto department = new DepartmentRequestDto();
                for(int cellIndex = 0; cellIndex<=lastCellIndex; cellIndex++)
                {
                    Cell cell = row.getCell(cellIndex);
                    switch (cellIndex)
                    {
                        case 0:
								if(cell.getCellType()== CellType.STRING)
                                	user.setUserName(cell.getStringCellValue());
								else
									user.setUserName("Invalid!");
                                break;
                        case 1:
								if(cell.getCellType()==CellType.NUMERIC)
                                	user.setUserMobile((long)cell.getNumericCellValue());
								else
									user.setUserMobile(0L);
                                break;
                        case 2:
								if(cell.getCellType()==CellType.STRING)
                                	user.setUserEmail(cell.getStringCellValue());
								else
									user.setUserEmail("invalid!");
                                break;
                        case 3:
                            	if(cell.getCellType()==CellType.STRING)
                                	userType.setUserTypeName(cell.getStringCellValue());
								else
									userType.setUserTypeName("invalid!");
                                break;
                        case 4:
								if(cell.getCellType()==CellType.STRING)
                                	userType.setUserTypeCode(cell.getStringCellValue());
								else
									userType.setUserTypeCode("invalid!");
                                break;
                        case 5:
								if(cell.getCellType()==CellType.STRING)
                                	department.setDepartmentName(cell.getStringCellValue());
								else
									department.setDepartmentName("invalid!");
                                break;
                        case 6:
								if(cell.getCellType()==CellType.STRING)
                                	department.setDepartmentCode(cell.getStringCellValue());
								else
									department.setDepartmentCode("invalid!");
                                break;

                    }
                }
                user.setUserType(userType);
                user.setDepartment(department);
                usersList.add(user);
            }

		}catch(Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
        return usersList;
	}
}
