const questionApi = () => {
  return {
    questionList: [
      {
        questionId: '1',

        classTitle: '나의 문의 1',

        directorNickname: '디렉터1',

        text: '나의 문의 내용1',

        answer: '답변1',

        status: '완료',
      },
      {
        questionId: '2',

        classTitle: '나의 문의 2',

        directorNickname: '디렉터2',

        text: '나의 문의 내용2',

        answer: '답변2',

        status: '수정',
      },
      {
        questionId: '3',

        classTitle: '나의 문의 3',

        directorNickname: '디렉터3',

        text: '나의 문의 내용3',

        answer: '답변3',

        status: '대기',
      },
    ],
  };
};

export default questionApi;
