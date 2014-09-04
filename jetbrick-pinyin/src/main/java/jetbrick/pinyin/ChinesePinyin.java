/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 * Email: subchen@gmail.com
 * URL: http://subchen.github.io/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.pinyin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 得到汉字的完整拼音 (支持GBK)
 *
 * @author Guoqiang Chen
 */
public final class ChinesePinyin {
    private static final ChinesePinyin instance = new ChinesePinyin();
    private final Map<String, String[]> tables = new HashMap<String, String[]>(21000);

    public static ChinesePinyin getInstance() {
        return instance;
    }

    private ChinesePinyin() {
        InputStream is = getClass().getResourceAsStream("ChinesePinyin.dat");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
            String line = null;
            while (true) {
                line = reader.readLine();
                if (line == null) break;

                int delimiter = line.indexOf('=');
                String hex = line.substring(0, delimiter);
                String pinyin = line.substring(delimiter + 1);
                if (pinyin.length() > 0) {
                    tables.put(hex, pinyin.split(","));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                is.close();
            } catch(IOException e) {
            }
        }
    }

    /**
     * 得到指定的中文字符对应的拼音, 返回值带声调.
     *
     * @return null - 不可识别的字符
     */
    public String[] getPinyinFromChar(char c) {
        String s = Integer.toHexString(c).toUpperCase();
        return tables.get(s);
    }

    /**
     * 返回中文，删除不可识别的字符.
     */
    public String getChinese(String str) {
        if (str == null) return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (getPinyinFromChar(ch) != null) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 得到字符串对应的拼音(默认小写, 不带声调), 不可识别的字符原样返回.
     */
    public String getFullPinyin(String str) {
        if (str == null) return null;

        StringBuilder sb = new StringBuilder();
        String[] item = null;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            item = getPinyinFromChar(ch);
            if (item == null) {
                sb.append(ch);
            } else {
                sb.append(item[0].substring(0, item[0].length() - 1));
            }
        }
        return sb.toString();
    }

    /**
     * 得到字符串对应的拼音首字母(默认小写), 不可识别的字符原样返回.
     */
    public String getFirstPinyin(String str) {
        if (str == null) return null;

        StringBuilder sb = new StringBuilder();
        String[] item = null;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            item = getPinyinFromChar(ch);
            if (item == null) {
                sb.append(ch);
            } else {
                sb.append(item[0].substring(0, 1));
            }
        }
        return sb.toString();
    }
}
