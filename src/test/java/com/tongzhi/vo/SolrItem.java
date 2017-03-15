package com.tongzhi.vo;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

public class SolrItem implements Serializable {

	@Field("id")
    private Long id;
	@Field("title")
    private String title;
	@Field("sellPoint")
    private String sellPoint;
	@Field("price")
    private Long price;
    @Field("image")
    private String image;
    @Field("status")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String[] getImages() {
    	if(image != null && !"".equals(image)){
    		return image.split(",");
    	}
        return null;
    }
}
