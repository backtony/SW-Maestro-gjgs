// export const API_URL = "http://localhost:8080/api/v1";
// export const API_URL = "http://3.37.110.127:8080/api/v1"; // 현재 가용가능 테스트 서버
export const API_URL = "http://127.0.0.1:8080/api/v1";

export type strJsonType = {
  [index: string]: string;
};

export type numberJsonType = {
  [index: string]: number;
};

export const koreanDayType: strJsonType = {
  일: "SUN",
  월: "MON",
  화: "TUE",
  수: "WED",
  목: "THU",
  금: "FRI",
  토: "SAT",
};

export const enDayList = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
export const koDayList = ["일", "월", "화", "수", "목", "금", "토"];

export const dataURLtoFile = (dataurl: string, filename: string) => {
  const arr = dataurl.split(",");
  const cibal = arr[0].match(/:(.*?);/);
  if (!cibal) return;
  const mime = cibal[1],
    bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);

  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }

  return new File([u8arr], filename, { type: mime });
};

export const filterNumber = (text: string) => {
  return +text.replace(/[^0-9.]/g, "").replace(/(\..*?)\..*/g, "$1");
};

export const parsedDate: (date: Date) => string = (date: Date) => {
  return `${date.getFullYear()}-${date.getMonth() + 1 < 10 ? "0" : ""}${
    date.getMonth() + 1
  }-${date.getDate() < 10 ? "0" : ""}${date.getDate()}`;
};

export const parseTime: (date: Date) => string = (date: Date) => {
  const timeStr = date.toTimeString().substr(0, 5);
  //13:48:11 GMT+0900 (한국 표준시)
  let hh = +timeStr.substr(0, 2);
  const mm = timeStr.substr(2, 3);

  let ret = "오전 ";

  if (hh > 12) {
    ret = "오후 ";
    hh -= 12;
  }

  ret += hh;
  ret += mm;

  return ret;
};
