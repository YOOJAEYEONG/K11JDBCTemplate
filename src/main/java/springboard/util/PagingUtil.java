package springboard.util;

public class PagingUtil {

	public static String pagingImg(int totalRecordCount, int pageSize,
			int blockPage, int nowPage, String page) {

		String pagingStr = "";

		// 1.전체페이지 구하기
		int totalPage = (int) (Math
				.ceil(((double) totalRecordCount / pageSize)));

		/*
		 * 2.현재페이지번호를 통해 이전 페이지블럭에 해당하는 페이지를 구한다.
		 */
		int intTemp = (((nowPage - 1) / blockPage) * blockPage) + 1;

		// 3.처음페이지 바로가기 & 이전페이지블럭 바로가기
		if (intTemp != 1) {
			// 첫번째 페이지 블럭에서는 출력되지 않음
			// 두번째 페이지 블럭부터 출력됨.
			pagingStr += "" + "<a href='" + page + "nowPage=1'>"
					+ "<img src='../images/paging1.gif'></a>";
			pagingStr += "&nbsp;";
			pagingStr += "" + "<a href='" + page + "nowPage="
					+ (intTemp - blockPage) + "'>"
					+ "<img src='../images/paging2.gif'></a>";
		}

		// 페이지표시 제어를 위한 변수
		int blockCount = 1;
		/*
		 * 4.페이지를 뿌려주는 로직 : blockPage의 수만큼 또는 마지막페이지가 될때까지 페이지를 출력한다.
		 */
		while (blockCount <= blockPage && intTemp <= totalPage) {
			if (intTemp == nowPage) {
				pagingStr += "&nbsp;" + intTemp + "&nbsp;";
			} else {
				pagingStr += "&nbsp;<a href='" + page + "nowPage=" + intTemp
						+ "'>" + intTemp + "</a>&nbsp;";
			}
			intTemp++;
			blockCount++;
		}

		// 5.다음페이지블럭 & 마지막페이지 바로가기
		if (intTemp <= totalPage) {
			pagingStr += "<a href='" + page + "nowPage=" + intTemp + "'>"
					+ "<img src='../images/paging3.gif'></a>";
			pagingStr += "&nbsp;";
			pagingStr += "<a href='" + page + "nowPage=" + totalPage + "'>"
					+ "<img src='../images/paging4.gif'></a>";
		}

		return pagingStr;
	}

	/*
	 * <li class='page-item'><a href='#' class='page-link'><i class='fas
	 * fa-angle-double-left'></i></a></li> <li class='page-item'><a href='#'
	 * class='page-link'><i class='fas fa-angle-left'></i></a></li>
	 * 
	 * <li class='page-item'><a href='#' class='page-link'>1</a></li> <li
	 * class='page-item active'><a href='#' class='page-link'>2</a></li> <li
	 * class='page-item'><a href='#' class='page-link'>3</a></li> <li
	 * class='page-item'><a href='#' class='page-link'>4</a></li> <li
	 * class='page-item'><a href='#' class='page-link'>5</a></li>
	 * 
	 * <li class='page-item'><a href='#' class='page-link'><i class='fas
	 * fa-angle-right'></i></a></li> <li class='page-item'><a href='#'
	 * class='page-link'><i class='fas fa-angle-double-right'></i></a></li>
	 * 
	 * 위 디자인 적용
	 */

	public static String pagingBS4(int totalRecordCount, int pageSize,
			int blockPage, int nowPage, String pageName) {

		System.out.println("PagingUtil>pagingBS4>pageName: " + pageName);

		String pagingStr = "";

		int totalPage = (int) (Math
				.ceil(((double) totalRecordCount / pageSize)));
		System.out.println("totalPage: " + totalPage);

		int intTemp = (((nowPage - 1) / blockPage) * blockPage) + 1;
		System.out.println("intTemp: " + intTemp);

		if (intTemp != 1) {

			// 첫패이지로
			pagingStr += "" + "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=1' class='page-link'>"
					+ "<i class='fas fa-angle-double-left'></i>" + "</a>"
					+ "</li>";
			// 이전블록으로
			pagingStr += "" + "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=" + (intTemp - 1) + "' class='page-link'>"
					+ "<i class='fas fa-angle-left'></i>" + "</a>" + "</li>";
		}

		int blockCount = 1;

		while (blockCount <= blockPage && intTemp <= totalPage) {

			// 페이지 바로가기
			if (intTemp == nowPage) {
				// 현제페이지인 경우
				pagingStr += "" + "<li class='page-item active'>"
						+ "<a href='#' class='page-link'>" + intTemp + "</a>"
						+ "</li>";
			} else {
				// 현재페이지가 아닌경우
				pagingStr += "" + "<li class='page-item'>" + "<a href='"
						+ pageName + "nowPage=" + intTemp
						+ "' class='page-link'>" + intTemp + "</a>" + "</li>";
			}
			intTemp++;
			blockCount++;
		}

		if (intTemp <= totalPage) {
			// 다음페이지 블록으로
			pagingStr += "" + "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=" + intTemp + "' class='page-link'>"
					+ "<i class='fas fa-angle-right'></i>" + "</a>" + "</li>";

			// 마지막페이지로
			pagingStr += "" + "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=" + totalPage + "' class='page-link'>"
					+ "<i class='fas fa-angle-double-right'></i>" + "</a>"
					+ "</li>";
		}

		return pagingStr;
	}

	public static String pagingBS74(int totalRecordCount, int pageSize,
			int blockPage, int nowPage, String pageName) {

		String pagingStr = "";

		int totalPage = (int) (Math
				.ceil(((double) totalRecordCount / pageSize)));
		// System.out.println(totalPage);

		int intTemp = (((nowPage - 1) / blockPage) * blockPage) + 1;
		// System.out.println(intTemp);

		if (intTemp != 1) {
			// 첫페이지로
			pagingStr += "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=1' class='page-link'>"
					+ "<i class='fas fa-angle-double-left'></i>" + "</a>"
					+ "</li>";
			// 이전블록으로
			pagingStr += "<li class='page-item'>" + "		<a href='"
					+ pageName + "nowPage=" + (intTemp - 1)
					+ "' class='page-link'>"
					+ "<i class='fas fa-angle-left'></i>" + "</a>" + "</li>";
		}

		int blockCount = 1;
		while (blockCount <= blockPage && intTemp <= totalPage) {
			// 페이지 바로가기
			if (intTemp == nowPage) {
				// 현재페이지인경우
				pagingStr += "<li class='page-item active'>"
						+ "<a href='#' class='page-link'>" + intTemp + "</a>"
						+ "</li>";
			} else {
				// 현재페이지가 아닌경우
				pagingStr += "<li class='page-item'>" + "<a href='" + pageName
						+ "nowPage=" + intTemp + "' class='page-link'>"
						+ intTemp + "</a>" + "</li>";
			}
			intTemp++;
			blockCount++;
		}

		if (intTemp <= totalPage) {
			// 다음 페이지 블록으로
			pagingStr += "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=" + intTemp + "' class='page-link'>"
					+ "<i class='fas fa-angle-right'></i>" + "</a>" + "</li>";

			// 마지막 페이지로..
			pagingStr += "<li class='page-item'>" + "<a href='" + pageName
					+ "nowPage=" + totalPage + "' class='page-link'>"
					+ "<i class='fas fa-angle-double-right'></i>" + "</a>"
					+ "</li>";
		}

		return pagingStr;
	}

}
