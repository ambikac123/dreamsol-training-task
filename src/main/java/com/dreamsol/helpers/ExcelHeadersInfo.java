package com.dreamsol.helpers;

import java.util.HashMap;
import java.util.Map;

public class ExcelHeadersInfo
{
    public static Map<String,String> getDepartmentHeadersMap()
    {
        // Map(String: ColumnName, String: AttributeName)
        Map<String,String> headerName = new HashMap<>();
        headerName.put("DepartmentName","departmentName");
        headerName.put("DepartmentCode","departmentCode");
        headerName.put("Status","status");
        return headerName;
    }
    public static Map<String,String> getDepartmentHeaderTypeMap()
    {
        Map<String,String>  headerType = new HashMap<>();
        headerType.put("DepartmentName","Mandatory");
        headerType.put("DepartmentCode","Mandatory");
        headerType.put("Status","Optional");
        return headerType;
    }
    public static Map<String,String> getDepartmentDataExampleMap()
    {
        Map<String,String> examples = new HashMap<>();
        examples.put("DepartmentName","Human Resource");
        examples.put("DepartmentCode","HR");
        examples.put("Status","true/false");
        return examples;
    }
    public static Map<String,String> getUserTypeHeadersMap()
    {
        // Map(String: ColumnName, String: AttributeName)
        Map<String,String> headers = new HashMap<>();
        headers.put("UserTypeName","userTypeName");
        headers.put("UserTypeCode","userTypeCode");
        headers.put("Status","status");
        return headers;
    }
    public static Map<String,String> getUserTypeHeaderTypeMap()
    {
        Map<String,String>  headerType = new HashMap<>();
        headerType.put("UserTypeName","Mandatory");
        headerType.put("UserTypeCode","Mandatory");
        headerType.put("Status","Optional");
        return headerType;
    }
    public static Map<String,String> getUserTypeDataExampleMap()
    {
        Map<String,String> examples = new HashMap<>();
        examples.put("UserTypeName","employee");
        examples.put("UserTypeCode","emp");
        examples.put("Status","true/false");
        return examples;
    }
    public static Map<String,String> getUserHeadersMap()
    {
        // Map(String: ColumnName, String: AttributeName)
        Map<String,String> headers = new HashMap<>();
        headers.put("UserName","userName");
        headers.put("UserEmail","userEmail");
        headers.put("UserMobile","userMobile");
        headers.put("UserTypeName","userTypeName");
        headers.put("UserTypeCode","userTypeCode");
        headers.put("DepartmentName","departmentName");
        headers.put("DepartmentCode","departmentCode");
        headers.put("Status","status");
        return headers;
    }
    public static Map<String,String> getUserHeaderTypeMap()
    {
        Map<String,String>  headerType = new HashMap<>();
        headerType.put("UserName","Mandatory");
        headerType.put("UserEmail","Mandatory");
        headerType.put("UserMobile","Mandatory");
        headerType.put("UserTypeName","Optional");
        headerType.put("UserTypeCode","Optional");
        headerType.put("DepartmentName","Optional");
        headerType.put("DepartmentCode","Optional");
        headerType.put("Status","Optional");
        return headerType;
    }
    public static Map<String,String> getUserDataExampleMap()
    {
        Map<String,String> examples = new HashMap<>();
        examples.put("UserName","Mr. John");
        examples.put("UserEmail","mr.john@example.xyz");
        examples.put("UserMobile","9876543210");
        examples.put("UserTypeName","employee");
        examples.put("UserTypeCode","emp");
        examples.put("DepartmentName","Human Resource");
        examples.put("DepartmentCode","HR");
        examples.put("Status","true/false");
        return examples;
    }
}
