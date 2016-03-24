/** 
**com.lulei.lucene.index.model.IndexConfig 
**/  
 /**   
 *@Description: ������������ò��� 
 */   
package com.lulei.lucene.index.model;    

import java.util.HashSet;  

public class IndexConfig {  
    //���ò���  
    private static HashSet<ConfigBean> configBean = null;  
      
    //Ĭ�ϵ�����  
    private static class LazyLoadIndexConfig {  
        private static final HashSet<ConfigBean> configBeanDefault = new HashSet<ConfigBean>();  
         static {  
             ConfigBean configBean = new ConfigBean();  
             configBeanDefault.add(configBean);  
         }  
    }  

    public static HashSet<ConfigBean> getConfigBean() {  
        //���δ��IndexConfig��ʼ������ʹ��Ĭ������  
        if (configBean == null) {  
            configBean = LazyLoadIndexConfig.configBeanDefault;  
        }  
        return configBean;  
    }  

    public static void setConfigBean(HashSet<ConfigBean> configBean) {  
        IndexConfig.configBean = configBean;  
    }  
}  

