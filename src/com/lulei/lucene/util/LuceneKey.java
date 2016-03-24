/** 
**com.lulei.lucene.util.LuceneKey 
**/  
 /**   
 *@Description:  �ַ�����lucene�����ַ����� 
 */   
package com.lulei.lucene.util;    
    
public class LuceneKey {  
      
    private static final String luceneKey = "+-!&|(){}[]^\"~*?:\\/";  
      
    /** 
     * @param str 
     * @param removelSpace �Ƿ��Ƴ��ո� 
     * @return 
     * @Author: lulei   
     * @Description: ���������ַ����е�lucene�����ַ� 
     */  
    public static String escapeLuceneKey(String str, boolean removelSpace){  
        if (str == null) {  
            return null;  
        }  
        StringBuffer stringBuffer = new StringBuffer();  
        for (int i = 0; i < str.length(); i++) {  
            char c = str.charAt(i);  
            if (removelSpace && c == ' '){  
                continue;  
            }  
            stringBuffer.append(escapeLuceneKey(c));  
        }  
        return stringBuffer.toString();  
    }  
      
    /** 
     * @param str 
     * @return 
     * @Author: lulei   
     * @Description: ���������ַ����е�lucene�����ַ� ���Ƴ��ո� 
     */  
    public static String escapeLuceneKey(String str) {  
        return escapeLuceneKey(str, false);  
    }  
      
    /** 
     * @param c 
     * @return 
     * @Author: lulei   
     * @Description: ת���ַ� 
     */  
    private static String escapeLuceneKey(char c){  
        if (luceneKey.indexOf(c) < 0) {  
            return c + "";  
        }  
        return "\\" + c;  
    }  

    public static void main(String[] args) {  
        // TODO Auto-generated method stub    
        System.out.println(LuceneKey.escapeLuceneKey("��   ��[�μ�+"));  
    }  

}  

