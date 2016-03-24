/** 
**com.lulei.lucene.index.operation.NRTSearch 
**/  
/** 
 * @Description:  �����Ĳ�ѯ���� 
 */  
package com.lulei.lucene.index.operation;  

import java.util.ArrayList;  
import java.util.List;  

import org.apache.lucene.document.Document;  
import org.apache.lucene.search.IndexSearcher;  
import org.apache.lucene.search.Query;  
import org.apache.lucene.search.Sort;  
import org.apache.lucene.search.TopDocs;  

import com.lulei.lucene.index.manager.IndexManager;  
import com.lulei.lucene.index.model.SearchResultBean;  

public class NRTSearch {  
    private IndexManager indexManager;  
      
    /** 
     * @param indexName ������ 
     */  
    public NRTSearch(String indexName) {  
        indexManager = IndexManager.getIndexManager(indexName);  
    }  
      
    /** 
     * @return 
     * @Author:lulei   
     * @Description: �����еļ�¼���� 
     */  
    public int getIndexNum() {  
        return indexManager.getIndexNum();  
    }  
      
    /** 
     * @param query ��ѯ�ַ��� 
     * @param start ��ʼλ�� 
     * @param end ����λ�� 
     * @author lulei 
     * @return ��ѯ��� 
     */  
    public SearchResultBean search(Query query, int start, int end) {  
        start = start < 0 ? 0 : start;  
        end = end < 0 ? 0 : end;  
        if (indexManager == null || query == null || start >= end) {  
            return null;  
        }  
        SearchResultBean result = new SearchResultBean();  
        List<Document> datas = new ArrayList<Document>();  
        result.setDatas(datas);  
        IndexSearcher searcher = indexManager.getIndexSearcher();  
        try {  
            TopDocs docs = searcher.search(query, end);  
            result.setCount(docs.totalHits);  
            end = end > docs.totalHits ? docs.totalHits : end;  
            for (int i = start; i < end; i++) {  
                datas.add(searcher.doc(docs.scoreDocs[i].doc));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            indexManager.release(searcher);  
        }  
        return result;  
    }  
      
    /** 
     * @param query ��ѯ�ַ��� 
     * @param start ��ʼλ�� 
     * @param end ����λ�� 
     * @param sort �������� 
     * @return ��ѯ��� 
     */  
    public SearchResultBean search(Query query, int start, int end, Sort sort) {  
        start = start < 0 ? 0 : start;  
        end = end < 0 ? 0 : end;  
        if (indexManager == null || query == null || start >= end) {  
            return null;  
        }  
        SearchResultBean result = new SearchResultBean();  
        List<Document> datas = new ArrayList<Document>();  
        result.setDatas(datas);  
        IndexSearcher searcher = indexManager.getIndexSearcher();  
        try {  
            TopDocs docs = searcher.search(query, end, sort);  
            result.setCount(docs.totalHits);  
            end = end > docs.totalHits ? docs.totalHits : end;  
            for (int i = start; i < end; i++) {  
                datas.add(searcher.doc(docs.scoreDocs[i].doc));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            indexManager.release(searcher);  
        }  
        return result;  
    }  
      
    /** 
     * @param start 
     * @param count 
     * @return 
     * @Author:lulei   
     * @Description: ����ż��� 
     */  
    public SearchResultBean search(int start, int count) {  
        start = start < 0 ? 0 : start;  
        count = count < 0 ? 0 : count;  
        if (indexManager == null) {  
            return null;  
        }  
        SearchResultBean result = new SearchResultBean();  
        List<Document> datas = new ArrayList<Document>();  
        result.setDatas(datas);  
        IndexSearcher searcher = indexManager.getIndexSearcher();  
        result.setCount(count);  
        try {  
            for (int i = 0; i < count; i++) {  
                datas.add(searcher.doc((start + i) % getIndexNum()));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            indexManager.release(searcher);  
        }  
        return result;  
    }  
}  