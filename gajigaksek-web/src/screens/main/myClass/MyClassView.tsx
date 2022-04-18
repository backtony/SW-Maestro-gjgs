import React from "react";
import LectureController from "../../../services/controllers/LectureController";
import MyClassHeaderButton from "./MyClassHeaderButton";
import MyClassItem from "./MyClassItem";
import "./MyClassView.css";

export interface MyClassInfo {
  temporaryStoredLectureId: number;
  savedLectureId: number;
  thumbnailImageUrl: string;
  title: string;
  mainText: string;
  lectureStatus: string;
  finished: boolean;
  rejectReason: string;
}

interface MyClassViewProps {
  path: string;
}

interface MyClassViewStates {
  tab: number;
  myClasses: MyClassInfo[];
}

export default class MyClassView extends React.Component<
  MyClassViewProps,
  MyClassViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { tab: 0, myClasses: [] };
    this.getLectures("ALL");
  }

  private async getLectures(condition: string) {
    const params = {
      condition,
    };

    try {
      const res = await LectureController.getMyClass(params);
      if (!res) return;
      const lectureList = res.data.lectureList;
      if (res.data.temporaryStoredLecture) {
        res.data.temporaryStoredLecture.lectureStatus = "CREATING";
        lectureList.push(res.data.temporaryStoredLecture);
      }
      console.log("lectureList: ", lectureList);
      this.setState({ myClasses: lectureList });
    } catch (e) {}
  }
  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header pretendard">내 클래스 목록</div>
        <div className="myclass-list-wrapper">
          <div className="myclass-list-header">
            <MyClassHeaderButton
              title="전체"
              on={this.state.tab === 0}
              onClick={() => this.setState({ tab: 0 })}
            />
            <MyClassHeaderButton
              title="진행중인 클래스"
              on={this.state.tab === 1}
              onClick={() => this.setState({ tab: 1 })}
            />
            <MyClassHeaderButton
              title="작성중인 클래스"
              on={this.state.tab === 2}
              onClick={() => this.setState({ tab: 2 })}
            />
            <MyClassHeaderButton
              title="검수중인 클래스"
              on={this.state.tab === 3}
              onClick={() => this.setState({ tab: 3 })}
            />
            <MyClassHeaderButton
              title="검수거절된 클래스"
              on={this.state.tab === 4}
              onClick={() => this.setState({ tab: 4 })}
            />
            <MyClassHeaderButton
              title="종료된 클래스"
              on={this.state.tab === 5}
              onClick={() => this.setState({ tab: 5 })}
            />
          </div>
          <div className="myclass-splitor" />
          <div className="myclass-list">
            {this.state.myClasses
              .filter((info: MyClassInfo) => {
                switch (this.state.tab) {
                  case 1:
                    return !info.finished && info.lectureStatus === "ACCEPT";
                  case 2:
                    return info.lectureStatus === "CREATING";
                  case 3:
                    return info.lectureStatus === "CONFIRM";
                  case 4:
                    return info.lectureStatus === "REJECT";
                  case 5:
                    return info.finished && info.lectureStatus === "ACCEPT";
                  default:
                    return true;
                }
              })
              .map((info: MyClassInfo) => (
                <MyClassItem
                  title={info.title}
                  lectureId={info.savedLectureId}
                  status={info.lectureStatus}
                  finished={info.finished}
                  rejectReason={info.rejectReason}
                />
              ))}
            <div className="myclass-splitor" />
          </div>
        </div>
      </div>
    );
  }
}
