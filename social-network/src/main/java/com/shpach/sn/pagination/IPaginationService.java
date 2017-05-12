package com.shpach.sn.pagination;

public interface IPaginationService {
	
	int calcStartPage();

	int calcStopPage();

	int calcMaxPage();

	boolean validatePaginationData();
}
