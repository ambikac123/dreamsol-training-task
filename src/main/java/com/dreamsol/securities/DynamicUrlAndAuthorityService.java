package com.dreamsol.securities;

import com.dreamsol.helpers.RoleAndPermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class DynamicUrlAndAuthorityService {
    @Autowired
    private RoleAndPermissionHelper roleAndPermissionHelper;

    public String[] getDynamicUrlPatterns() {
        Map<String, String[]> authorityNameAndUrlsMap = roleAndPermissionHelper.getAuthorityNameAndUrlsMap();
        Set<String> patternSet = new HashSet<>();
        for (Map.Entry<String, String[]> e : authorityNameAndUrlsMap.entrySet()) {
            Collections.addAll(patternSet, e.getValue());
        }
        return patternSet.toArray(new String[]{});
    }

    public String[] getDynamicAuthorities() {
        Map<String, String[]> authorityNameAndUrlsMap = roleAndPermissionHelper.getAuthorityNameAndUrlsMap();
        Set<String> authoritySet = authorityNameAndUrlsMap.keySet();
        return authoritySet.toArray(new String[]{});
    }
}

