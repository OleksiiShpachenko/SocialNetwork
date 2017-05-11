package com.shpak.sn.pagination;

public class PaginationServiceImpl implements IPaginationService {

	private Pagination pagination;
	
	public PaginationServiceImpl(Pagination pagination) {
		this.pagination=pagination;
	}


	public int calcStartPage() {
		 int paginationSlashCount=(int)Math.ceil((double)pagination.getCurrentPage()/pagination.getPaginationCount());
		 return pagination.getPaginationCount()*(paginationSlashCount-1)+1;
		 
	}

	public int calcStopPage() {
		return Math.min(calcMaxPage(), calcStartPage()+pagination.getPaginationCount()-1);
	}

	
	public int calcMaxPage() {
		return (int)Math.ceil((double)pagination.getItemsCount()/pagination.getItemsOnPage());
	}

	
	public boolean validatePaginationData() {
		int itemsCount=pagination.getItemsCount();
		int itemsOnPage=pagination.getItemsOnPage();
		int paginationCount=pagination.getPaginationCount();
		int currPage=pagination.getCurrentPage();
		if (itemsCount<=0||itemsOnPage<=0||paginationCount<=0||currPage<1)
			return false;
		
		return true;
	}

}
