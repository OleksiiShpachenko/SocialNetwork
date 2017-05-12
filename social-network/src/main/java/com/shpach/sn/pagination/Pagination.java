package com.shpach.sn.pagination;

public class Pagination {
	
	private static final int PAGINATION_COUNT = 5;
	
	private int itemsOnPage;
	private int currentPage;
	private int itemsCount;
	
	public Pagination(int currentPage, int itemsCount, int itemsOnPage) {
		this.currentPage=currentPage;
		this.itemsCount=itemsCount;
		this.itemsOnPage=itemsOnPage;
	}

	public int getPaginationCount() {
		return PAGINATION_COUNT;
	}

	public int getItemsOnPage() {
		return itemsOnPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getItemsCount() {
		return itemsCount;
	}
	

}
