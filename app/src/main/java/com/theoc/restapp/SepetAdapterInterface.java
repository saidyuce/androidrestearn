package com.theoc.restapp.helper;

import java.util.Map;

public interface SepetAdapterInterface {
    void refreshAdapter(Map<String, Integer> sepetDict, Map<String, String> sepetDictPrice, Map<String, String> sepetDictPoint, Map<String, String> sepetDictCategory);
}
