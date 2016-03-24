/** 
**com.lulei.lucene.index.model.ConfigBean 
**/  
/**   
 *@Description:  ���������������� 
 */  
package com.lulei.lucene.index.model;  

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.standard.StandardAnalyzer;  
import org.apache.lucene.util.Version;  

public class ConfigBean {  
    // �ִ���  
    private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);  
    // ������ַ  
    private String indexPath = "/index/";  
    private double indexReopenMaxStaleSec = 10;  
    private double indexReopenMinStaleSec = 0.025;  
    // ����commitʱ��  
    private int indexCommitSeconds = 60;  
    // ��������  
    private String indexName = "index";  
    //commitʱ�Ƿ���������Ϣ  
    private boolean bprint = true;  
      
    public Analyzer getAnalyzer() {  
        return analyzer;  
    }  
    public void setAnalyzer(Analyzer analyzer) {  
        this.analyzer = analyzer;  
    }  
    public String getIndexPath() {  
        return indexPath;  
    }  
    public void setIndexPath(String indexPath) {  
        if (!(indexPath.endsWith("\\") || indexPath.endsWith("/"))) {  
            indexPath += "/";  
        }  
        this.indexPath = indexPath;  
    }  
    public double getIndexReopenMaxStaleSec() {  
        return indexReopenMaxStaleSec;  
    }  
    public void setIndexReopenMaxStaleSec(double indexReopenMaxStaleSec) {  
        this.indexReopenMaxStaleSec = indexReopenMaxStaleSec;  
    }  
    public double getIndexReopenMinStaleSec() {  
        return indexReopenMinStaleSec;  
    }  
    public void setIndexReopenMinStaleSec(double indexReopenMinStaleSec) {  
        this.indexReopenMinStaleSec = indexReopenMinStaleSec;  
    }  
    public int getIndexCommitSeconds() {  
        return indexCommitSeconds;  
    }  
    public void setIndexCommitSeconds(int indexCommitSeconds) {  
        this.indexCommitSeconds = indexCommitSeconds;  
    }  
    public String getIndexName() {  
        return indexName;  
    }  
    public void setIndexName(String indexName) {  
        this.indexName = indexName;  
    }  
    public boolean isBprint() {  
        return bprint;  
    }  
    public void setBprint(boolean bprint) {  
        this.bprint = bprint;  
    }  
}  