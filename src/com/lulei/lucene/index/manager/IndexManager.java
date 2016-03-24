/** 
**com.lulei.lucene.index.manager.IndexManager 
**/  
 /**   
 *@Description: ����������    
 */   
package com.lulei.lucene.index.manager;    

import java.io.File;  
import java.io.IOException;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.concurrent.TimeUnit;  

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.index.IndexWriter;  
import org.apache.lucene.index.IndexWriterConfig;  
import org.apache.lucene.index.IndexWriterConfig.OpenMode;  
import org.apache.lucene.search.IndexSearcher;  
 
 
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;  
import org.apache.lucene.search.SearcherFactory;  
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.NIOFSDirectory;  
import org.apache.lucene.util.Version;  

import com.lulei.lucene.index.model.ConfigBean;  
import com.lulei.lucene.index.model.IndexConfig;  

public class IndexManager {  

    private IndexWriter indexWriter;  
    //���������ļ���IndexWriter  
    private TrackingIndexWriter trackingIndexWriter;  
    //�����ļ����õķִ���  
    private Analyzer analyzer;  
    //�����������  
    private NRTManager nrtManager;  
    //�����ض��߳�  
    private NRTManagerReopenThread nrtManagerReopenThread;  
    //����д������߳�  
    private IndexCommitThread indexCommitThread;  
      
    //������ַ  
    private String indexPath;  
    //�����ض������Сʱ����  
    private double indexReopenMaxStaleSec;  
    private double indexReopenMinStaleSec;  
    //����commitʱ��  
    private int indexCommitSeconds;  
    //������  
    private String IndexManagerName;  
    //commitʱ�Ƿ���������Ϣ  
    private boolean bprint = true;  
      
    /** 
     * Initialization on Demand Holderʽ��ʼ��IndexManager 
     */  
    private static class LazyLoadIndexManager {  
        private static final HashMap<String, IndexManager> indexManager = new HashMap<String, IndexManager>();  
          
        static {  
            for (ConfigBean configBean : IndexConfig.getConfigBean()) {  
                indexManager.put(configBean.getIndexName(), new IndexManager(configBean));  
            }  
        }  
    }  
      
    /**   
     *@Description: IndexManager˽�й��췽�� 
     *@Author: lulei   
     *@Version: 1.1.0   
     */  
    private IndexManager(ConfigBean configBean){  
        //�����������  
        analyzer = configBean.getAnalyzer();  
        indexPath = configBean.getIndexPath();  
        IndexManagerName = configBean.getIndexName();  
        indexReopenMaxStaleSec = configBean.getIndexReopenMaxStaleSec();  
        indexReopenMinStaleSec = configBean.getIndexReopenMinStaleSec();  
        indexCommitSeconds = configBean.getIndexCommitSeconds();  
        bprint = configBean.isBprint();  
        String indexFile = indexPath + IndexManagerName + "/";  
        //�����������  
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);  
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);  
        Directory directory = null;  
        try {  
            directory = NIOFSDirectory.open(new File(indexFile));  
            if (IndexWriter.isLocked(directory)){  
                IndexWriter.unlock(directory);  
            }  
            this.indexWriter = new IndexWriter(directory, indexWriterConfig);  
            this.trackingIndexWriter = new TrackingIndexWriter(this.indexWriter);  
            this.nrtManager = new NRTManager(this.trackingIndexWriter, new SearcherFactory());  
        } catch(IOException e){  
            e.printStackTrace();  
        }  
        //�����ػ�����  
        this.setThread();  
    }  
    /** 
     * @Author: lulei   
     * @Description: �������������߳� 
     */  
    private void setThread(){  
        this.nrtManagerReopenThread = new NRTManagerReopenThread(this.nrtManager, indexReopenMaxStaleSec, indexReopenMinStaleSec);  
        this.nrtManagerReopenThread.setName("NRTManager Reopen Thread");  
        this.nrtManagerReopenThread.setPriority(Math.min(Thread.currentThread().getPriority()+2, Thread.MAX_PRIORITY));  
        this.nrtManagerReopenThread.setDaemon(true);  
        this.nrtManagerReopenThread.start();  
          
        this.indexCommitThread = new IndexCommitThread(IndexManagerName + "Index Commit Thread");  
        this.indexCommitThread.setDaemon(true);  
        this.indexCommitThread.start();  
    }  
      
    /** 
     * @return 
     * @Author:lulei   
     * @Description: ��������commit�߳� 
     */  
    public String setCommitThread() {  
        try {  
            if (this.indexCommitThread.isAlive()){  
                return "is alive";  
            }  
            this.indexCommitThread = new IndexCommitThread(IndexManagerName + "Index Commit Thread");  
            this.indexCommitThread.setDaemon(true);  
            this.indexCommitThread.start();  
        } catch (Exception e) {  
            e.printStackTrace();  
            return "failed";  
        }  
        return "reload";  
    }  
      
    /** 
     *@Description: ����commit�߳�  
     *@Author: lulei   
     *@Version: 1.1.0 
     */  
    private class IndexCommitThread extends Thread{  
        private boolean flag;  
        public IndexCommitThread(String name){  
            super(name);  
        }  
          
        @SuppressWarnings("deprecation")  
        public void run(){  
            flag = true;  
            while(flag) {  
                try {  
                    indexWriter.commit();  
                    if (bprint) {  
                        System.out.println(new Date().toLocaleString() + "\t" + IndexManagerName + "\tcommit");  
                    }  
                    TimeUnit.SECONDS.sleep(indexCommitSeconds);  
                } catch (IOException e) {  
                    e.printStackTrace();  
                } catch (InterruptedException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    }  
      
      
    /** 
     * @return IndexManager 
     * @Author: lulei   
     * @Description: ��ȡ���������� 
     */  
    public static IndexManager getIndexManager(String indexName){  
        return LazyLoadIndexManager.indexManager.get(indexName);  
    }  
      
    /** 
     * @@Description:�ͷ�IndexSearcher��Դ 
     * @param searcher 
     */  
    public void release(IndexSearcher searcher){  
        try {  
            nrtManager.release(searcher);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block    
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * @return IndexSearcher 
     * @Author: lulei   
     * @Description: ����IndexSearcher����ʹ����֮�󣬵���release���������ͷ� 
     */  
    public IndexSearcher getIndexSearcher(){  
        try {  
            return this.nrtManager.acquire();  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
      
    public NRTManager getNRTManager(){  
        return this.nrtManager;  
    }  
      
    public IndexWriter getIndexWriter(){  
        return this.indexWriter;  
    }  
      
    public TrackingIndexWriter getTrackingIndexWriter(){  
        return this.trackingIndexWriter;  
    }  
      
    public Analyzer getAnalyzer(){  
        return analyzer;  
    }  
      
    /** 
     * @return 
     * @Author: lulei   
     * @Description: ��ȡ�����еļ�¼���� 
     */  
    public int getIndexNum(){  
        return indexWriter.numDocs();  
    }  
}  