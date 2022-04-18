import React from "react";
import ClassAddNaviButton from "../classAdd/ClassAddNaviButton";
import ScheduleBlock from "../classAdd/schedule/ScheduleBlock";
import SchedulePopup, {
  ScheduleInfo,
} from "../classAdd/schedule/popup/SchedulePopup";
import { koDayList } from "../../../utils/commonParams";
import "./MyClassScheduleView.css";
import LectureController from "../../../services/controllers/LectureController";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface LectureScheduleInfo {
  canDelete: boolean;
  currentParticipants: number;
  endHour: number;
  endMinute: number;
  lectureDate: string;
  progressMinute: number;
  scheduleId: number;
  startHour: number;
  startMinute: number;
}

interface MyClassScheduleViewStates {
  schedules: LectureScheduleInfo[];
  popup: boolean;
  progressHour: number;
  progressMinute: number;
  minPeople: number;
  maxPeople: number;
}

const hour = [
  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
  22, 23, 24,
];

const minute = [0, 30];

export default class MyClassScheduleView extends React.Component<
  Record<string, never>,
  MyClassScheduleViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      schedules: [],
      popup: false,
      progressHour: 2,
      progressMinute: 0,
      minPeople: 1,
      maxPeople: 1,
    };
    this.getLectureSchedule(cookies.get("schedule_lectureId"));
  }

  private async getLectureSchedule(lectureId: number) {
    try {
      const res = await LectureController.getLectureSchedule(lectureId);
      if (!res) return;

      let progressMinute = 0;

      if (res.data.scheduleList.length > 0) {
        const startMinute =
          res.data.scheduleList[0].startHour * 60 +
          res.data.scheduleList[0].startMinute;
        const endMinute =
          res.data.scheduleList[0].endHour * 60 +
          res.data.scheduleList[0].endMinute;

        progressMinute = endMinute - startMinute;
      } else {
        progressMinute = 150;
      }

      this.setState({
        schedules: res.data.scheduleList,
        minPeople: res.data.minParticipants,
        maxPeople: res.data.maxParticipants,
        progressHour: parseInt(JSON.stringify(progressMinute / 60)),
        progressMinute: progressMinute % 60,
      });
    } catch (e) {}
  }

  private async deleteLectureSchedule(
    lectureId: number,
    scheduleId: number,
    callback: () => void
  ) {
    try {
      await LectureController.deleteLectureSchedule(lectureId, scheduleId);
      callback();
    } catch (e) {}
  }

  private async postLectureSchedule(
    lectureId: number,
    scheduleInfo: ScheduleInfo
  ) {
    if (!this.checkForm()) return;
    const endMinute =
      scheduleInfo.startHour * 60 +
      scheduleInfo.startMinute +
      scheduleInfo.lectureProgressMinute;

    const params = {
      canDelete: true,
      currentParticipants: 0,
      endHour: parseInt(JSON.stringify(endMinute / 60)),
      endMinute: endMinute % 60,
      lectureDate: scheduleInfo.lectureDate,
      progressMinute: scheduleInfo.lectureProgressMinute,
      startHour: scheduleInfo.startHour,
      startMinute: scheduleInfo.startMinute,
    };

    try {
      await LectureController.postLectureSchedule(lectureId, params);
    } catch (e) {}
  }

  private checkForm() {
    if (this.state.progressHour < 1) {
      alert("클래스 시간은 최소 1시간이상 설정해주세요.");
      return false;
    }
    return true;
  }

  render() {
    return (
      <div className="class-add-first-container">
        <div className="class-add-first-header">
          <button
            className="class-add-first-back myclass-schedule-view-header"
            onClick={() => (window.location.href = "/main/myclass")}
          >
            <i className="fas fa-chevron-left fa-3x"></i>
          </button>
          <div className="class-add-first-header-title myclass-schedule-view-header pretendard">
            클래스 스케줄 변경
          </div>
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-first-name-container">
            <div className="class-add-schedule-header">
              <div className="class-add-mainCategory-title pretendard">
                스케줄 등록
              </div>
              <div className="class-add-schedule-time-wrapper">
                <div className="class-add-schedule-time-text1">수업시간</div>
                <div className="class-add-schedule-time-select-container">
                  <select
                    onChange={(event: any) => {
                      if (this.state.schedules.length > 0) {
                        alert("모든 스케줄을 지워야 변경 할 수 있습니다.");
                        return;
                      }
                      this.setState({ progressHour: event.target.value });
                    }}
                    value={this.state.progressHour}
                  >
                    {hour.map((h) => (
                      <option value={h}>{`${h}시간`}</option>
                    ))}
                  </select>
                  <select
                    onChange={(event: any) => {
                      if (this.state.schedules.length > 0) {
                        alert("모든 스케줄을 지워야 변경 할 수 있습니다.");
                        return;
                      }
                      this.setState({ progressMinute: event.target.value });
                    }}
                    value={this.state.progressMinute}
                  >
                    {minute.map((m) => (
                      <option value={m}>{`${m}분`}</option>
                    ))}
                  </select>
                </div>
              </div>
              <div className="class-add-schedule-time-wrapper">
                <div className="class-add-schedule-time-text1">인원</div>
                <div className="class-add-schedule-time-select-container">
                  <div className="class-add-schedule-popup-date-input-container add-flex-start">
                    <div className="class-add-schedule-popup-date-input-wrapper number-input">
                      <input
                        type="number"
                        className="pretendard"
                        min={1}
                        max={this.state.maxPeople}
                        value={this.state.minPeople}
                        onChange={(event: any) => {
                          if (this.state.schedules.length > 0) {
                            alert("모든 스케줄을 지워야 변경 할 수 있습니다.");
                            return;
                          }
                          this.setState({ minPeople: event.target.value });
                        }}
                      ></input>
                      <div>명</div>
                    </div>
                    <div className="margin-vertical">~</div>
                    <div className="class-add-schedule-popup-date-input-wrapper number-input">
                      <input
                        type="number"
                        className="pretendard"
                        min={this.state.minPeople}
                        value={this.state.maxPeople}
                        onChange={(event: any) => {
                          if (this.state.schedules.length > 0) {
                            alert("모든 스케줄을 지워야 변경 할 수 있습니다.");
                            return;
                          }
                          this.setState({ maxPeople: event.target.value });
                        }}
                      ></input>
                      <div>명</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="class-add-schedule-item-container">
              {this.state.schedules.map((item: LectureScheduleInfo) => {
                const date = new Date(item.lectureDate);

                const startDateFormat = `${date.getFullYear()}.${
                  date.getMonth() + 1
                }.${date.getDate()}(${koDayList[date.getDay()]}) ${
                  item.startHour < 10 ? "0" : ""
                }${item.startHour}시 ${item.startMinute < 10 ? "0" : ""}${
                  item.startMinute
                }분`;

                const endTimeMinute =
                  +(+item.startHour * 60) +
                  +item.startMinute +
                  +(+this.state.progressHour * 60) +
                  +this.state.progressMinute;

                const endHour = Math.floor(endTimeMinute / 60);

                const endMinute = endTimeMinute % 60;

                const endDateFormat = `${date.getFullYear()}.${
                  date.getMonth() + 1
                }.${date.getDate()}(${koDayList[date.getDay()]}) ${
                  endHour < 10 ? "0" : ""
                }${endHour}시 ${endMinute < 10 ? "0" : ""}${endMinute}분`;

                return (
                  <ScheduleBlock
                    date={`${startDateFormat} ~ ${endDateFormat}`}
                    people={`${this.state.minPeople} ~ ${this.state.maxPeople}명`}
                    onClick={() => {
                      if (!item.canDelete) {
                        alert("해당 스케줄은 삭제할 수 었습니다.");
                        return;
                      }
                      this.deleteLectureSchedule(
                        cookies.get("schedule_lectureId"),
                        item.scheduleId,
                        () => window.location.reload()
                      );
                    }}
                  />
                );
              })}
            </div>
            <button
              className="class-add-schedule-button pretendard"
              onClick={() => this.setState({ popup: true })}
            >
              <i className="fas fa-plus fa-lg"></i>
              <div>스케줄 추가</div>
            </button>
          </div>
        </div>

        <SchedulePopup
          progressMinute={
            +this.state.progressMinute + +(this.state.progressHour * 60)
          }
          on={this.state.popup}
          onClickExit={() => {
            this.setState({ popup: false });
          }}
          addSchedule={(infoList: ScheduleInfo[]) => {
            infoList.forEach((scheduleInfo: ScheduleInfo) => {
              this.postLectureSchedule(
                cookies.get("schedule_lectureId"),
                scheduleInfo
              );
              window.location.reload();
            });
          }}
        />
      </div>
    );
  }
}
