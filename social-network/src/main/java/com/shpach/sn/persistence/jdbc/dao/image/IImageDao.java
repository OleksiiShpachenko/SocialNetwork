package com.shpach.sn.persistence.jdbc.dao.image;

import java.util.List;

import com.shpach.sn.persistence.entities.Image;

public interface IImageDao {

	public List<Image> findAll();

	public Image addOrUpdate(Image image);

	public Image findImageById(int id);

	public List<Image> findImageByPostId(int postId);

	public boolean deleteImageById(int id);
}
