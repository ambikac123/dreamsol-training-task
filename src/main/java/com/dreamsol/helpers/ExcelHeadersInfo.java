package com.dreamsol.helpers;

import java.util.HashMap;
import java.util.Map;

public class ExcelHeadersInfo
{
    public static Map<String,String> getDepartmentHeadersMap()
    {
        // Map(String: ColumnName, String: AttributeName)
        Map<String,String> headerName = new HashMap<>();
        headerName.put("Name","departmentName");
        headerName.put("Code","departmentCode");
        headerName.put("Status","status");
        return headerName;
    }
    public static Map<String,String> getDepartmentHeaderTypeMap()
    {
        Map<String,String>  headerType = new HashMap<>();
        headerType.put("Name","Mandatory");
        headerType.put("Code","Mandatory");
        headerType.put("Status","Optional");
        return headerType;
    }
    public static Map<String,String> getDepartmentDataExampleMap()
    {
        Map<String,String> examples = new HashMap<>();
        examples.put("Name","Human Resource");
        examples.put("Code","HR");
        examples.put("Status","true/false");
        return examples;
    }
    public static Map<String,String> getUserTypeHeadersMap()
    {
        // Map(String: ColumnName, String: AttributeName)
        Map<String,String> headers = new HashMap<>();
        headers.put("Name","userTypeName");
        headers.put("Code","userTypeCode");
        headers.put("Status","status");
        return headers;
    }
    public static Map<String,String> getUserTypeHeaderTypeMap()
    {
        Map<String,String>  headerType = new HashMap<>();
        headerType.put("Name","Mandatory");
        headerType.put("Code","Mandatory");
        headerType.put("Status","Optional");
        return headerType;
    }
    public static Map<String,String> getUserTypeDataExampleMap()
    {
        Map<String,String> examples = new HashMap<>();
        examples.put("Name","employee");
        examples.put("Code","emp");
        examples.put("Status","true/false");
        return examples;
    }
    public static Map<String,String> getUserHeadersMap()
    {
        // Map(String: ColumnName, String: AttributeName)
        Map<String,String> headers = new HashMap<>();
        headers.put("Name","userName");
        headers.put("Email","userEmail");
        headers.put("Mobile","userMobile");
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
        headerType.put("Name","Mandatory");
        headerType.put("Email","Mandatory");
        headerType.put("Mobile","Mandatory");
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
        examples.put("Name","Mr. John");
        examples.put("Email","mr.john@example.xyz");
        examples.put("Mobile","9876543210");
        examples.put("UserTypeName","employee");
        examples.put("UserTypeCode","emp");
        examples.put("DepartmentName","Human Resource");
        examples.put("DepartmentCode","HR");
        examples.put("Status","true/false");
        return examples;
    }
}
