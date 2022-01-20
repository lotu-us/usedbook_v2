package team.hello.usedbook.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import team.hello.usedbook.domain.enums.Category;

@Getter
@ToString
public class Pagination {
    //setter있어야 queryString값 받아올 수 있음!!

    //게시글
    private int postOffset = 0;     //몇번째 행부터 출력할지
    private int postLimit = 5;      //출력할 행의 개수

    //페이징
    private int page = 1;               //현재페이지
    private int startPage;                  //출력할 페이지 시작 번호
    private int endPage;                    //출력할 페이지 마지막 번호
    private int limitPage = 5;              //출력할 페이지 번호의 개수
    private boolean prevPage = true;        //이전페이지 여부
    private boolean nextPage = true;        //다음페이지 여부

    //정렬. 항상 createtime이 정렬된 상태이기위해 otext가 createtime인경우엔 ctype을 변경한다 (2단계정렬)
    @Setter
    private String otext = "";                   //정렬컬럼
    @Setter
    private String otype = "";                   //정렬타입
    private String ctype = "desc";          //createtime 컬럼 type

    //검색
    private Category category;
    @Setter
    private String stext;      //검색문자
    @Setter
    private String srange;     //검색범위


    public void init(int categoryAndSearchCount){
        setPostOffset();
        setEndPage(categoryAndSearchCount);
        setStartEndPrevNextPage();
        setOrder();
    }

    //타임리프 편의메서드
    public String getCategoryKor() {
        if(category == null){
            return "통합검색";
        }
        return category.getValue();
    }

    //초기화 setter
    private void setPostOffset() {
        /*
        현재 페이지가 1일때는 0(1 -1)*5번째부터
        현재 페이지가 2일때는 5(2 -1)*5번째부터
        * */
        postOffset = (page -1)*limitPage;
    }

    private void setEndPage(int categoryAndSearchCount) {
        // 전체 페이지 개수 = 전체게시글수 / 출력할 행의 개수
        // 7 / 5 = 1 ...2
        // 5 / 5 = 1
        // 3 / 5 = 0 ...3 만약 나머지가 있다면 +1 처리
        endPage = categoryAndSearchCount / postLimit;
        if(categoryAndSearchCount % postLimit != 0){
            endPage = endPage + 1;
        }

        pageGreaterThanEndPage();
    }

    private void setStartEndPrevNextPage(){
        /*
         현재 페이지가 3이면 시작번호 1 ~ 끝번호 5
         현재 페이지가 5이면 시작번호 1 ~ 끝번호 5
         현재 페이지가 7이면 시작번호 6 ~ 끝번호 10
         현재 페이지가 10이면 시작번호 6 ~ 끝번호 10
         현재 페이지가 18이면 시작번호 16 ~ 끝번호 20

         출력할 페이지 시작 번호 = (현재페이지 / 페이지 번호의 개수) * 페이지번호의 개수 + 1
                                 현재 페이지가 limitPage값의 배수라면 (현재페이지 - (페이지 번호의 개수 -1))
         출력할 페이지 마지막 번호 = 시작번호 + (페이지 번호의 개수 -1)
        * */

        if(page % limitPage == 0){
            startPage = page - (limitPage -1);
        }else{
            startPage = (page / limitPage) * limitPage + 1;
        }

        /*
        endpage가 startpage+(limitPage-1)한 값보다 작다면  endpage 그대로 => next없음
        endpage가 startpage+(limitPage-1)한 값보다 크다면 endpage = startpage+(limitPage-1)값으로 => next있음
        * */
        if(endPage > startPage+(limitPage-1)){
            endPage = startPage + (limitPage-1);
        }else{
            nextPage = false;
        }

        /*
        startpage가 1이면 prev 없음
        * */
        if(startPage == 1){
            prevPage = false;
        }
    }

    private void setOrder() {
        if(otext.equals("createtime")){
            ctype = otype;
        }
    }

    public void setCategory(String category) {
        if(category != null){
            this.category = Category.valueOf(category.toUpperCase());
        }
    }

    public void setPage(String page) {
        if(!page.isBlank()){
            this.page = Integer.parseInt(page);
        }

        if(this.page <= 0){
            this.page = 1;
        }
    }

    private void pageGreaterThanEndPage(){
        if(page > endPage){
            page = endPage;
        }
    }
}
