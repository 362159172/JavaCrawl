/** 
**com.lulei.lucene.index.operation.NRTIndex 
**/  
 /**   
 *@Description:  ������������࣬��ɾ�����ֲ��� 
 */   
package com.lulei.lucene.index.operation;    

import java.io.IOException;  

import org.apache.lucene.document.Document;  
import org.apache.lucene.index.Term;  
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.Query;  

import com.lulei.lucene.index.manager.IndexManager;  
    
public class NRTIndex {  
      
    private TrackingIndexWriter indexWriter;  
    private String indexName;  
      
    //ֱ��ʹ��IndexManager�е�indexWriter�����������޸Ĳ���ί�и�TrackingIndexWriterʵ��  
    public NRTIndex(String indexName){  
        this.indexName = indexName;  
        indexWriter = IndexManager.getIndexManager(indexName).getTrackingIndexWriter();  
    }  
      
    /** 
     * @param doc 
     * @return boolean 
     * @Author: lulei   
     * @Description:  ����Document������ 
     */  
    public boolean addDocument(Document doc){  
        try {  
            indexWriter.addDocument(doc);  
            return true;  
        } catch (IOException e){  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
    /** 
     * @param query 
     * @return boolean 
     * @Author: lulei   
     * @Description: ����Query������������ɾ��Document 
     */  
    public boolean deleteDocument(Query query){  
        try {  
            indexWriter.deleteDocuments(query);  
            return true;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
    /** 
     * @return 
     * @Author: lulei   
     * @Description:  ������� 
     */  
    public boolean deleteAll(){  
        try {  
            indexWriter.deleteAll();  
            return true;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
    /** 
     * @param term 
     * @param doc 
     * @return 
     * @Author: lulei   
     * @Description: ����Term�����޸�������Document 
     */  
    public boolean updateDocument(Term term, Document doc){  
        try {  
            indexWriter.updateDocument(term, doc);  
            return true;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
      
    /** 
     * @throws IOException 
     * @Author:lulei   
     * @Description: �ϲ����� 
     */  
    public void commit() throws IOException {  
        IndexManager.getIndexManager(indexName).getIndexWriter().commit();  
    }  
}  

