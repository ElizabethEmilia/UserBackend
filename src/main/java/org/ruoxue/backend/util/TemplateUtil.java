package org.ruoxue.backend.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtil {

    public static String parseTemplate(HashMap<String, String> map, String template) throws Exception{
        String patternString = "\\$\\{(" + StringUtils.join(map.keySet(), "|") + ")\\}";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(template);

        //两个方法：appendReplacement, appendTail
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, map.get(matcher.group(1)));
        }
        matcher.appendTail(sb);

        String processed = sb.toString();

        return processed;
    }

}
