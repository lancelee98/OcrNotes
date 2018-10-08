package com.smujsj16.ocr_notes.Entity;

import java.util.Date;

public class Info {
    private  long info_id;
    private String user_id;
    private  int type;
    private Date create_time;
    private String title;
    private  long parent_id;
    private String image_link;
    private String ocr_content;
    private String image_name;

    public Info(long info_id, String user_id, int type, Date create_time, String title, long parent_id, String image_link, String ocr_content)
    {
        this.create_time=create_time;
        this.image_link=image_link;
        this.info_id=info_id;
        this.ocr_content=ocr_content;
        this.parent_id=parent_id;
        this.title=title;
        this.user_id=user_id;
        this.type=type;
    }
    public Info(String user_id,String title,String ocr_content,String image_link,String image_name)
    {
        this.image_link=image_link;
        this.ocr_content=ocr_content;
        this.title=title;
        this.user_id=user_id;
        this.image_name=image_name;
    }
    public Info()
    {

    }

    @Override
    public String toString() {
        String re= String.valueOf(info_id)+user_id+String.valueOf(type)+String.valueOf(create_time)+title+String.valueOf(parent_id)
                +image_link+ocr_content;

        return  re;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getType() {
        return type;
    }

    public long getInfo_id() {
        return info_id;
    }

    public long getParent_id() {
        return parent_id;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getOcr_content() {
        return ocr_content;
    }

    public String getTitle() {
        return title;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public void setInfo_id(long info_id) {
        this.info_id = info_id;
    }

    public void setOcr_content(String ocr_content) {
        this.ocr_content = ocr_content;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

}
