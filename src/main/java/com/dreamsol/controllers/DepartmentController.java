package com.dreamsol.controllers;

import com.dreamsol.dto.DepartmentRequestDto;
import com.dreamsol.response.AllDataResponse;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.DepartmentService;
import com.dreamsol.dto.DepartmentResponseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department", description = "This is department API")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @Operation(
            summary = "Create new department",
            description = "This api will take name, code, status and save the data into database."
    )
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentRequestDto departmentDto) {
        return departmentService.createDepartment(departmentDto);
    }

    @Operation(
            summary = "Update existing department",
            description = "This api will replace the existing data by new one."
    )
    @PutMapping("/update/{deptId}")
    public ResponseEntity<ApiResponse> updateDepartment(@Valid @RequestBody DepartmentRequestDto departmentDto, @PathVariable("deptId") Long departmentId) {
        return departmentService.updateDepartment(departmentDto, departmentId);
    }

    @Operation(
            summary = "Delete existing department",
            description = "This api will take department id and delete the corresponding record."
    )
    @DeleteMapping("/delete/{deptId}")
    public ResponseEntity<ApiResponse> deleteDepartment(@Valid @PathVariable("deptId") Long departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @Operation(
            summary = "Get a single department and related users info.",
            description = "It is used to retrieve single data from database"
    )
    @GetMapping("/get/{deptId}")
    public ResponseEntity<DepartmentResponseDto> getSingleDepartment(@PathVariable("deptId") Long departmentId) {
        return departmentService.getSingleDepartment(departmentId);
    }

    @Operation(
            summary = "Search departments containing keywords",
            description = "It is used to search departments on the basis of department name/code containing given keyword"
    )
    @GetMapping("/search")
    public ResponseEntity<AllDataResponse> searchDepartments(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) @Min(0) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) @Min(1) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "departmentId", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords)
    {
        return departmentService.searchDepartments(pageNumber,pageSize,sortBy,sortDirection,keywords);
    }

    @Operation(
            summary = "Validate excel data and get correct and incorrect list",
            description = "It is used to upload an excel file for filtering correct and incorrect data"
    )
    @PostMapping(value = "/validate-excel-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> getCorrectAndIncorrectDepartments(@RequestParam("file") MultipartFile file) {
        return departmentService.getCorrectAndIncorrectDepartmentList(file);
    }
    @Operation(
            summary = "Save list of correct data",
            description = "This api will help to save the correct data into database"
    )
    @PostMapping(value = "/save-correct-list")
    public ResponseEntity<?> saveCorrectList(@RequestBody List<DepartmentRequestDto> correctList)
    {
        return departmentService.saveCorrectList(correctList);
    }

    @Operation(
            summary = "Download department data from database as excel",
            description = "It is used to extract department data from database , store in an excel file and return."
    )
    @GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadDepartmentDataAsExcel()
    {
        return departmentService.downloadDataFromDB();
    }

    @Operation(
            summary = "Download department auto generated dummy data as excel",
            description = "It is used to get dummy data for department"
    )
    @GetMapping(value = "/download-excel-dummy", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadDepartmentDummyDataAsExcel(@RequestParam(value = "noOfRecords", defaultValue = "10", required = false) int noOfRecords)
    {
        return departmentService.downloadDummyData(noOfRecords);
    }

    @Operation(
            summary = "Download excel sample ",
            description = "This api provides a sample for excel file to save bulk data"
    )
    @GetMapping(value = "/download-excel-sample")
    public ResponseEntity<Resource> downloadDepartmentExcelSample() {
        return departmentService.getDepartmentExcelSample();
    }
}
