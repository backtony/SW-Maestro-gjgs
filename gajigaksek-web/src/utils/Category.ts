const categoryMap = [
  //스포츠
  ["", "", ""],
  ["액티비티", "", "1"],
  ["액티비티", "보드", "2"],
  ["액티비티", "클라이밍", "3"],
  ["액티비티", "다이빙", "4"],
  ["액티비티", "스키/스노우보드", "5"],
  ["액티비티", "테니스", "6"],
  ["액티비티", "배드민턴", "7"],
  ["액티비티", "펜싱", "8"],
  ["액티비티", "탁구", "9"],
  ["액티비티", "사격", "10"],
  //쿠킹
  ["쿠킹", "", "11"],
  ["쿠킹", "한식", "12"],
  ["쿠킹", "양식", "13"],
  ["쿠킹", "중식", "14"],
  ["쿠킹", "일식", "15"],
  ["쿠킹", "베이킹/디저트", "16"],
  ["쿠킹", "떡/한과", "17"],
  ["쿠킹", "바리스타", "18"],
  ["쿠킹", "비건", "19"],
  //뷰티/헬스
  ["뷰티/헬스", "", "20"],
  ["뷰티/헬스", "요가", "21"],
  ["뷰티/헬스", "필라테스", "22"],
  ["뷰티/헬스", "헬스", "23"],
  ["뷰티/헬스", "얼굴", "24"],
  ["뷰티/헬스", "헤어", "25"],
  ["뷰티/헬스", "애견미용", "26"],
  ["뷰티/헬스", "네일", "27"],
  ["뷰티/헬스", "퍼스널컬러", "28"],
  //댄스
  ["댄스", "", "29"],
  ["댄스", "방송댄스", "30"],
  ["댄스", "재즈댄스", "31"],
  ["댄스", "발레", "32"],
  ["댄스", "한국무용", "33"],
  ["댄스", "벨리댄스", "34"],
  ["댄스", "비보이/스트릿댄스", "35"],
  //미술
  ["미술", "", "36"],
  ["미술", "인물화", "37"],
  ["미술", "서양화", "38"],
  ["미술", "수제화", "39"],
  ["미술", "파스텔", "40"],
  ["미술", "드로잉", "41"],
  ["미술", "만화/캐리커처", "42"],
  ["미술", "캘리그라피", "43"],
  ["미술", "사진", "44"],
  ["미술", "영상편집", "45"],
  ["미술", "페인팅", "46"],
  ["미술", "타투", "47"],
  //수공예
  ["수공예", "", "48"],
  ["수공예", "천연비누", "49"],
  ["수공예", "입욕제", "50"],
  ["수공예", "화장품", "51"],
  ["수공예", "악세사리", "52"],
  ["수공예", "향수", "53"],
  ["수공예", "디퓨저", "54"],
  ["수공예", "캔들", "55"],
  ["수공예", "도예", "56"],
  ["수공예", "미싱/재봉틀", "57"],
  ["수공예", "자수", "58"],
  ["수공예", "가죽", "59"],
  ["수공예", "목공", "60"],
  ["수공예", "페이퍼플라워", "61"],
  ["수공예", "플라워", "62"],
  //음악/예술
  ["음악/예술", "", "63"],
  ["음악/예술", "보컬", "64"],
  ["음악/예술", "기타/베이스", "65"],
  ["음악/예술", "드럼", "66"],
  ["음악/예술", "피아노", "67"],
  ["음악/예술", "작사/작곡", "68"],
  ["음악/예술", "연기", "69"],
  ["음악/예술", "전통악기", "70"],
  ["음악/예술", "랩", "71"],
];

const mainCategoryList = [
  "액티비티",
  "쿠킹",
  "뷰티/헬스",
  "댄스",
  "미술",
  "수공예",
  "음악/예술",
];

const mainCategoryImageName = [
  ["액티비티", "activity.png"],
  ["쿠킹", "cooking.png"],
  ["뷰티/헬스", "beautyHealth.png"],
  ["댄스", "dance.png"],
  ["미술", "art.png"],
  ["수공예", "handcraft.png"],
  ["음악/예술", "music.png"],
];

const mainCategoryUnsplash = [
  [
    "액티비티",
    "https://images.unsplash.com/photo-1502680390469-be75c86b636f?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2550&q=80",
  ],
  [
    "쿠킹",
    "https://images.unsplash.com/photo-1547592180-85f173990554?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80",
  ],
  [
    "뷰티/헬스",
    "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80",
  ],
  [
    "댄스",
    "https://images.unsplash.com/photo-1547153760-18fc86324498?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80",
  ],
  [
    "미술",
    "https://images.unsplash.com/photo-1537884557178-342a575d7d16?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80",
  ],
  [
    "수공예",
    "https://images.unsplash.com/photo-1604510493959-8a20b3630d53?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1100&q=80",
  ],
  [
    "음악/예술",
    "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80",
  ],
];

export class CategoryPair {
  constructor(public main: string, public sub: string, public id: string) {}
}

class Category {
  public getCategory(categoryId: number) {
    return categoryMap[categoryId];
  }

  public getId(mainCategory: string, subCategory: string) {
    let targetIndex = 1;
    categoryMap.some((value, index) => {
      if (value[0] === mainCategory && value[1] === subCategory) {
        targetIndex = index;
        return true;
      }
    });
    return targetIndex;
  }

  public getIdWithSub(subCategory: string) {
    let targetIndex = 1;
    categoryMap.some((value, index) => {
      if (value[1] === subCategory) {
        targetIndex = index;
        return true;
      }
    });
    return targetIndex;
  }

  public getSubCategoryList(mainCategory: string): CategoryPair[] {
    return categoryMap
      .filter((value) => value[0] === mainCategory && value[1] !== "")
      .map((value) => new CategoryPair(value[0], value[1], value[2]));
  }

  public getCategoryList(): string[] {
    return mainCategoryList;
  }

  public getMainId(id: number): number {
    if (id < 11) return 1;
    else if (id < 20) return 11;
    else if (id < 29) return 20;
    else if (id < 36) return 29;
    else if (id < 48) return 36;
    else if (id < 63) return 48;
    else return 63;
  }

  public getUnsplashImageUrl(main: string) {
    return mainCategoryUnsplash.filter((value) => value[0] === main)[0][1];
  }
}

export default new Category();
