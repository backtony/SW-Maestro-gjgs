import React from "react";
import LectureController from "../../../services/controllers/LectureController";
import MyClassHeaderButton from "../myClass/MyClassHeaderButton";
import { MyClassInfo } from "../myClass/MyClassView";
import QuestionItem from "./QuestionItem";
import "./QuestionManageView.css";

// const questionDummy: QuestionItemInfo[] = [
//   {
//     lectureInfo: {
//       id: 1,
//       lectureCreatedAt: "2021-10-31-28282828",
//       title: "강남에서 즐기는 푸드 클래스",
//     },
//     questionInfo: {
//       id: 1,
//       questionAnswerText: "",
//       questionCreatedAt: "2021-10-31-28282828",
//       questionMainText:
//         "안녕하세요 문의드립니다! 해당 클래스가 언제 개설 될까요?",
//       questionStatus: "WAIT",
//       questionerId: 1,
//       questionerNickname: "김기완",
//     },
//   },
//   {
//     lectureInfo: {
//       id: 2,
//       lectureCreatedAt: "2021-10-31-28282828",
//       title: "강남에서 즐기는 푸드 클래스",
//     },
//     questionInfo: {
//       id: 2,
//       questionAnswerText: "",
//       questionCreatedAt: "2021-10-31-28282828",
//       questionMainText: "그룹으로 신청하고 싶은데 문의드립니다!",
//       questionStatus: "WAIT",
//       questionerId: 2,
//       questionerNickname: "김기완",
//     },
//   },
// ];

interface QuestionInfo {
  id: number;
  questionAnswerText: string;
  questionCreatedAt: string;
  questionMainText: string;
  questionStatus: string;
  questionerId: number;
  questionerNickname: string;
}

interface LectureInfo {
  id: number;
  lectureCreatedAt: string;
  title: string;
}

interface QuestionItemInfo {
  lectureInfo: LectureInfo;
  questionInfo: QuestionInfo;
}

interface QuestionManageViewProps {
  path: string;
}

interface QuestionManageViewStates {
  tab: number;
  questions: QuestionItemInfo[];
  openedIds: number[];
  on: boolean;
  on2: boolean;
  classes: MyClassInfo[];
  lectureId: number;
}

export default class QuestionManageView extends React.Component<
  QuestionManageViewProps,
  QuestionManageViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      tab: 0,
      questions: [],
      openedIds: [],
      on: false,
      on2: false,
      classes: [],
      lectureId: 0,
    };
    this.getLectures("PROGRESS");
  }
  private async getLectures(condition: string) {
    const params = {
      condition,
    };

    try {
      const res = await LectureController.getMyClass(params);
      if (!res) return;
      const lectureList = res.data.lectureList;

      this.setState({ classes: lectureList }, () =>
        this.getQuestions(this.state.tab === 0 ? "WAIT" : "COMPLETE", 0)
      );
    } catch (e) {}
  }

  private async getQuestions(status: string, lectureId: number) {
    const params = {
      questionStatus: status,
      lectureId,
    };

    try {
      const res = await LectureController.getQuestions(params);
      const data = res?.data;
      this.setState({ questions: data.content });
      // this.setState({ questions: questionDummy });
    } catch (e) {}
  }

  private checkAnswer(answer: string) {
    if (!answer) {
      alert("답변을 입력해주세요.");
      return false;
    }

    if (answer.length < 10 || answer.length > 1000) {
      alert("답변은 10자이상 1000자이하로 작성해주세요.");
      return false;
    }

    return true;
  }

  private async putAnswer(answer: string, questionId: number) {
    const params = { replyText: answer };

    if (!this.checkAnswer(answer)) return;

    try {
      await LectureController.putAnswer(params, questionId);
      alert("답변등록이 완료되었습니다.");
      window.location.href = "/main/question-manage";
    } catch (e) {}
  }

  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header pretendard">문의 관리</div>
        <div className="myclass-list-wrapper">
          <div className="myclass-list-header">
            <MyClassHeaderButton
              title="답변 대기중"
              on={this.state.tab === 0}
              onClick={() =>
                this.setState({ tab: 0 }, () => this.getQuestions("WAIT", 0))
              }
            />
            <MyClassHeaderButton
              title="답변 완료"
              on={this.state.tab === 1}
              onClick={() =>
                this.setState({ tab: 1 }, () =>
                  this.getQuestions("COMPLETE", 0)
                )
              }
            />
          </div>
          <div className="myclass-splitor" />
          <div className="question-manage-header">
            <div className="pretendard">{`전체 ${this.state.questions.length}개`}</div>
            <select
              className="class-add-curriculum-info-select pretendard meme"
              value={this.state.lectureId}
              onChange={(e: any) =>
                this.setState({ lectureId: e.target.value }, () =>
                  this.getQuestions(
                    this.state.tab === 0 ? "WAIT" : "COMPLETE",
                    this.state.lectureId
                  )
                )
              }
            >
              <option value={0}>전체</option>
              {this.state.classes.map((myclass: MyClassInfo) => (
                <option value={myclass.savedLectureId}>{myclass.title}</option>
              ))}
            </select>
          </div>
          <div className="question-manage-list">
            {this.state.questions.map(
              (item: QuestionItemInfo, index: number) => (
                <div>
                  <QuestionItem
                    key={index}
                    title={item.questionInfo.questionMainText}
                    answerText={item.questionInfo.questionAnswerText}
                    classTitle={item.lectureInfo.title}
                    open={this.state.openedIds.includes(item.questionInfo.id)}
                    onClick={() => {
                      let ids = this.state.openedIds;

                      if (ids.includes(item.questionInfo.id)) {
                        ids = ids.filter(
                          (id: number) => id !== item.questionInfo.id
                        );
                      } else {
                        ids.push(item.questionInfo.id);
                      }
                      this.setState({ openedIds: ids });
                    }}
                    text={item.questionInfo.questionMainText}
                    buttonVisible={this.state.tab === 0}
                    onSend={(text: string) =>
                      this.putAnswer(text, item.questionInfo.id)
                    }
                  />
                  <div className="myclass-splitor" />
                </div>
              )
            )}
          </div>
        </div>
      </div>
    );
  }
}
