package com.einfochips.student.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationSortingUtils {

	public static final String PAGE = "0";
	public static final String SIZE = "20";

	private PaginationSortingUtils() {
	}

	public static Pageable getPageable(Integer page, Integer size) {
		return new PageRequest(page, size);
	}

}
