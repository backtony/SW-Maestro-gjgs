import React from "react";
import AdminLectureController from "../../../services/controllers/AdminLectureController";
import BackofficeClassBlock from "./BackofficeClassBlock";

export interface BackofficeClassInfo {
  lectureId: number;
  lectureTitle: string;
  confirmDateTime: string;
  directorNickname: string;
  categoryId: number;
  categoryName: string;
  zoneId: number;
  zoneName: string;
}

interface BackofficeClassViewStates {
  lectures: BackofficeClassInfo[];
}

export default class BackofficeClassView extends React.Component<
  Record<string, never>,
  BackofficeClassViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { lectures: [] };
    this.getLectures();
  }

  private async getLectures() {
    try {
      const res = await AdminLectureController.getLectures();
      this.setState({ lectures: res.data.content });
    } catch (e) {}
  }

  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header backoffice-noti-header pretendard">
          클래스 검수
        </div>
        <div>
          {this.state.lectures.map((lecture: BackofficeClassInfo) => (
            <BackofficeClassBlock lecture={lecture} />
          ))}
        </div>
      </div>
    );
  }
}
