export const getDayOfWeekInKorean = (day: string) => {
  switch (day) {
    case 'SUN':
      return '일';
    case 'MON':
      return '월';
    case 'TUE':
      return '화';
    case 'WED':
      return '수';
    case 'THU':
      return '목';
    case 'FRI':
      return '금';
    case 'SAT':
      return '토';
    default:
      return '토';
  }
};

export const getDayOfWeekInEnglish = (day: string) => {
  switch (day) {
    case '일':
      return 'SUN';
    case '월':
      return 'MON';
    case '화':
      return 'TUE';
    case '수':
      return 'WED';
    case '목':
      return 'THU';
    case '금':
      return 'FRI';
    case '토':
      return 'SAT';
    default:
      return 'SAT';
  }
};

export const getTimeInKorean = (time: string) => {
  switch (time) {
    case 'MORNING':
      return '오전';
    case 'NOON':
      return '오후';
    case 'AFTERNOON':
      return '저녁';
    default:
      return '오후';
  }
};

export const getTimeInEnglish = (time: string) => {
  switch (time) {
    case '오전':
      return 'MORNING';
    case '오후':
      return 'NOON';
    case '저녁':
      return 'AFTERNOON';
    default:
      return '오후';
  }
};

export const parseDate = (date: string) => {
  return `${date.substr(0, 4)}.${date.substr(5, 2)}.${date.substr(
    8,
    2,
  )} ${date.substr(11, 5)}`;
};
