package me.vtag.app.backend;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import ly.apps.android.rest.converters.impl.JacksonQueryParamsConverter;

/**
 * Created by nageswara on 5/21/14.
 */
public class VtagQueryParamsConverter extends JacksonQueryParamsConverter {
    @Override
    public String parseQueryParams(String path, Map<Integer, String> queryParams, Object[] args) throws UnsupportedEncodingException {
        Object[] newArgs = new Object[args.length + 1];
        System.arraycopy(args, 0, newArgs, 0, args.length);
        newArgs[args.length] = true;
        queryParams.put(args.length, "mobile");
        String fullPath = super.parseQueryParams(path, queryParams, newArgs);
        return fullPath;
    }
}
