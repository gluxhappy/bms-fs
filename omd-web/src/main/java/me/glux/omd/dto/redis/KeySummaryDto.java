package me.glux.omd.dto.redis;

public class KeySummaryDto {
    private String key;
    private int db;
    public KeySummaryDto(){}
    public KeySummaryDto(String key,int db){
        this.key=key;
        this.db=db;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public int getDb() {
        return db;
    }
    public void setDb(int db) {
        this.db = db;
    }
}
